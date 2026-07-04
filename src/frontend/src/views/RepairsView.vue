<template>
  <section class="page-grid">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-segmented v-model="statusFilter" :options="statusOptions" />
        </div>
        <div class="toolbar-right">
          <el-button @click="exportRepairs">导出工单</el-button>
          <el-button v-if="canDo('button:repair:create')" type="primary" @click="openCreate">新建工单</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="12">
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">工单总数</span>
          <strong class="metric-value">{{ repairs.length }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">高优先级</span>
          <strong class="metric-value">{{ highPriorityCount }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">处理中</span>
          <strong class="metric-value">{{ processingCount }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">平均耗时</span>
          <strong class="metric-value">{{ averageHours }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>报修工单中心</strong>
            <span>支持新增、派单、处理中、完成、详情查看和状态筛选</span>
          </div>
        </div>
      </template>

      <el-table :data="filteredRepairs" v-if="filteredRepairs.length">
        <el-table-column prop="code" label="工单编号" width="160" fixed="left" />
        <el-table-column prop="title" label="问题描述" min-width="220" />
        <el-table-column prop="area" label="发生位置" width="150" />
        <el-table-column label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="priorityType(row.priority)" effect="plain">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignee" label="处理人" width="150" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="耗时" width="90">
          <template #default="{ row }">{{ row.durationHours }} 小时</template>
        </el-table-column>
        <el-table-column label="操作" width="290" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-if="canDo('button:repair:update')" link type="primary" @click="openProgress(row, '待处理')">派单</el-button>
            <el-button v-if="canDo('button:repair:update')" link type="warning" @click="quickProgress(row, '处理中')">处理</el-button>
            <el-button v-if="canDo('button:repair:update')" link type="success" @click="quickProgress(row, '已完成')">完成</el-button>
            <el-button v-if="canDo('button:repair:delete')" link type="danger" @click="removeRepair(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无工单数据" />
    </el-card>

    <el-dialog v-model="formVisible" title="新建报修工单" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="问题描述">
          <el-input v-model="form.title" placeholder="请输入报修问题" />
        </el-form-item>
        <el-form-item label="发生位置">
          <el-input v-model="form.area" placeholder="例如 A1-1203 / 地下车库" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="form.priority" style="width: 100%">
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="form.assignee" placeholder="请输入处理班组或人员" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待处理" value="待处理" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRepair">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="progressVisible" title="工单流转" width="520px">
      <el-form :model="progressForm" label-position="top">
        <el-form-item label="状态">
          <el-select v-model="progressForm.status" style="width: 100%">
            <el-option label="待处理" value="待处理" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="progressForm.assignee" />
        </el-form-item>
        <el-form-item label="累计耗时">
          <el-input-number v-model="progressForm.durationHours" :min="0" :precision="1" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="progressVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProgress">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="工单详情" width="520px">
      <el-descriptions :column="1" border v-if="currentRepair">
        <el-descriptions-item label="工单编号">{{ currentRepair.code }}</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ currentRepair.title }}</el-descriptions-item>
        <el-descriptions-item label="发生位置">{{ currentRepair.area }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ currentRepair.priority }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentRepair.assignee }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">{{ currentRepair.status }}</el-descriptions-item>
        <el-descriptions-item label="累计耗时">{{ currentRepair.durationHours }} 小时</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createRepair, deleteRepair, fetchRepairs, updateRepairProgress } from '../api/property'
import { repairsFallback } from '../data/mock'
import { exportCsv } from '../utils/exportCsv'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const statusOptions = ['全部工单', '待处理', '处理中', '已完成']
const repairs = ref(repairsFallback)
const statusFilter = ref('全部工单')
const formVisible = ref(false)
const progressVisible = ref(false)
const detailVisible = ref(false)
const form = reactive(defaultRepair())
const progressForm = reactive(defaultProgress())
const currentRepair = ref(null)
const currentUser = computed(() => getUser())

const filteredRepairs = computed(() => {
  if (statusFilter.value === '全部工单') return repairs.value
  return repairs.value.filter((item) => item.status === statusFilter.value)
})

const highPriorityCount = computed(() => repairs.value.filter((item) => item.priority === '高').length)
const processingCount = computed(() => repairs.value.filter((item) => item.status === '处理中').length)
const averageHours = computed(() => {
  if (!repairs.value.length) return '0h'
  const total = repairs.value.reduce((sum, item) => sum + Number(item.durationHours || 0), 0)
  return `${(total / repairs.value.length).toFixed(1)}h`
})

function canDo(permissionCode) {
  return hasPermission(currentUser.value, permissionCode)
}

async function loadRepairs() {
  try {
    const response = await fetchRepairs()
    repairs.value = response.data
  } catch (error) {
    ElMessage.warning('工单数据暂时无法加载，已显示默认状态')
    repairs.value = repairsFallback
  }
}

function openCreate() {
  Object.assign(form, defaultRepair())
  formVisible.value = true
}

async function submitRepair() {
  if (!form.title || !form.area || !form.assignee) {
    ElMessage.warning('请补全问题描述、位置和处理人')
    return
  }
  await createRepair(form)
  ElMessage.success('工单已创建')
  formVisible.value = false
  await loadRepairs()
}

function openProgress(row, nextStatus = row.status) {
  currentRepair.value = row
  Object.assign(progressForm, {
    status: nextStatus,
    assignee: row.assignee || '工程班组 1',
    durationHours: Number(row.durationHours || 0),
  })
  progressVisible.value = true
}

async function quickProgress(row, status) {
  await updateRepairProgress(row.id, {
    status,
    assignee: row.assignee || '工程班组 1',
    durationHours: status === '已完成' ? Math.max(Number(row.durationHours || 0), 1) : Number(row.durationHours || 0),
  })
  ElMessage.success('工单状态已更新')
  await loadRepairs()
}

async function submitProgress() {
  await updateRepairProgress(currentRepair.value.id, progressForm)
  ElMessage.success('工单进度已保存')
  progressVisible.value = false
  await loadRepairs()
}

async function removeRepair(row) {
  await ElMessageBox.confirm(`确定要删除工单 ${row.code} 吗？`, '删除工单确认', {
    type: 'warning',
  })
  await deleteRepair(row.id)
  ElMessage.success('工单已删除')
  await loadRepairs()
}

function openDetail(row) {
  currentRepair.value = row
  detailVisible.value = true
}

function exportRepairs() {
  exportCsv('报修工单.csv', [
    { label: '工单编号', prop: 'code' },
    { label: '问题描述', prop: 'title' },
    { label: '发生位置', prop: 'area' },
    { label: '优先级', prop: 'priority' },
    { label: '处理人', prop: 'assignee' },
    { label: '状态', prop: 'status' },
    { label: '耗时', prop: 'durationHours' },
  ], filteredRepairs.value)
  ElMessage.success('工单数据已导出')
}

function priorityType(priority) {
  return priority === '高' ? 'danger' : priority === '中' ? 'warning' : 'info'
}

function statusType(status) {
  return status === '已完成' ? 'success' : status === '处理中' ? 'warning' : 'info'
}

function defaultRepair() {
  return {
    title: '',
    area: '',
    priority: '中',
    assignee: '工程班组 1',
    status: '待处理',
    durationHours: 0,
  }
}

function defaultProgress() {
  return {
    status: '待处理',
    assignee: '',
    durationHours: 0,
  }
}

onMounted(loadRepairs)
</script>
