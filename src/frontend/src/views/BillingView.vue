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
          <el-button v-if="canDo('button:payment:create')" @click="openPayment">缴费登记</el-button>
          <el-button v-if="canDo('button:fee-item:manage')" @click="openFeeCreate">新增费用项目</el-button>
          <el-button v-if="canDo('button:bill:create')" type="primary" @click="openBillCreate">新增账单</el-button>
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
          <span class="metric-label">收缴率</span>
          <strong class="metric-value">{{ collectionRate }}%</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>账单查询</strong>
            <span>账单生成、条件查询、缴费确认和逾期标记</span>
          </div>
        </div>
      </template>

      <el-table :data="filteredBills" v-loading="loading">
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
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canDo('button:payment:create')" link type="success" :disabled="row.status === '已缴费'" @click="openPayment(row)">登记</el-button>
            <el-button v-if="canDo('button:bill:update')" link type="danger" :disabled="row.status === '已缴费'" @click="markOverdue(row)">逾期</el-button>
            <el-button v-if="canDo('button:bill:delete')" link type="danger" @click="removeBill(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="12">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <div class="panel-header">
              <div>
                <strong>费用项目管理</strong>
                <span>维护物业费、停车费、公共服务费等收费标准</span>
              </div>
            </div>
          </template>
          <el-table :data="billing.feeItems">
            <el-table-column prop="name" label="项目" min-width="140" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="billingCycle" label="周期" width="90" />
            <el-table-column label="单价" width="110">
              <template #default="{ row }">{{ toCurrency(row.unitPrice) }}</template>
            </el-table-column>
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'info'" effect="plain">{{ row.enabled ? '启用' : '停用' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="{ row }">
                <el-button v-if="canDo('button:fee-item:manage')" link type="primary" @click="openFeeEdit(row)">编辑</el-button>
                <el-button v-if="canDo('button:fee-item:manage')" link type="danger" @click="removeFeeItem(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <div class="panel-header">
              <div>
                <strong>缴费记录</strong>
                <span>记录线上支付、柜台收款和操作人</span>
              </div>
            </div>
          </template>
          <el-table :data="billing.payments">
            <el-table-column prop="payerName" label="缴费人" width="110" />
            <el-table-column prop="house" label="房号" width="110" />
            <el-table-column label="金额" width="110">
              <template #default="{ row }">{{ toCurrency(row.amount) }}</template>
            </el-table-column>
            <el-table-column prop="paymentMethod" label="方式" width="110" />
            <el-table-column prop="paidAt" label="缴费时间" min-width="170" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="billVisible" title="新增收费账单" width="560px">
      <el-form :model="billForm" label-position="top">
        <el-form-item label="住户"><el-input v-model="billForm.residentName" /></el-form-item>
        <el-form-item label="房号"><el-input v-model="billForm.house" /></el-form-item>
        <el-form-item label="费用类型"><el-input v-model="billForm.feeType" /></el-form-item>
        <el-form-item label="账期"><el-input v-model="billForm.billPeriod" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="billForm.amount" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="billForm.status" style="width: 100%">
            <el-option label="待缴费" value="待缴费" />
            <el-option label="已缴费" value="已缴费" />
            <el-option label="逾期" value="逾期" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期"><el-date-picker v-model="billForm.dueDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="billVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBill">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="feeVisible" :title="feeForm.id ? '编辑费用项目' : '新增费用项目'" width="560px">
      <el-form :model="feeForm" label-position="top">
        <el-form-item label="项目名称"><el-input v-model="feeForm.name" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="feeForm.category" /></el-form-item>
        <el-form-item label="计费周期"><el-input v-model="feeForm.billingCycle" /></el-form-item>
        <el-form-item label="单价"><el-input-number v-model="feeForm.unitPrice" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="feeForm.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="feeForm.enabled" active-text="启用" inactive-text="停用" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="feeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFeeItem">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="paymentVisible" title="缴费登记" width="560px">
      <el-form :model="paymentForm" label-position="top">
        <el-form-item label="关联账单"><el-input v-model="paymentForm.billId" disabled /></el-form-item>
        <el-form-item label="缴费人"><el-input v-model="paymentForm.payerName" /></el-form-item>
        <el-form-item label="房号"><el-input v-model="paymentForm.house" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="paymentForm.amount" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="paymentForm.paymentMethod" style="width: 100%">
            <el-option label="微信支付" value="微信支付" />
            <el-option label="支付宝" value="支付宝" />
            <el-option label="银行卡" value="银行卡" />
            <el-option label="现金" value="现金" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model="paymentForm.remark" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="paymentVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPayment">登记</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  createBill,
  createFeeItem,
  createPayment,
  deleteBill,
  deleteFeeItem,
  fetchBilling,
  updateBillStatus,
  updateFeeItem,
} from '../api/property'
import { billsFallback } from '../data/mock'
import { toCurrency } from '../utils/format'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const billing = ref({ ...billsFallback, feeItems: [], payments: [] })
const keyword = ref('')
const statusFilter = ref('')
const periodFilter = ref('')
const loading = ref(false)
const billVisible = ref(false)
const feeVisible = ref(false)
const paymentVisible = ref(false)
const currentUser = computed(() => getUser())
const billForm = reactive(defaultBill())
const feeForm = reactive(defaultFeeItem())
const paymentForm = reactive(defaultPayment())

const filteredBills = computed(() => billing.value.bills.filter((bill) => {
  const text = `${bill.residentName}${bill.house}`.toLowerCase()
  const keywordMatched = !keyword.value || text.includes(keyword.value.toLowerCase())
  const statusMatched = !statusFilter.value || bill.status === statusFilter.value
  const periodMatched = !periodFilter.value || bill.billPeriod.includes(periodFilter.value)
  return keywordMatched && statusMatched && periodMatched
}))

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
  loading.value = true
  try {
    const response = await fetchBilling()
    billing.value = {
      summary: response.data.summary || {},
      bills: response.data.bills || [],
      feeItems: response.data.feeItems || [],
      payments: response.data.payments || [],
    }
  } catch (error) {
    ElMessage.warning('收费数据暂时无法加载，已显示默认状态')
    billing.value = { ...billsFallback, feeItems: [], payments: [] }
  } finally {
    loading.value = false
  }
}

function openBillCreate() {
  Object.assign(billForm, defaultBill())
  billVisible.value = true
}

async function submitBill() {
  if (!billForm.residentName || !billForm.house || !billForm.amount) {
    ElMessage.warning('请补全住户、房号和金额')
    return
  }
  await createBill(billForm)
  ElMessage.success('账单已创建')
  billVisible.value = false
  await loadBilling()
}

async function markOverdue(row) {
  await updateBillStatus(row.id, '逾期')
  ElMessage.success('已标记为逾期')
  await loadBilling()
}

async function removeBill(row) {
  await ElMessageBox.confirm(`确定删除 ${row.residentName} 的 ${row.billPeriod} 账单吗？`, '删除账单确认', { type: 'warning' })
  await deleteBill(row.id)
  ElMessage.success('账单已删除')
  await loadBilling()
}

function openFeeCreate() {
  Object.assign(feeForm, defaultFeeItem())
  feeVisible.value = true
}

function openFeeEdit(row) {
  Object.assign(feeForm, { ...row })
  feeVisible.value = true
}

async function submitFeeItem() {
  if (!feeForm.name || !feeForm.category) {
    ElMessage.warning('请补全项目名称和分类')
    return
  }
  if (feeForm.id) {
    await updateFeeItem(feeForm.id, feeForm)
    ElMessage.success('费用项目已更新')
  } else {
    await createFeeItem(feeForm)
    ElMessage.success('费用项目已创建')
  }
  feeVisible.value = false
  await loadBilling()
}

async function removeFeeItem(row) {
  await ElMessageBox.confirm(`确定删除费用项目 ${row.name} 吗？`, '删除费用项目确认', { type: 'warning' })
  await deleteFeeItem(row.id)
  ElMessage.success('费用项目已删除')
  await loadBilling()
}

function openPayment(row = null) {
  Object.assign(paymentForm, defaultPayment())
  if (row) {
    Object.assign(paymentForm, {
      billId: row.id,
      payerName: row.residentName,
      house: row.house,
      amount: row.amount,
      remark: `${row.billPeriod} ${row.feeType}`,
    })
  }
  paymentVisible.value = true
}

async function submitPayment() {
  if (!paymentForm.payerName || !paymentForm.house || !paymentForm.amount) {
    ElMessage.warning('请补全缴费人、房号和金额')
    return
  }
  await createPayment(paymentForm)
  ElMessage.success('缴费登记成功')
  paymentVisible.value = false
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

function defaultFeeItem() {
  return {
    id: null,
    name: '',
    category: '物业费',
    billingCycle: '月度',
    unitPrice: 0,
    description: '',
    enabled: true,
  }
}

function defaultPayment() {
  return {
    billId: null,
    payerName: '',
    house: '',
    amount: 0,
    paymentMethod: '微信支付',
    operator: currentUser.value?.realName || '财务专员',
    remark: '',
  }
}

onMounted(loadBilling)
</script>
