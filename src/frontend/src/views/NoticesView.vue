<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in notices.summary" :key="item.label" :xs="24" :sm="8">
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
            <strong>公告与活动编排</strong>
            <span>社区通知、活动和安全提醒</span>
          </div>
          <el-button v-if="canDo('button:notice:create')" type="primary" class="notice-publish-button" @click="openCreate">
            发布新公告
          </el-button>
        </div>
      </template>

      <el-table :data="notices.list" v-if="notices.list.length">
        <el-table-column prop="title" label="标题" min-width="260" fixed="left" />
        <el-table-column prop="category" label="类型" width="100" />
        <el-table-column prop="audience" label="触达范围" width="140" />
        <el-table-column prop="publisher" label="发布部门" width="130" />
        <el-table-column prop="publishTime" label="发布时间" width="180" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="noticeStatusType(row.status)" effect="plain">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canDo('button:notice:create')" link type="primary" @click="toggleNotice(row)">
              {{ row.status === '已发布' ? '下架' : '发布' }}
            </el-button>
            <el-button v-if="canDo('button:notice:delete')" link type="danger" @click="removeNotice(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无公告活动数据" />
    </el-card>

    <el-dialog v-model="formVisible" title="发布新公告" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="通知" value="通知" />
            <el-option label="活动" value="活动" />
            <el-option label="安全" value="安全" />
          </el-select>
        </el-form-item>
        <el-form-item label="触达范围">
          <el-input v-model="form.audience" placeholder="例如 全体住户 / 车主用户" />
        </el-form-item>
        <el-form-item label="发布部门">
          <el-input v-model="form.publisher" placeholder="例如 客服中心" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="已发布" value="已发布" />
            <el-option label="报名中" value="报名中" />
            <el-option label="草稿" value="草稿" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitNotice">发布</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createNotice, deleteNotice, fetchNotices, updateNoticeStatus } from '../api/property'
import { noticesFallback } from '../data/mock'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const notices = ref(noticesFallback)
const formVisible = ref(false)
const form = reactive(defaultNotice())
const currentUser = computed(() => getUser())

function canDo(permissionCode) {
  return hasPermission(currentUser.value, permissionCode)
}

async function loadNotices() {
  try {
    const response = await fetchNotices()
    notices.value = response.data
  } catch (error) {
    ElMessage.warning('公告活动数据暂时无法加载，已显示默认状态')
    notices.value = noticesFallback
  }
}

function openCreate() {
  Object.assign(form, defaultNotice())
  formVisible.value = true
}

async function submitNotice() {
  if (!form.title || !form.audience || !form.publisher) {
    ElMessage.warning('请补全标题、触达范围和发布部门')
    return
  }
  await createNotice(form)
  ElMessage.success('公告已发布')
  formVisible.value = false
  await loadNotices()
}

async function toggleNotice(row) {
  const nextStatus = row.status === '已发布' ? '已下架' : '已发布'
  await updateNoticeStatus(row.id, nextStatus)
  ElMessage.success(`公告已${nextStatus === '已发布' ? '发布' : '下架'}`)
  await loadNotices()
}

async function removeNotice(row) {
  await ElMessageBox.confirm(`确定要删除公告「${row.title}」吗？`, '删除公告确认', {
    type: 'warning',
  })
  await deleteNotice(row.id)
  ElMessage.success('公告已删除')
  await loadNotices()
}

function noticeStatusType(status) {
  if (status === '已发布') return 'success'
  if (status === '报名中') return 'warning'
  if (status === '已下架') return 'danger'
  return 'info'
}

function defaultNotice() {
  return {
    title: '',
    category: '通知',
    audience: '全体住户',
    publisher: '客服中心',
    status: '已发布',
  }
}

onMounted(loadNotices)
</script>
