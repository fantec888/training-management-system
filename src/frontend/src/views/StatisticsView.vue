<template>
  <section class="page-grid">
    <el-row :gutter="12">
      <el-col v-for="item in stats.summary" :key="item.label" :xs="12" :lg="6">
        <el-card shadow="never" class="metric-card compact">
          <span class="metric-label">{{ item.label }}</span>
          <strong class="metric-value">{{ item.value }}</strong>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="panel-card">
          <template #header><div class="panel-header"><strong>报修统计图表</strong><span>待处理、已完成与高优先级</span></div></template>
          <div class="summary-stack">
            <div class="summary-item"><span>待处理工单</span><strong>{{ stats.repairChart.pending || 0 }}</strong></div>
            <div class="summary-item"><span>已完成工单</span><strong>{{ stats.repairChart.finished || 0 }}</strong></div>
            <div class="summary-item"><span>高优先级</span><strong>{{ stats.repairChart.highPriority || 0 }}</strong></div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="panel-card">
          <template #header><div class="panel-header"><strong>缴费率统计</strong><span>应收、实收和未收</span></div></template>
          <div class="summary-stack">
            <div class="summary-item"><span>应收金额</span><strong>{{ toCurrency(stats.paymentChart.receivable || 0) }}</strong></div>
            <div class="summary-item"><span>实收金额</span><strong>{{ toCurrency(stats.paymentChart.paid || 0) }}</strong></div>
            <div class="summary-item"><span>未收金额</span><strong>{{ toCurrency(stats.paymentChart.unpaid || 0) }}</strong></div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="panel-card">
          <template #header><div class="panel-header"><strong>投诉处理时效</strong><span>处理闭环和待跟进</span></div></template>
          <div class="summary-stack">
            <div class="summary-item"><span>投诉总量</span><strong>{{ stats.complaintChart.total || 0 }}</strong></div>
            <div class="summary-item"><span>已处理</span><strong>{{ stats.complaintChart.handled || 0 }}</strong></div>
            <div class="summary-item"><span>待处理</span><strong>{{ stats.complaintChart.pending || 0 }}</strong></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchAdvancedStatistics } from '../api/property'
import { toCurrency } from '../utils/format'

const stats = ref({ summary: [], repairChart: {}, paymentChart: {}, complaintChart: {} })

async function loadStats() {
  try {
    const response = await fetchAdvancedStatistics()
    stats.value = response.data
  } catch (error) {
    ElMessage.warning('统计数据暂时无法加载')
  }
}

onMounted(loadStats)
</script>
