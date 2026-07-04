<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in smart.summary" :key="item.label" :xs="12" :lg="8">
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
            <strong>智能服务</strong>
            <span>公告通知发布、社区活动管理、快递代收管理</span>
          </div>
          <div class="toolbar-right">
            <el-button v-if="activeTab === 'activities' && canDo('button:activity:manage')" type="primary" @click="openActivity()">新增活动</el-button>
            <el-button v-if="activeTab === 'packages' && canDo('button:package:manage')" type="primary" @click="openPackage()">登记快递</el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="公告通知" name="notices">
          <el-table :data="smart.notices" v-loading="loading">
            <el-table-column prop="title" label="标题" min-width="220" />
            <el-table-column prop="category" label="类型" width="100" />
            <el-table-column prop="audience" label="范围" width="140" />
            <el-table-column prop="publisher" label="发布部门" width="130" />
            <el-table-column prop="status" label="状态" width="100" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="社区活动" name="activities">
          <el-table :data="smart.activities" v-loading="loading">
            <el-table-column prop="title" label="活动名称" min-width="180" />
            <el-table-column prop="activityType" label="类型" width="120" />
            <el-table-column prop="location" label="地点" width="150" />
            <el-table-column prop="organizer" label="组织部门" width="130" />
            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column prop="signups" label="报名人数" width="100" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }"><el-tag effect="plain">{{ row.status }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="210" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:activity:manage')" link type="primary" @click="openActivity(row)">编辑</el-button>
                <el-button v-if="canDo('button:activity:manage')" link type="warning" @click="toggleActivity(row)">{{ row.status === '报名中' ? '结束' : '开启' }}</el-button>
                <el-button v-if="canDo('button:activity:manage')" link type="danger" @click="removeActivity(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="快递代收" name="packages">
          <el-table :data="smart.packages" v-loading="loading">
            <el-table-column prop="trackingNo" label="快递单号" width="150" fixed="left" />
            <el-table-column prop="recipientName" label="收件人" width="110" />
            <el-table-column prop="phone" label="电话" width="130" />
            <el-table-column prop="pickupCode" label="取件码" width="100" />
            <el-table-column prop="cabinetNo" label="存放位置" width="130" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '已领取' ? 'success' : 'warning'" effect="plain">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="arrivedAt" label="到达时间" width="180" />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:package:manage')" link type="primary" @click="openPackage(row)">编辑</el-button>
                <el-button v-if="canDo('button:package:manage')" link type="success" :disabled="row.status === '已领取'" @click="markPicked(row)">领取</el-button>
                <el-button v-if="canDo('button:package:manage')" link type="danger" @click="removePackage(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="activityVisible" :title="activityForm.id ? '编辑社区活动' : '新增社区活动'" width="560px">
      <el-form :model="activityForm" label-position="top">
        <el-form-item label="活动名称"><el-input v-model="activityForm.title" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="activityForm.activityType" /></el-form-item>
        <el-form-item label="地点"><el-input v-model="activityForm.location" /></el-form-item>
        <el-form-item label="组织部门"><el-input v-model="activityForm.organizer" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="activityForm.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" /></el-form-item>
        <el-form-item label="报名人数"><el-input-number v-model="activityForm.signups" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="activityForm.status" style="width: 100%"><el-option label="报名中" value="报名中" /><el-option label="筹备中" value="筹备中" /><el-option label="已结束" value="已结束" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="activityVisible = false">取消</el-button><el-button type="primary" @click="submitActivity">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="packageVisible" :title="packageForm.id ? '编辑快递' : '登记快递'" width="560px">
      <el-form :model="packageForm" label-position="top">
        <el-form-item label="快递单号"><el-input v-model="packageForm.trackingNo" /></el-form-item>
        <el-form-item label="收件人"><el-input v-model="packageForm.recipientName" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="packageForm.phone" /></el-form-item>
        <el-form-item label="取件码"><el-input v-model="packageForm.pickupCode" /></el-form-item>
        <el-form-item label="存放位置"><el-input v-model="packageForm.cabinetNo" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="packageForm.status" style="width: 100%"><el-option label="待领取" value="待领取" /><el-option label="已领取" value="已领取" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="packageVisible = false">取消</el-button><el-button type="primary" @click="submitPackage">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createActivity, createPackage, deleteActivity, deletePackage, fetchSmartServices, updateActivity, updateActivityStatus, updatePackage, updatePackageStatus } from '../api/property'
import { getUser } from '../utils/auth'
import { hasPermission } from '../utils/roles'

const smart = ref({ summary: [], notices: [], activities: [], packages: [] })
const loading = ref(false)
const activeTab = ref('notices')
const activityVisible = ref(false)
const packageVisible = ref(false)
const activityForm = reactive(defaultActivity())
const packageForm = reactive(defaultPackage())
const currentUser = computed(() => getUser())

function canDo(code) {
  return hasPermission(currentUser.value, code)
}

async function loadSmart() {
  loading.value = true
  try {
    const response = await fetchSmartServices()
    smart.value = response.data
  } finally {
    loading.value = false
  }
}

function openActivity(row) {
  Object.assign(activityForm, row || defaultActivity())
  activityVisible.value = true
}

async function submitActivity() {
  if (!activityForm.title || !activityForm.location || !activityForm.organizer) {
    ElMessage.warning('请补全活动名称、地点和组织部门')
    return
  }
  if (activityForm.id) await updateActivity(activityForm.id, activityForm)
  else await createActivity(activityForm)
  ElMessage.success('社区活动已保存')
  activityVisible.value = false
  await loadSmart()
}

async function toggleActivity(row) {
  await updateActivityStatus(row.id, row.status === '报名中' ? '已结束' : '报名中')
  await loadSmart()
}

async function removeActivity(row) {
  await ElMessageBox.confirm(`确定删除活动 ${row.title} 吗？`, '删除确认', { type: 'warning' })
  await deleteActivity(row.id)
  await loadSmart()
}

function openPackage(row) {
  Object.assign(packageForm, row || defaultPackage())
  packageVisible.value = true
}

async function submitPackage() {
  if (!packageForm.trackingNo || !packageForm.recipientName || !packageForm.phone) {
    ElMessage.warning('请补全快递单号、收件人和电话')
    return
  }
  if (packageForm.id) await updatePackage(packageForm.id, packageForm)
  else await createPackage(packageForm)
  ElMessage.success('快递信息已保存')
  packageVisible.value = false
  await loadSmart()
}

async function markPicked(row) {
  await updatePackageStatus(row.id, '已领取')
  await loadSmart()
}

async function removePackage(row) {
  await ElMessageBox.confirm(`确定删除快递 ${row.trackingNo} 吗？`, '删除确认', { type: 'warning' })
  await deletePackage(row.id)
  await loadSmart()
}

function defaultActivity() {
  return { id: null, title: '', activityType: '社区活动', location: '', organizer: '社区运营部', startTime: '2026-07-08T15:00:00', signups: 0, status: '报名中' }
}

function defaultPackage() {
  return { id: null, trackingNo: '', recipientName: '', phone: '', pickupCode: '', cabinetNo: '前台货架', status: '待领取' }
}

onMounted(loadSmart)
</script>
