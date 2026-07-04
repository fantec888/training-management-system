<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">管理小区</span>
          <strong class="metric-value">{{ communities.length }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">楼栋总数</span>
          <strong class="metric-value">{{ totalBuildings }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">房屋总数</span>
          <strong class="metric-value">{{ totalRooms }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">正常运营</span>
          <strong class="metric-value">{{ normalCount }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-input v-model="keyword" placeholder="搜索小区名称 / 地址 / 负责人" clearable />
        </div>
        <div class="toolbar-right">
          <el-button v-if="canDo('button:community:manage')" type="primary" @click="openCreate">新增小区</el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>小区基础数据</strong>
            <span>维护小区、物业公司、负责人、楼栋与房屋规模</span>
          </div>
        </div>
      </template>

      <el-table :data="filteredCommunities" v-loading="loading">
        <el-table-column prop="name" label="小区名称" width="140" fixed="left" />
        <el-table-column prop="address" label="地址" min-width="220" />
        <el-table-column prop="propertyCompany" label="物业公司" width="180" />
        <el-table-column prop="developer" label="开发商" width="130" />
        <el-table-column prop="totalBuildings" label="楼栋" width="80" />
        <el-table-column prop="totalRooms" label="房屋" width="90" />
        <el-table-column prop="manager" label="负责人" width="110" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '正常' ? 'success' : 'warning'" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canDo('button:community:manage')" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="canDo('button:community:manage')" link type="danger" @click="removeCommunity(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="formVisible" :title="form.id ? '编辑小区' : '新增小区'" width="620px">
      <el-form :model="form" label-position="top">
        <el-form-item label="小区名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="开发商"><el-input v-model="form.developer" /></el-form-item>
        <el-form-item label="物业公司"><el-input v-model="form.propertyCompany" /></el-form-item>
        <div class="inline-fields">
          <el-form-item label="楼栋数"><el-input-number v-model="form.totalBuildings" :min="0" style="width: 100%" /></el-form-item>
          <el-form-item label="房屋数"><el-input-number v-model="form.totalRooms" :min="0" style="width: 100%" /></el-form-item>
        </div>
        <el-form-item label="负责人"><el-input v-model="form.manager" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="正常" value="正常" />
            <el-option label="筹备中" value="筹备中" />
            <el-option label="停用" value="停用" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCommunity">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createCommunity, deleteCommunity, fetchCommunities, updateCommunity } from '../api/property'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const communities = ref([])
const keyword = ref('')
const loading = ref(false)
const formVisible = ref(false)
const form = reactive(defaultCommunity())
const currentUser = computed(() => getUser())

const filteredCommunities = computed(() => communities.value.filter((item) => {
  const text = `${item.name}${item.address}${item.manager}${item.propertyCompany}`
  return !keyword.value || text.includes(keyword.value)
}))
const totalBuildings = computed(() => communities.value.reduce((sum, item) => sum + Number(item.totalBuildings || 0), 0))
const totalRooms = computed(() => communities.value.reduce((sum, item) => sum + Number(item.totalRooms || 0), 0))
const normalCount = computed(() => communities.value.filter((item) => item.status === '正常').length)

function canDo(code) {
  return hasPermission(currentUser.value, code)
}

async function loadCommunities() {
  loading.value = true
  try {
    const response = await fetchCommunities()
    communities.value = response.data
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, defaultCommunity())
  formVisible.value = true
}

function openEdit(row) {
  Object.assign(form, { ...row })
  formVisible.value = true
}

async function submitCommunity() {
  if (!form.name || !form.address || !form.propertyCompany) {
    ElMessage.warning('请补全小区名称、地址和物业公司')
    return
  }
  if (form.id) {
    await updateCommunity(form.id, form)
    ElMessage.success('小区信息已更新')
  } else {
    await createCommunity(form)
    ElMessage.success('小区已创建')
  }
  formVisible.value = false
  await loadCommunities()
}

async function removeCommunity(row) {
  await ElMessageBox.confirm(`确定删除小区 ${row.name} 吗？`, '删除小区确认', { type: 'warning' })
  await deleteCommunity(row.id)
  ElMessage.success('小区已删除')
  await loadCommunities()
}

function defaultCommunity() {
  return {
    id: null,
    name: '',
    address: '',
    developer: '',
    propertyCompany: '云栖物业服务有限公司',
    totalBuildings: 0,
    totalRooms: 0,
    manager: '',
    status: '正常',
  }
}

onMounted(loadCommunities)
</script>
