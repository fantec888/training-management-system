<template>
  <section class="page-grid">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-input v-model="keyword" placeholder="搜索姓名 / 手机号 / 房号" clearable />
          <el-select v-model="identityFilter" placeholder="住户类型" style="width: 140px">
            <el-option label="全部" value="all" />
            <el-option label="业主" value="业主" />
            <el-option label="租户" value="租户" />
          </el-select>
          <el-select v-model="verifyFilter" placeholder="认证状态" style="width: 140px">
            <el-option label="全部" value="all" />
            <el-option label="已认证" value="已认证" />
            <el-option label="待审核" value="待审核" />
          </el-select>
        </div>
        <div class="toolbar-right">
          <el-button @click="exportResidents">导出档案</el-button>
          <el-button type="primary" @click="openCreate">新增住户</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="12">
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">当前列表</span>
          <strong class="metric-value">{{ filteredResidents.length }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">已认证</span>
          <strong class="metric-value">{{ verifiedCount }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">车辆登记</span>
          <strong class="metric-value">{{ vehicleCount }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">停用档案</span>
          <strong class="metric-value">{{ disabledCount }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>住户档案</strong>
            <span>维护业主、租户、认证状态和车辆登记</span>
          </div>
        </div>
      </template>

      <el-table :data="filteredResidents" v-if="filteredResidents.length">
        <el-table-column prop="name" label="姓名" width="120" fixed="left" />
        <el-table-column prop="identityType" label="身份" width="90" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column label="房屋" width="120">
          <template #default="{ row }">{{ row.building }}-{{ row.roomNo }}</template>
        </el-table-column>
        <el-table-column prop="vehicles" label="车辆" width="80" />
        <el-table-column label="认证" width="105">
          <template #default="{ row }">
            <el-tag :type="row.verifiedStatus === '已认证' ? 'success' : 'warning'" effect="plain">
              {{ row.verifiedStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="95">
          <template #default="{ row }">
            <el-tag :type="row.status === '正常' ? 'success' : 'danger'" effect="plain">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标签" min-width="140">
          <template #default="{ row }">
            <el-tag effect="plain">{{ row.tag }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">查看</el-button>
            <el-button link @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="toggleResident(row)">
              {{ row.status === '停用' ? '启用' : '停用' }}
            </el-button>
            <el-button link type="danger" @click="removeResident(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无住户数据" />
    </el-card>

    <el-dialog v-model="formVisible" :title="formMode === 'create' ? '新增住户' : '编辑住户'" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="姓名">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="身份">
          <el-select v-model="form.identityType" style="width: 100%">
            <el-option label="业主" value="业主" />
            <el-option label="租户" value="租户" />
          </el-select>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="楼栋房号">
          <div class="inline-fields">
            <el-input v-model="form.building" placeholder="楼栋" />
            <el-input v-model="form.roomNo" placeholder="房号" />
          </div>
        </el-form-item>
        <el-form-item label="车辆数">
          <el-input-number v-model="form.vehicles" :min="0" :max="9" />
        </el-form-item>
        <el-form-item label="认证状态">
          <el-select v-model="form.verifiedStatus" style="width: 100%">
            <el-option label="已认证" value="已认证" />
            <el-option label="待审核" value="待审核" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="form.tag" placeholder="例如 VIP 住户 / 新入住" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResident">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="住户详情" width="520px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ selectedResident.name }}</el-descriptions-item>
        <el-descriptions-item label="身份">{{ selectedResident.identityType }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ selectedResident.phone }}</el-descriptions-item>
        <el-descriptions-item label="房号">{{ selectedResident.building }}-{{ selectedResident.roomNo }}</el-descriptions-item>
        <el-descriptions-item label="车辆">{{ selectedResident.vehicles }}</el-descriptions-item>
        <el-descriptions-item label="认证">{{ selectedResident.verifiedStatus }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedResident.status }}</el-descriptions-item>
        <el-descriptions-item label="标签">{{ selectedResident.tag }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createResident,
  deleteResident,
  fetchResidents,
  updateResident,
  updateResidentStatus,
} from '../api/property'
import { residentsFallback } from '../data/mock'
import { exportCsv } from '../utils/exportCsv'

const residents = ref(residentsFallback)
const keyword = ref('')
const identityFilter = ref('all')
const verifyFilter = ref('all')
const formVisible = ref(false)
const detailVisible = ref(false)
const formMode = ref('create')
const selectedResident = ref({})

const form = reactive(defaultResident())

const filteredResidents = computed(() => residents.value.filter((item) => {
  const text = `${item.name}${item.phone}${item.building}${item.roomNo}`
  const matchKeyword = !keyword.value || text.includes(keyword.value)
  const matchIdentity = identityFilter.value === 'all' || item.identityType === identityFilter.value
  const matchVerify = verifyFilter.value === 'all' || item.verifiedStatus === verifyFilter.value
  return matchKeyword && matchIdentity && matchVerify
}))

const verifiedCount = computed(() => residents.value.filter((item) => item.verifiedStatus === '已认证').length)
const vehicleCount = computed(() => residents.value.reduce((sum, item) => sum + Number(item.vehicles || 0), 0))
const disabledCount = computed(() => residents.value.filter((item) => item.status === '停用').length)

async function loadResidents() {
  try {
    const response = await fetchResidents()
    residents.value = response.data
  } catch (error) {
    ElMessage.warning('住户数据暂时无法加载，已显示默认状态')
    residents.value = residentsFallback
  }
}

function openCreate() {
  formMode.value = 'create'
  Object.assign(form, defaultResident())
  formVisible.value = true
}

function openEdit(row) {
  formMode.value = 'edit'
  Object.assign(form, row)
  formVisible.value = true
}

function openDetail(row) {
  selectedResident.value = row
  detailVisible.value = true
}

async function submitResident() {
  if (!form.name || !form.phone || !form.building || !form.roomNo) {
    ElMessage.warning('请补全姓名、电话、楼栋和房号')
    return
  }
  if (formMode.value === 'create') {
    await createResident(form)
    ElMessage.success('住户已新增')
  } else {
    await updateResident(form.id, form)
    ElMessage.success('住户信息已更新')
  }
  formVisible.value = false
  await loadResidents()
}

async function toggleResident(row) {
  const nextStatus = row.status === '停用' ? '正常' : '停用'
  await ElMessageBox.confirm(`确定要将 ${row.name} ${nextStatus}吗？`, '住户状态确认', {
    type: 'warning',
  })
  await updateResidentStatus(row.id, nextStatus)
  ElMessage.success('住户状态已更新')
  await loadResidents()
}

async function removeResident(row) {
  await ElMessageBox.confirm(`确定要删除住户 ${row.name} 吗？删除后列表中将不再显示。`, '删除住户确认', {
    type: 'warning',
  })
  await deleteResident(row.id)
  ElMessage.success('住户已删除')
  await loadResidents()
}

function exportResidents() {
  exportCsv('住户档案.csv', [
    { label: '姓名', prop: 'name' },
    { label: '身份', prop: 'identityType' },
    { label: '电话', prop: 'phone' },
    { label: '楼栋', prop: 'building' },
    { label: '房号', prop: 'roomNo' },
    { label: '车辆', prop: 'vehicles' },
    { label: '认证状态', prop: 'verifiedStatus' },
    { label: '状态', prop: 'status' },
    { label: '标签', prop: 'tag' },
  ], filteredResidents.value)
  ElMessage.success('住户档案已导出')
}

function defaultResident() {
  return {
    name: '',
    identityType: '业主',
    phone: '',
    building: '',
    roomNo: '',
    vehicles: 0,
    verifiedStatus: '待审核',
    tag: '新入住',
    status: '正常',
  }
}

onMounted(loadResidents)
</script>
