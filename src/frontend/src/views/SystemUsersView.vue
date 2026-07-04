<template>
  <section class="page-grid rbac-page">
    <el-row :gutter="12">
      <el-col :xs="12" :lg="6" v-for="item in roleStats" :key="item.roleCode">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.roleName }}</span>
          <strong class="metric-value">{{ item.count }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>RBAC 权限中心</strong>
            <span>用户、角色、菜单权限、按钮权限和授权关系统一维护</span>
          </div>
          <div class="toolbar-right">
            <el-button v-if="canDo('button:permission:manage')" @click="openPermissionCreate">新增权限</el-button>
            <el-button v-if="canDo('button:role:manage')" @click="openRoleCreate">新增角色</el-button>
            <el-button v-if="canDo('button:user:create')" type="primary" @click="openUserCreate">新增账号</el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="rbac-tabs">
        <el-tab-pane label="用户管理" name="users">
          <el-table :data="pagedSystemUsers" v-loading="loading">
            <el-table-column prop="realName" label="姓名" width="120" fixed="left" />
            <el-table-column prop="username" label="账号" width="140" />
            <el-table-column label="主角色" width="150">
              <template #default="{ row }">
                <el-tag effect="plain">{{ roleName(row.roleCode) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="department" label="部门" width="160" />
            <el-table-column prop="permissionScope" label="权限说明" min-width="220" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'" effect="plain">
                  {{ row.enabled ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="260" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:user-role:assign')" link type="primary" @click="openUserRole(row)">分配角色</el-button>
                <el-button v-if="canDo('button:user:update')" link type="primary" @click="openUserEdit(row)">编辑</el-button>
                <el-button v-if="canDo('button:user:update')" link :type="row.enabled ? 'danger' : 'primary'" @click="toggleStatus(row)">
                  {{ row.enabled ? '禁用' : '启用' }}
                </el-button>
                <el-button v-if="canDo('button:user:delete')" link type="danger" @click="removeUser(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="table-pagination" v-if="systemUsers.length">
            <el-pagination
              v-model:current-page="userPage"
              v-model:page-size="userPageSize"
              :page-sizes="userPageSizes"
              :total="userTotal"
              layout="total, sizes, prev, pager, next, jumper"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="角色管理" name="roles">
          <el-table :data="pagedRoles" v-loading="loading">
            <el-table-column prop="roleName" label="角色名称" width="150" />
            <el-table-column prop="roleCode" label="角色编码" width="180" />
            <el-table-column prop="description" label="说明" min-width="260" />
            <el-table-column prop="sortOrder" label="排序" width="90" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'" effect="plain">{{ row.enabled ? '启用' : '禁用' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:role-permission:assign')" link type="primary" @click="openRolePermission(row)">授权</el-button>
                <el-button v-if="canDo('button:role:manage')" link type="primary" @click="openRoleEdit(row)">编辑</el-button>
                <el-button v-if="canDo('button:role:manage')" link type="danger" @click="removeRole(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="table-pagination" v-if="roles.length">
            <el-pagination
              v-model:current-page="rolePage"
              v-model:page-size="rolePageSize"
              :page-sizes="rolePageSizes"
              :total="roleTotal"
              layout="total, sizes, prev, pager, next, jumper"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="菜单权限" name="permissions">
          <el-table :data="pagedPermissions" row-key="id" v-loading="loading">
            <el-table-column prop="permissionName" label="权限名称" width="170" />
            <el-table-column prop="permissionCode" label="权限编码" min-width="230" />
            <el-table-column label="类型" width="100">
              <template #default="{ row }">
                <el-tag :type="row.permissionType === 'MENU' ? 'primary' : 'warning'" effect="plain">
                  {{ row.permissionType === 'MENU' ? '菜单' : '按钮' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="routePath" label="路由" width="130" />
            <el-table-column prop="componentKey" label="模块" width="130" />
            <el-table-column prop="sortOrder" label="排序" width="90" />
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:permission:manage')" link type="primary" @click="openPermissionEdit(row)">编辑</el-button>
                <el-button v-if="canDo('button:permission:manage')" link type="danger" @click="removePermission(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="table-pagination" v-if="permissions.length">
            <el-pagination
              v-model:current-page="permissionPage"
              v-model:page-size="permissionPageSize"
              :page-sizes="permissionPageSizes"
              :total="permissionTotal"
              layout="total, sizes, prev, pager, next, jumper"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="授权分配" name="assign">
          <div class="assignment-grid">
            <div class="assignment-panel">
              <strong>用户角色分配</strong>
              <el-select v-model="selectedUserId" placeholder="选择用户" style="width: 100%" @change="loadUserRoles">
                <el-option v-for="user in systemUsers" :key="user.id" :label="`${user.realName}（${user.username}）`" :value="user.id" />
              </el-select>
              <el-checkbox-group v-model="userRoleCodes" class="check-list">
                <el-checkbox v-for="role in roles" :key="role.roleCode" :label="role.roleCode">
                  {{ role.roleName }}
                </el-checkbox>
              </el-checkbox-group>
              <el-button type="primary" :disabled="!selectedUserId || !canDo('button:user-role:assign')" @click="saveUserRoles">
                保存用户角色
              </el-button>
            </div>

            <div class="assignment-panel">
              <strong>角色菜单/按钮授权</strong>
              <el-select v-model="selectedRoleId" placeholder="选择角色" style="width: 100%" @change="loadRolePermissions">
                <el-option v-for="role in roles" :key="role.id" :label="role.roleName" :value="role.id" />
              </el-select>
              <el-checkbox-group v-model="rolePermissionCodes" class="check-list permission-checks">
                <el-checkbox v-for="permission in permissions" :key="permission.permissionCode" :label="permission.permissionCode">
                  {{ permission.permissionName }} / {{ permission.permissionCode }}
                </el-checkbox>
              </el-checkbox-group>
              <el-button type="primary" :disabled="!selectedRoleId || !canDo('button:role-permission:assign')" @click="saveRolePermissions">
                保存角色权限
              </el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="userDialogVisible" :title="userForm.id ? '编辑系统账号' : '新增系统账号'" width="560px">
      <el-alert v-if="!userForm.id" title="新账号默认密码为 123456，登录时从数据库用户表校验。" type="info" show-icon :closable="false" class="dialog-alert" />
      <el-form :model="userForm" label-position="top">
        <el-form-item label="登录账号"><el-input v-model="userForm.username" :disabled="Boolean(userForm.id)" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="userForm.realName" /></el-form-item>
        <el-form-item label="主角色">
          <el-select v-model="userForm.roleCode" style="width: 100%" @change="applyRolePreset">
            <el-option v-for="role in roles" :key="role.roleCode" :label="role.roleName" :value="role.roleCode" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门"><el-input v-model="userForm.department" /></el-form-item>
        <el-form-item label="权限说明"><el-input v-model="userForm.permissionScope" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="userForm.phone" /></el-form-item>
        <el-form-item label="账号状态"><el-switch v-model="userForm.enabled" active-text="启用" inactive-text="禁用" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUser">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="roleDialogVisible" :title="roleForm.id ? '编辑角色' : '新增角色'" width="560px">
      <el-form :model="roleForm" label-position="top">
        <el-form-item label="角色名称"><el-input v-model="roleForm.roleName" /></el-form-item>
        <el-form-item label="角色编码"><el-input v-model="roleForm.roleCode" /></el-form-item>
        <el-form-item label="角色说明"><el-input v-model="roleForm.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="roleForm.sortOrder" :min="1" :max="999" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="roleForm.enabled" active-text="启用" inactive-text="禁用" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRole">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="permissionDialogVisible" :title="permissionForm.id ? '编辑权限' : '新增权限'" width="600px">
      <el-form :model="permissionForm" label-position="top">
        <el-form-item label="权限名称"><el-input v-model="permissionForm.permissionName" /></el-form-item>
        <el-form-item label="权限编码"><el-input v-model="permissionForm.permissionCode" placeholder="例如 button:user:create" /></el-form-item>
        <el-form-item label="权限类型">
          <el-radio-group v-model="permissionForm.permissionType">
            <el-radio-button label="MENU">菜单</el-radio-button>
            <el-radio-button label="BUTTON">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属菜单">
          <el-select v-model="permissionForm.parentId" clearable style="width: 100%">
            <el-option v-for="item in menuPermissions" :key="item.id" :label="item.permissionName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="路由路径"><el-input v-model="permissionForm.routePath" /></el-form-item>
        <el-form-item label="模块标识"><el-input v-model="permissionForm.componentKey" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="permissionForm.sortOrder" :min="1" :max="999" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPermission">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  assignRolePermissions,
  assignUserRoles,
  createPermission,
  createRole,
  createSystemUser,
  deletePermission,
  deleteRole,
  deleteSystemUser,
  fetchPermissions,
  fetchRolePermissions,
  fetchRoles,
  fetchSystemUsers,
  fetchUserRoles,
  updatePermission,
  updateRole,
  updateSystemUser,
  updateSystemUserStatus,
} from '../api/property'
import { getUser } from '../utils/auth'
import { useClientPagination } from '../utils/pagination'
import { hasPermission } from '../utils/roles'

const activeTab = ref('users')
const loading = ref(false)
const systemUsers = ref([])
const roles = ref([])
const permissions = ref([])
const selectedUserId = ref(null)
const selectedRoleId = ref(null)
const userRoleCodes = ref([])
const rolePermissionCodes = ref([])

const userDialogVisible = ref(false)
const roleDialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const userForm = reactive(defaultUser())
const roleForm = reactive(defaultRole())
const permissionForm = reactive(defaultPermission())

const currentUser = computed(() => getUser())
const menuPermissions = computed(() => permissions.value.filter((item) => item.permissionType === 'MENU'))
const roleStats = computed(() => roles.value.map((role) => ({
  ...role,
  count: systemUsers.value.filter((user) => user.roleCode === role.roleCode).length,
})))
const {
  page: userPage,
  pageSize: userPageSize,
  pageSizes: userPageSizes,
  total: userTotal,
  records: pagedSystemUsers,
} = useClientPagination(systemUsers)
const {
  page: rolePage,
  pageSize: rolePageSize,
  pageSizes: rolePageSizes,
  total: roleTotal,
  records: pagedRoles,
} = useClientPagination(roles)
const {
  page: permissionPage,
  pageSize: permissionPageSize,
  pageSizes: permissionPageSizes,
  total: permissionTotal,
  records: pagedPermissions,
} = useClientPagination(permissions, { pageSize: 10, pageSizes: [10, 20, 50] })

function canDo(code) {
  return hasPermission(currentUser.value, code)
}

async function loadAll() {
  loading.value = true
  try {
    const [usersRes, rolesRes, permissionsRes] = await Promise.all([
      fetchSystemUsers(),
      fetchRoles(),
      fetchPermissions(),
    ])
    systemUsers.value = usersRes.data
    roles.value = rolesRes.data
    permissions.value = permissionsRes.data
  } finally {
    loading.value = false
  }
}

function openUserCreate() {
  Object.assign(userForm, defaultUser())
  userDialogVisible.value = true
}

function openUserEdit(row) {
  Object.assign(userForm, { ...row })
  userDialogVisible.value = true
}

async function submitUser() {
  if (!userForm.username || !userForm.realName || !userForm.roleCode) {
    ElMessage.warning('请补全账号、姓名和角色')
    return
  }
  if (userForm.id) {
    await updateSystemUser(userForm.id, userForm)
    ElMessage.success('账号信息已更新')
  } else {
    await createSystemUser(userForm)
    ElMessage.success('账号已创建，默认密码 123456')
  }
  userDialogVisible.value = false
  await loadAll()
}

async function toggleStatus(row) {
  await updateSystemUserStatus(row.id, !row.enabled)
  ElMessage.success('账号状态已更新')
  await loadAll()
}

async function removeUser(row) {
  await ElMessageBox.confirm(`确定删除账号 ${row.username} 吗？`, '删除账号确认', { type: 'warning' })
  await deleteSystemUser(row.id)
  ElMessage.success('账号已删除')
  await loadAll()
}

function openRoleCreate() {
  Object.assign(roleForm, defaultRole())
  roleDialogVisible.value = true
}

function openRoleEdit(row) {
  Object.assign(roleForm, { ...row })
  roleDialogVisible.value = true
}

async function submitRole() {
  if (!roleForm.roleName || !roleForm.roleCode) {
    ElMessage.warning('请补全角色名称和角色编码')
    return
  }
  if (roleForm.id) {
    await updateRole(roleForm.id, roleForm)
    ElMessage.success('角色已更新')
  } else {
    await createRole(roleForm)
    ElMessage.success('角色已创建')
  }
  roleDialogVisible.value = false
  await loadAll()
}

async function removeRole(row) {
  await ElMessageBox.confirm(`确定删除角色 ${row.roleName} 吗？`, '删除角色确认', { type: 'warning' })
  await deleteRole(row.id)
  ElMessage.success('角色已删除')
  await loadAll()
}

function openPermissionCreate() {
  Object.assign(permissionForm, defaultPermission())
  permissionDialogVisible.value = true
}

function openPermissionEdit(row) {
  Object.assign(permissionForm, { ...row })
  permissionDialogVisible.value = true
}

async function submitPermission() {
  if (!permissionForm.permissionName || !permissionForm.permissionCode || !permissionForm.permissionType) {
    ElMessage.warning('请补全权限名称、编码和类型')
    return
  }
  if (permissionForm.id) {
    await updatePermission(permissionForm.id, permissionForm)
    ElMessage.success('权限已更新')
  } else {
    await createPermission(permissionForm)
    ElMessage.success('权限已创建')
  }
  permissionDialogVisible.value = false
  await loadAll()
}

async function removePermission(row) {
  await ElMessageBox.confirm(`确定删除权限 ${row.permissionName} 吗？`, '删除权限确认', { type: 'warning' })
  await deletePermission(row.id)
  ElMessage.success('权限已删除')
  await loadAll()
}

async function openUserRole(row) {
  activeTab.value = 'assign'
  selectedUserId.value = row.id
  await loadUserRoles()
}

async function openRolePermission(row) {
  activeTab.value = 'assign'
  selectedRoleId.value = row.id
  await loadRolePermissions()
}

async function loadUserRoles() {
  if (!selectedUserId.value) return
  const response = await fetchUserRoles(selectedUserId.value)
  userRoleCodes.value = response.data
}

async function saveUserRoles() {
  await assignUserRoles(selectedUserId.value, userRoleCodes.value)
  ElMessage.success('用户角色已保存')
  await loadAll()
}

async function loadRolePermissions() {
  if (!selectedRoleId.value) return
  const response = await fetchRolePermissions(selectedRoleId.value)
  rolePermissionCodes.value = response.data
}

async function saveRolePermissions() {
  await assignRolePermissions(selectedRoleId.value, rolePermissionCodes.value)
  ElMessage.success('角色权限已保存')
  await loadAll()
}

function applyRolePreset(roleCode) {
  const role = roles.value.find((item) => item.roleCode === roleCode)
  if (!role) return
  userForm.permissionScope = role.description
}

function roleName(roleCode) {
  return roles.value.find((item) => item.roleCode === roleCode)?.roleName || roleCode
}

function defaultUser() {
  return {
    id: null,
    username: '',
    realName: '',
    roleCode: 'SERVICE_MANAGER',
    department: '客户服务部',
    permissionScope: '住户、工单、公告等日常服务',
    phone: '',
    enabled: true,
  }
}

function defaultRole() {
  return {
    id: null,
    roleName: '',
    roleCode: '',
    description: '',
    enabled: true,
    sortOrder: 99,
  }
}

function defaultPermission() {
  return {
    id: null,
    permissionName: '',
    permissionCode: '',
    permissionType: 'BUTTON',
    parentId: null,
    routePath: '',
    componentKey: '',
    sortOrder: 99,
    visible: true,
  }
}

onMounted(loadAll)
</script>
