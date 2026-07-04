<template>
  <section class="page-grid">
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-input v-model="keyword" placeholder="搜索投诉人 / 标题 / 分类" clearable />
          <el-select v-model="statusFilter" placeholder="处理状态" clearable style="width: 140px">
            <el-option label="全部" value="" />
            <el-option label="待处理" value="待处理" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已回复" value="已回复" />
          </el-select>
        </div>
        <div class="toolbar-right">
          <el-button type="primary" @click="openCreate">提交投诉建议</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="12">
      <el-col :xs="12" :lg="6" v-for="item in stats" :key="item.label">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.label }}</span>
          <strong class="metric-value">{{ item.value }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>投诉建议处理</strong>
            <span>提交、筛选、回复和闭环跟踪住户反馈</span>
          </div>
        </div>
      </template>

      <el-table :data="pagedComplaints" v-loading="loading">
        <el-table-column prop="code" label="编号" width="150" fixed="left" />
        <el-table-column prop="residentName" label="住户" width="110" />
        <el-table-column prop="category" label="分类" width="110" />
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="handler" label="处理人" width="110" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submittedAt" label="提交时间" width="180" />
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-if="canDo('button:complaint:handle')" link type="primary" @click="openReply(row)">回复</el-button>
            <el-button v-if="canDo('button:complaint:handle')" link type="danger" @click="removeComplaint(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-pagination" v-if="filteredComplaints.length">
        <el-pagination
          v-model:current-page="complaintPage"
          v-model:page-size="complaintPageSize"
          :page-sizes="complaintPageSizes"
          :total="complaintTotal"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>

    <el-dialog v-model="formVisible" title="提交投诉建议" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="住户姓名"><el-input v-model="form.residentName" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="环境卫生" value="环境卫生" />
            <el-option label="噪音投诉" value="噪音投诉" />
            <el-option label="服务建议" value="服务建议" />
            <el-option label="设施维护" value="设施维护" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplaint">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="replyVisible" title="投诉处理回复" width="560px">
      <el-form :model="replyForm" label-position="top">
        <el-form-item label="处理人"><el-input v-model="replyForm.handler" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="replyForm.status" style="width: 100%">
            <el-option label="处理中" value="处理中" />
            <el-option label="已回复" value="已回复" />
          </el-select>
        </el-form-item>
        <el-form-item label="回复内容"><el-input v-model="replyForm.reply" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">保存回复</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="投诉建议详情" width="620px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="编号">{{ selected.code }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ selected.title }}</el-descriptions-item>
        <el-descriptions-item label="内容">{{ selected.content }}</el-descriptions-item>
        <el-descriptions-item label="回复">{{ selected.reply || '暂无回复' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createComplaint, deleteComplaint, fetchComplaints, replyComplaint } from '../api/property'
import { getUser } from '../utils/auth'
import { useClientPagination } from '../utils/pagination'
import { hasPermission } from '../utils/roles'

const complaints = ref([])
const keyword = ref('')
const statusFilter = ref('')
const loading = ref(false)
const formVisible = ref(false)
const replyVisible = ref(false)
const detailVisible = ref(false)
const selected = ref({})
const form = reactive(defaultComplaint())
const replyForm = reactive(defaultReply())
const currentUser = computed(() => getUser())

const filteredComplaints = computed(() => complaints.value.filter((item) => {
  const text = `${item.residentName}${item.title}${item.category}${item.code}`
  const keywordMatched = !keyword.value || text.includes(keyword.value)
  const statusMatched = !statusFilter.value || item.status === statusFilter.value
  return keywordMatched && statusMatched
}))
const {
  page: complaintPage,
  pageSize: complaintPageSize,
  pageSizes: complaintPageSizes,
  total: complaintTotal,
  records: pagedComplaints,
} = useClientPagination(filteredComplaints)
const stats = computed(() => [
  { label: '全部反馈', value: complaints.value.length },
  { label: '待处理', value: complaints.value.filter((item) => item.status === '待处理').length },
  { label: '处理中', value: complaints.value.filter((item) => item.status === '处理中').length },
  { label: '已回复', value: complaints.value.filter((item) => item.status === '已回复').length },
])

function canDo(code) {
  return hasPermission(currentUser.value, code)
}

async function loadComplaints() {
  loading.value = true
  try {
    const response = await fetchComplaints()
    complaints.value = response.data
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, defaultComplaint())
  formVisible.value = true
}

async function submitComplaint() {
  if (!form.residentName || !form.phone || !form.title || !form.content) {
    ElMessage.warning('请补全住户、电话、标题和内容')
    return
  }
  await createComplaint(form)
  ElMessage.success('投诉建议已提交')
  formVisible.value = false
  await loadComplaints()
}

function openReply(row) {
  selected.value = row
  Object.assign(replyForm, {
    handler: row.handler || currentUser.value?.realName || '客服主管',
    status: row.status === '待处理' ? '处理中' : row.status,
    reply: row.reply || '',
  })
  replyVisible.value = true
}

async function submitReply() {
  await replyComplaint(selected.value.id, replyForm)
  ElMessage.success('处理回复已保存')
  replyVisible.value = false
  await loadComplaints()
}

function openDetail(row) {
  selected.value = row
  detailVisible.value = true
}

async function removeComplaint(row) {
  await ElMessageBox.confirm(`确定删除投诉建议 ${row.code} 吗？`, '删除确认', { type: 'warning' })
  await deleteComplaint(row.id)
  ElMessage.success('投诉建议已删除')
  await loadComplaints()
}

function statusType(status) {
  if (status === '已回复') return 'success'
  if (status === '处理中') return 'warning'
  return 'info'
}

function defaultComplaint() {
  return {
    residentName: '',
    phone: '',
    category: '服务建议',
    title: '',
    content: '',
    status: '待处理',
  }
}

function defaultReply() {
  return {
    handler: '',
    status: '处理中',
    reply: '',
  }
}

onMounted(loadComplaints)
</script>
