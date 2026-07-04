<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in parking.stats" :key="item.label" :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.label }}</span>
          <strong class="metric-value">{{ item.value }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar-row">
        <div class="toolbar-left">
          <el-input v-model="keyword" placeholder="搜索车牌、住户、车位位置" clearable />
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="panel-card">
      <template #header>
        <div class="panel-header">
          <div>
            <strong>车辆与车位列表</strong>
            <span>固定车、临停车与月租状态</span>
          </div>
        </div>
      </template>

      <el-table :data="pagedVehicles" v-if="filteredVehicles.length">
        <el-table-column prop="plate" label="车牌号" width="130" fixed="left" />
        <el-table-column prop="owner" label="所属住户" width="130" />
        <el-table-column prop="vehicleType" label="车辆类型" width="120" />
        <el-table-column prop="location" label="车位位置" width="140" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.status === '正常' || row.status === '在场' ? 'success' : 'warning'" effect="plain">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="月租费用" min-width="120">
          <template #default="{ row }">{{ row.monthlyFee ? toCurrency(row.monthlyFee) : '临停计费' }}</template>
        </el-table-column>
      </el-table>
      <div class="table-pagination" v-if="filteredVehicles.length">
        <el-pagination
          v-model:current-page="parkingPage"
          v-model:page-size="parkingPageSize"
          :page-sizes="parkingPageSizes"
          :total="parkingTotal"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
      <el-empty v-else description="暂无停车数据" />
    </el-card>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchParking } from '../api/property'
import { parkingFallback } from '../data/mock'
import { toCurrency } from '../utils/format'
import { useClientPagination } from '../utils/pagination'

const parking = ref(parkingFallback)
const keyword = ref('')
const filteredVehicles = computed(() => {
  const vehicles = parking.value.vehicles || []
  if (!keyword.value) return vehicles
  return vehicles.filter((item) => `${item.plate}${item.owner}${item.location}${item.vehicleType}`.includes(keyword.value))
})
const {
  page: parkingPage,
  pageSize: parkingPageSize,
  pageSizes: parkingPageSizes,
  total: parkingTotal,
  records: pagedVehicles,
} = useClientPagination(filteredVehicles)

async function loadParking() {
  try {
    const response = await fetchParking()
    parking.value = response.data
  } catch (error) {
    ElMessage.warning('停车数据暂时无法加载，已显示默认状态')
    parking.value = parkingFallback
  }
}

onMounted(loadParking)
</script>
