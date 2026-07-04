<template>
  <section class="page-grid">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left billing-filters">
          <el-input v-model="keyword" placeholder="搜索住户或房号" clearable />
          <el-select v-model="statusFilter" placeholder="账单状态" clearable>
            <el-option label="全部状态" value="" />
            <el-option label="待缴费" value="待缴费" />
            <el-option label="已缴费" value="已缴费" />
            <el-option label="逾期" value="逾期" />
          </el-select>
          <el-input v-model="periodFilter" placeholder="账期，如 2026-07" clearable />
        </div>
        <div class="toolbar-right">
          <el-button v-if="canDo('button:bill:create')" type="primary" @click="openCreate">新增账单</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="12">
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">本月应收</span>
          <strong class="metric-value">{{ toCurrency(billing.summary.receivable || 0) }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">本月实收</span>
          <strong class="metric-value">{{ toCurrency(billing.summary.paid || 0) }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">欠费笔数</span>
          <strong class="metric-value">{{ billing.summary.unpaidCount || 0 }}</strong>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">逾期笔数</span>
          <strong class="metric-value">{{ billing.summary.overdueCount || 0 }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>账单列表</strong>
            <span>支持新增账单、缴费确认、欠费状态和按条件筛选</span>
          </div>
          <el-tag effect="plain" type="primary">收缴率 {{ collectionRate }}%</el-tag>
        </div>
      </template>

      <el-table :data="filteredBills" v-if="filteredBills.length">
        <el-table-column prop="residentName" label="住户" width="120" fixed="left" />
        <el-table-column prop="house" label="房号" width="120" />
        <el-table-column prop="feeType" label="费用类型" width="130" />
        <el-table-column prop="billPeriod" label="账期" width="110" />
        <el-table-column label="金额" width="130">
          <template #default="{ row }">{{ toCurrency(row.amount) }}</template>
        </el-table-column>
        <el-table-column label="账单状态" width="120">
          <template #default="{ row }">
            <el-tag :type="billStatusType(row.status)" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="截止日期" min-width="120" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canDo('button:bill:update')" link type="success" :disabled="row.status === '已缴费'" @click="markPaid(row)">缴费确认</el-button>
            <el-button v-if="canDo('button:bill:update')" link type="danger" :disabled="row.status === '已缴费'" @click="markOverdue(row)">标记逾期</el-button>
            <el-button v-if="canDo('button:bill:delete')" link type="danger" @click="removeBill(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无账单数据" />
    </el-card>

    <el-dialog v-model="formVisible" title="新增收费账单" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="住户">
          <el-input v-model="form.residentName" placeholder="请输入住户姓名" />
        </el-form-item>
        <el-form-item label="房号">
          <el-input v-model="form.house" placeholder="例如 A1-1203" />
        </el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="form.feeType" style="width: 100%">
            <el-option label="物业费" value="物业费" />
            <el-option label="水电费" value="水电费" />
            <el-option label="停车月租" value="停车月租" />
          </el-select>
        </el-form-item>
        <el-form-item label="账期">
          <el-input v-model="form.billPeriod" placeholder="例如 2026-07" />
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待缴费" value="待缴费" />
            <el-option label="已缴费" value="已缴费" />
            <el-option label="逾期" value="逾期" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBill">创建</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createBill, deleteBill, fetchBilling, updateBillStatus } from '../api/property'
import { billsFallback } from '../data/mock'
import { toCurrency } from '../utils/format'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const billing = ref(billsFallback)
const keyword = ref('')
const statusFilter = ref('')
const periodFilter = ref('')
const formVisible = ref(false)
const form = reactive(defaultBill())
const currentUser = computed(() => getUser())

const filteredBills = computed(() => {
  return billing.value.bills.filter((bill) => {
    const text = `${bill.residentName}${bill.house}`.toLowerCase()
    const keywordMatched = !keyword.value || text.includes(keyword.value.toLowerCase())
    const statusMatched = !statusFilter.value || bill.status === statusFilter.value
    const periodMatched = !periodFilter.value || bill.billPeriod.includes(periodFilter.value)
    return keywordMatched && statusMatched && periodMatched
  })
})

const collectionRate = computed(() => {
  const receivable = Number(billing.value.summary.receivable || 0)
  const paid = Number(billing.value.summary.paid || 0)
  if (!receivable) return '0.0'
  return ((paid / receivable) * 100).toFixed(1)
})

function canDo(permissionCode) {
  return hasPermission(currentUser.value, permissionCode)
}

async function loadBilling() {
  try {
    const response = await fetchBilling()
    billing.value = response.data
  } catch (error) {
    ElMessage.warning('收费数据暂时无法加载，已显示默认状态')
    billing.value = billsFallback
  }
}

function openCreate() {
  Object.assign(form, defaultBill())
  formVisible.value = true
}

async function submitBill() {
  if (!form.residentName || !form.house || !form.amount) {
    ElMessage.warning('请补全住户、房号和金额')
    return
  }
  await createBill(form)
  ElMessage.success('账单已创建')
  formVisible.value = false
  await loadBilling()
}

async function markPaid(row) {
  await updateBillStatus(row.id, '已缴费')
  ElMessage.success('已确认缴费')
  await loadBilling()
}

async function markOverdue(row) {
  await updateBillStatus(row.id, '逾期')
  ElMessage.success('已标记为逾期')
  await loadBilling()
}

async function removeBill(row) {
  await ElMessageBox.confirm(`确定要删除 ${row.residentName} 的 ${row.billPeriod} 账单吗？`, '删除账单确认', {
    type: 'warning',
  })
  await deleteBill(row.id)
  ElMessage.success('账单已删除')
  await loadBilling()
}

function billStatusType(status) {
  if (status === '已缴费') return 'success'
  if (status === '逾期') return 'danger'
  return 'warning'
}

function defaultBill() {
  return {
    residentName: '',
    house: '',
    feeType: '物业费',
    billPeriod: '2026-07',
    amount: 0,
    status: '待缴费',
    dueDate: '2026-07-15',
  }
}

onMounted(loadBilling)
</script>
