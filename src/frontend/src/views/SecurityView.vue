<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in security.summary" :key="item.label" :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.label }}</span>
          <strong class="metric-value">{{ item.value }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div><strong>安防管理</strong><span>门禁管理、车辆管理、巡检管理</span></div>
          <div class="toolbar-right">
            <el-button v-if="activeTab === 'access' && canDo('button:access:manage')" type="primary" @click="openAccess()">新增门禁</el-button>
            <el-button v-if="activeTab === 'patrols' && canDo('button:patrol:manage')" type="primary" @click="openPatrol()">新增巡检</el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="门禁管理" name="access">
          <el-table :data="security.accessControls" v-loading="loading">
            <el-table-column prop="deviceName" label="设备名称" min-width="150" />
            <el-table-column prop="gateName" label="位置" width="160" />
            <el-table-column prop="deviceType" label="类型" width="120" />
            <el-table-column label="状态" width="100"><template #default="{ row }"><el-tag :type="row.status === '在线' ? 'success' : 'danger'" effect="plain">{{ row.status }}</el-tag></template></el-table-column>
            <el-table-column prop="manager" label="负责人" width="120" />
            <el-table-column prop="lastCheckAt" label="最近检查" width="180" />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:access:manage')" link type="primary" @click="openAccess(row)">编辑</el-button>
                <el-button v-if="canDo('button:access:manage')" link type="warning" @click="toggleAccess(row)">{{ row.status === '在线' ? '离线' : '在线' }}</el-button>
                <el-button v-if="canDo('button:access:manage')" link type="danger" @click="removeAccess(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="车辆管理" name="vehicles">
          <el-table :data="security.vehicles" v-loading="loading">
            <el-table-column prop="plate" label="车牌号" width="130" />
            <el-table-column prop="owner" label="所属住户" width="130" />
            <el-table-column prop="vehicleType" label="车辆类型" width="120" />
            <el-table-column prop="location" label="车位位置" width="140" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column label="月租费用"><template #default="{ row }">{{ row.monthlyFee ? toCurrency(row.monthlyFee) : '临停计费' }}</template></el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="巡检管理" name="patrols">
          <el-table :data="security.patrols" v-loading="loading">
            <el-table-column prop="routeName" label="巡检路线" min-width="160" />
            <el-table-column prop="area" label="区域" min-width="180" />
            <el-table-column prop="assignee" label="巡检人" width="110" />
            <el-table-column prop="planTime" label="计划时间" width="180" />
            <el-table-column label="状态" width="100"><template #default="{ row }"><el-tag :type="row.status === '已完成' ? 'success' : 'warning'" effect="plain">{{ row.status }}</el-tag></template></el-table-column>
            <el-table-column prop="result" label="结果" min-width="200" />
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button v-if="canDo('button:patrol:manage')" link type="primary" @click="openPatrol(row)">编辑</el-button>
                <el-button v-if="canDo('button:patrol:manage')" link type="success" :disabled="row.status === '已完成'" @click="finish(row)">完成</el-button>
                <el-button v-if="canDo('button:patrol:manage')" link type="danger" @click="removePatrol(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="accessVisible" :title="accessForm.id ? '编辑门禁设备' : '新增门禁设备'" width="560px">
      <el-form :model="accessForm" label-position="top">
        <el-form-item label="设备名称"><el-input v-model="accessForm.deviceName" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="accessForm.gateName" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="accessForm.deviceType" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="accessForm.manager" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="accessForm.status" style="width: 100%"><el-option label="在线" value="在线" /><el-option label="离线" value="离线" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="accessVisible = false">取消</el-button><el-button type="primary" @click="submitAccess">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="patrolVisible" :title="patrolForm.id ? '编辑巡检任务' : '新增巡检任务'" width="560px">
      <el-form :model="patrolForm" label-position="top">
        <el-form-item label="路线名称"><el-input v-model="patrolForm.routeName" /></el-form-item>
        <el-form-item label="巡检区域"><el-input v-model="patrolForm.area" /></el-form-item>
        <el-form-item label="巡检人"><el-input v-model="patrolForm.assignee" /></el-form-item>
        <el-form-item label="计划时间"><el-date-picker v-model="patrolForm.planTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="patrolForm.status" style="width: 100%"><el-option label="待巡检" value="待巡检" /><el-option label="巡检中" value="巡检中" /><el-option label="已完成" value="已完成" /></el-select></el-form-item>
        <el-form-item label="结果"><el-input v-model="patrolForm.result" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="patrolVisible = false">取消</el-button><el-button type="primary" @click="submitPatrol">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createAccessControl, createPatrol, deleteAccessControl, deletePatrol, fetchSecurity, finishPatrol, updateAccessControl, updateAccessStatus, updatePatrol } from '../api/property'
import { getUser } from '../utils/auth'
import { toCurrency } from '../utils/format'
import { hasPermission } from '../utils/roles'

const security = ref({ summary: [], accessControls: [], vehicles: [], patrols: [] })
const loading = ref(false)
const activeTab = ref('access')
const accessVisible = ref(false)
const patrolVisible = ref(false)
const accessForm = reactive(defaultAccess())
const patrolForm = reactive(defaultPatrol())
const currentUser = computed(() => getUser())

function canDo(code) {
  return hasPermission(currentUser.value, code)
}

async function loadSecurity() {
  loading.value = true
  try {
    const response = await fetchSecurity()
    security.value = response.data
  } finally {
    loading.value = false
  }
}

function openAccess(row) {
  Object.assign(accessForm, row || defaultAccess())
  accessVisible.value = true
}

async function submitAccess() {
  if (!accessForm.deviceName || !accessForm.gateName || !accessForm.manager) {
    ElMessage.warning('请补全设备名称、位置和负责人')
    return
  }
  if (accessForm.id) await updateAccessControl(accessForm.id, accessForm)
  else await createAccessControl(accessForm)
  accessVisible.value = false
  await loadSecurity()
}

async function toggleAccess(row) {
  await updateAccessStatus(row.id, row.status === '在线' ? '离线' : '在线')
  await loadSecurity()
}

async function removeAccess(row) {
  await ElMessageBox.confirm(`确定删除门禁设备 ${row.deviceName} 吗？`, '删除确认', { type: 'warning' })
  await deleteAccessControl(row.id)
  await loadSecurity()
}

function openPatrol(row) {
  Object.assign(patrolForm, row || defaultPatrol())
  patrolVisible.value = true
}

async function submitPatrol() {
  if (!patrolForm.routeName || !patrolForm.area || !patrolForm.assignee) {
    ElMessage.warning('请补全路线、区域和巡检人')
    return
  }
  if (patrolForm.id) await updatePatrol(patrolForm.id, patrolForm)
  else await createPatrol(patrolForm)
  patrolVisible.value = false
  await loadSecurity()
}

async function finish(row) {
  await finishPatrol(row.id, { status: '已完成', result: row.result || '巡检正常' })
  await loadSecurity()
}

async function removePatrol(row) {
  await ElMessageBox.confirm(`确定删除巡检任务 ${row.routeName} 吗？`, '删除确认', { type: 'warning' })
  await deletePatrol(row.id)
  await loadSecurity()
}

function defaultAccess() {
  return { id: null, deviceName: '', gateName: '', deviceType: '人脸识别', status: '在线', manager: '' }
}

function defaultPatrol() {
  return { id: null, routeName: '', area: '', assignee: '', planTime: '2026-07-05T10:00:00', status: '待巡检', result: '' }
}

onMounted(loadSecurity)
</script>
