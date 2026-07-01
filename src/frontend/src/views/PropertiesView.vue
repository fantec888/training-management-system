<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in properties.summary" :key="item.label" :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.label }}</span>
          <strong class="metric-value">{{ item.value }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-segmented v-model="activeTab" :options="['楼栋列表', '房屋列表']" />
        </div>
        <div class="toolbar-right">
          <el-select v-if="activeTab === '房屋列表'" v-model="roomStatusFilter" clearable placeholder="房屋状态">
            <el-option label="全部状态" value="" />
            <el-option label="空置" value="空置" />
            <el-option label="已入住" value="已入住" />
            <el-option label="装修中" value="装修中" />
          </el-select>
          <el-button v-if="activeTab === '楼栋列表'" type="primary" @click="openBuilding()">新增楼栋</el-button>
          <el-button v-else type="primary" @click="openRoom()">新增房屋</el-button>
        </div>
      </div>
    </el-card>

    <el-card v-if="activeTab === '楼栋列表'" shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>楼栋资源分布</strong>
            <span>维护楼栋、楼层、单元、入住率和楼栋负责人</span>
          </div>
        </div>
      </template>

      <el-table :data="properties.buildings" v-if="properties.buildings.length">
        <el-table-column prop="name" label="楼栋" width="100" fixed="left" />
        <el-table-column prop="floors" label="楼层数" width="100" />
        <el-table-column prop="units" label="单元数" width="100" />
        <el-table-column label="入住率" width="120">
          <template #default="{ row }">{{ toPercent(row.occupancyRate) }}</template>
        </el-table-column>
        <el-table-column prop="vacantCount" label="空置套数" width="120" />
        <el-table-column prop="manager" label="楼栋负责人" width="140" />
        <el-table-column label="入住概览" min-width="220">
          <template #default="{ row }">
            <el-progress :percentage="Number(row.occupancyRate)" :stroke-width="9" status="success" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openBuilding(row)">编辑</el-button>
            <el-button link type="danger" @click="removeBuilding(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无楼栋数据" />
    </el-card>

    <el-card v-else shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>房屋列表</strong>
            <span>维护房屋状态、面积、住户绑定和装修/空置状态</span>
          </div>
        </div>
      </template>

      <el-table :data="filteredRooms" v-if="filteredRooms.length">
        <el-table-column prop="buildingName" label="楼栋" width="90" fixed="left" />
        <el-table-column prop="unitNo" label="单元" width="100" />
        <el-table-column prop="roomNo" label="房号" width="100" />
        <el-table-column prop="floorNo" label="楼层" width="90" />
        <el-table-column label="面积" width="100">
          <template #default="{ row }">{{ row.area }}㎡</template>
        </el-table-column>
        <el-table-column prop="residentName" label="绑定住户" width="130" />
        <el-table-column label="房屋状态" width="120">
          <template #default="{ row }">
            <el-tag :type="roomStatusType(row.status)" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openRoom(row)">编辑</el-button>
            <el-button link type="success" @click="openBind(row)">绑定住户</el-button>
            <el-button link type="danger" @click="removeRoom(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无房屋数据" />
    </el-card>

    <el-dialog v-model="buildingVisible" :title="buildingForm.id ? '编辑楼栋' : '新增楼栋'" width="560px">
      <el-form :model="buildingForm" label-position="top">
        <el-form-item label="楼栋名称">
          <el-input v-model="buildingForm.name" />
        </el-form-item>
        <el-form-item label="楼层数">
          <el-input-number v-model="buildingForm.floors" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单元数">
          <el-input-number v-model="buildingForm.units" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="入住率">
          <el-input-number v-model="buildingForm.occupancyRate" :min="0" :max="100" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="空置套数">
          <el-input-number v-model="buildingForm.vacantCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="buildingForm.manager" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="buildingVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBuilding">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="roomVisible" :title="roomForm.id ? '编辑房屋' : '新增房屋'" width="560px">
      <el-form :model="roomForm" label-position="top">
        <el-form-item label="楼栋">
          <el-input v-model="roomForm.buildingName" />
        </el-form-item>
        <el-form-item label="单元">
          <el-input v-model="roomForm.unitNo" />
        </el-form-item>
        <el-form-item label="房号">
          <el-input v-model="roomForm.roomNo" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number v-model="roomForm.floorNo" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="面积">
          <el-input-number v-model="roomForm.area" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="绑定住户">
          <el-input v-model="roomForm.residentName" />
        </el-form-item>
        <el-form-item label="房屋状态">
          <el-select v-model="roomForm.status" style="width: 100%">
            <el-option label="空置" value="空置" />
            <el-option label="已入住" value="已入住" />
            <el-option label="装修中" value="装修中" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roomVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoom">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="bindVisible" title="绑定住户" width="480px">
      <el-form :model="bindForm" label-position="top">
        <el-form-item label="住户姓名">
          <el-input v-model="bindForm.residentName" placeholder="清空姓名可解除绑定" />
        </el-form-item>
        <el-form-item label="房屋状态">
          <el-select v-model="bindForm.status" style="width: 100%">
            <el-option label="空置" value="空置" />
            <el-option label="已入住" value="已入住" />
            <el-option label="装修中" value="装修中" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bindVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBind">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  bindRoomResident,
  createBuilding,
  createRoom,
  deleteBuilding,
  deleteRoom,
  fetchProperties,
  updateBuilding,
  updateRoom,
} from '../api/property'
import { propertiesFallback } from '../data/mock'
import { toPercent } from '../utils/format'

const properties = ref({ ...propertiesFallback, rooms: [] })
const activeTab = ref('楼栋列表')
const roomStatusFilter = ref('')
const buildingVisible = ref(false)
const roomVisible = ref(false)
const bindVisible = ref(false)
const buildingForm = reactive(defaultBuilding())
const roomForm = reactive(defaultRoom())
const bindForm = reactive({ id: null, residentName: '', status: '已入住' })

const filteredRooms = computed(() => {
  const rooms = properties.value.rooms || []
  if (!roomStatusFilter.value) return rooms
  return rooms.filter((item) => item.status === roomStatusFilter.value)
})

async function loadProperties() {
  try {
    const response = await fetchProperties()
    properties.value = response.data
  } catch (error) {
    ElMessage.warning('楼栋房屋数据暂时无法加载，已显示默认状态')
    properties.value = { ...propertiesFallback, rooms: [] }
  }
}

function openBuilding(row) {
  Object.assign(buildingForm, row || defaultBuilding())
  buildingVisible.value = true
}

async function submitBuilding() {
  if (!buildingForm.name || !buildingForm.manager) {
    ElMessage.warning('请补全楼栋名称和负责人')
    return
  }
  if (buildingForm.id) {
    await updateBuilding(buildingForm.id, buildingForm)
  } else {
    await createBuilding(buildingForm)
  }
  ElMessage.success('楼栋信息已保存')
  buildingVisible.value = false
  await loadProperties()
}

async function removeBuilding(row) {
  await ElMessageBox.confirm(`确定要删除楼栋 ${row.name} 吗？`, '删除楼栋确认', {
    type: 'warning',
  })
  await deleteBuilding(row.id)
  ElMessage.success('楼栋已删除')
  await loadProperties()
}

function openRoom(row) {
  Object.assign(roomForm, row || defaultRoom())
  roomVisible.value = true
}

async function submitRoom() {
  if (!roomForm.buildingName || !roomForm.unitNo || !roomForm.roomNo) {
    ElMessage.warning('请补全楼栋、单元和房号')
    return
  }
  if (roomForm.id) {
    await updateRoom(roomForm.id, roomForm)
  } else {
    await createRoom(roomForm)
  }
  ElMessage.success('房屋信息已保存')
  roomVisible.value = false
  await loadProperties()
}

async function removeRoom(row) {
  await ElMessageBox.confirm(`确定要删除房屋 ${row.buildingName}-${row.unitNo}-${row.roomNo} 吗？`, '删除房屋确认', {
    type: 'warning',
  })
  await deleteRoom(row.id)
  ElMessage.success('房屋已删除')
  await loadProperties()
}

function openBind(row) {
  Object.assign(bindForm, {
    id: row.id,
    residentName: row.residentName || '',
    status: row.status || '已入住',
  })
  bindVisible.value = true
}

async function submitBind() {
  await bindRoomResident(bindForm.id, bindForm)
  ElMessage.success('房屋绑定已更新')
  bindVisible.value = false
  await loadProperties()
}

function roomStatusType(status) {
  if (status === '已入住') return 'success'
  if (status === '装修中') return 'warning'
  return 'info'
}

function defaultBuilding() {
  return {
    id: null,
    name: '',
    floors: 26,
    units: 2,
    occupancyRate: 90,
    vacantCount: 0,
    manager: '',
    status: '正常',
  }
}

function defaultRoom() {
  return {
    id: null,
    buildingName: '',
    unitNo: '1单元',
    roomNo: '',
    floorNo: 1,
    area: 90,
    residentName: '',
    status: '空置',
  }
}

onMounted(loadProperties)
</script>
