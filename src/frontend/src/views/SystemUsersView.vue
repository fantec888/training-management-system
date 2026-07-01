<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col :xs="12" :lg="6" v-for="item in roleStats" :key="item.code">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.name }}</span>
          <strong class="metric-value">{{ item.count }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>后台账号中心</strong>
            <span>维护管理员、财务、维修、物业人员的角色权限和账号状态</span>
          </div>
          <div class="toolbar-right">
            <el-button @click="roleVisible = true">角色配置</el-button>
            <el-button type="primary" @click="openCreate">新增账号</el-button>
          </div>
        </div>
      </template>

      <el-table :data="systemUsers" v-if="systemUsers.length">
        <el-table-column prop="realName" label="姓名" width="120" fixed="left" />
        <el-table-column prop="username" label="账号" width="140" />
        <el-table-column label="角色" width="150">
          <template #default="{ row }">
            <el-tag effect="plain">{{ roleName(row.roleCode) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属部门" width="160" />
        <el-table-column prop="lastLoginAt" label="最近登录" width="180" />
        <el-table-column prop="permissionScope" label="权限范围" min-width="220" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'" effect="plain">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link :type="row.enabled ? 'danger' : 'primary'" @click="toggleStatus(row)">
              {{ row.enabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无系统用户数据" />
    </el-card>

    <el-dialog v-model="formVisible" :title="form.id ? '编辑系统账号' : '新增系统账号'" width="560px">
      <el-alert
        v-if="!form.id"
        title="新账号默认密码为 123456，创建后可用于演示登录。"
        type="info"
        show-icon
        :closable="false"
        class="dialog-alert"
      />
      <el-form :model="form" label-position="top">
        <el-form-item label="登录账号">
          <el-input v-model="form.username" :disabled="Boolean(form.id)" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.roleCode" style="width: 100%" @change="applyRolePreset">
            <el-option v-for="role in roles" :key="role.code" :label="role.name" :value="role.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="form.department" placeholder="请输入所属部门" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-input v-model="form.permissionScope" placeholder="请输入权限范围" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="账号状态">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUser">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="roleVisible" title="角色配置" width="760px">
      <el-table :data="roles">
        <el-table-column prop="name" label="角色名称" width="130" />
        <el-table-column prop="code" label="角色编码" width="160" />
        <el-table-column prop="department" label="默认部门" width="140" />
        <el-table-column prop="scope" label="权限说明" />
      </el-table>
      <template #footer>
        <el-button type="primary" @click="roleVisible = false">知道了</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  createSystemUser,
  fetchSystemUsers,
  updateSystemUser,
  updateSystemUserStatus,
} from '../api/property'
import { systemUsersFallback } from '../data/mock'

const systemUsers = ref(systemUsersFallback)
const formVisible = ref(false)
const roleVisible = ref(false)
const form = reactive(defaultUser())

const roles = [
  { name: '管理员', code: 'SUPER_ADMIN', department: '平台运营中心', scope: '系统全模块配置、账号管理和核心运营数据查看' },
  { name: '财务', code: 'FINANCE_ADMIN', department: '财务部', scope: '收费账单、缴费确认、欠费提醒和收缴统计' },
  { name: '维修', code: 'ENGINEER_LEAD', department: '工程维修部', scope: '报修工单、派单处理、楼栋巡检和设备维护' },
  { name: '物业人员', code: 'SERVICE_MANAGER', department: '客户服务部', scope: '住户档案、公告活动、报修跟进和日常服务' },
]

const roleStats = computed(() => roles.map((role) => ({
  ...role,
  count: systemUsers.value.filter((user) => user.roleCode === role.code).length,
})))

async function loadSystemUsers() {
  try {
    const response = await fetchSystemUsers()
    systemUsers.value = response.data
  } catch (error) {
    ElMessage.warning('系统用户数据暂时无法加载，已显示默认状态')
    systemUsers.value = systemUsersFallback
  }
}

function openCreate() {
  Object.assign(form, defaultUser())
  formVisible.value = true
}

function openEdit(row) {
  Object.assign(form, { ...row })
  formVisible.value = true
}

async function submitUser() {
  if (!form.username || !form.realName || !form.department) {
    ElMessage.warning('请补全账号、姓名和部门')
    return
  }
  if (form.id) {
    await updateSystemUser(form.id, form)
    ElMessage.success('账号信息已更新')
  } else {
    await createSystemUser(form)
    ElMessage.success('系统账号已创建，默认密码 123456')
  }
  formVisible.value = false
  await loadSystemUsers()
}

async function toggleStatus(row) {
  await updateSystemUserStatus(row.id, !row.enabled)
  ElMessage.success('账号状态已更新')
  await loadSystemUsers()
}

function applyRolePreset(roleCode) {
  const role = roles.find((item) => item.code === roleCode)
  if (!role) return
  form.department = role.department
  form.permissionScope = role.scope
}

function roleName(roleCode) {
  return roles.find((item) => item.code === roleCode)?.name || roleCode
}

function defaultUser() {
  return {
    id: null,
    username: '',
    realName: '',
    roleCode: 'SERVICE_MANAGER',
    department: '客户服务部',
    permissionScope: '住户档案、公告活动、报修跟进和日常服务',
    phone: '',
    enabled: true,
  }
}

onMounted(loadSystemUsers)
</script>
