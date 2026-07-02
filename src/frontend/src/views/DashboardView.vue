<template>
  <section class="page-grid">
    <div class="dashboard-hero">
      <div class="command-panel">
        <small>PROPERTY OPERATIONS CENTER</small>
        <h2>今日运营看板</h2>
        <p>
          首页数据直接来自住户、房屋、收费、报修、停车和系统用户等业务表。你在管理页面新增或修改数据后，回到这里刷新即可看到统计变化。
        </p>
        <div class="command-actions">
          <el-button type="primary" @click="refreshDashboard">刷新数据</el-button>
        </div>
      </div>

      <div class="command-side">
        <div class="command-side-row">
          <span>系统状态</span>
          <el-tag type="success" effect="plain">运行中</el-tag>
        </div>
        <div class="command-side-row">
          <span>数据库</span>
          <strong>H2 / MySQL 兼容</strong>
        </div>
        <div class="command-side-row">
          <span>演示账号</span>
          <strong>admin / 123456</strong>
        </div>
      </div>
    </div>

    <div class="stat-strip dashboard-stat-strip">
      <el-card v-for="item in stats" :key="item.label" shadow="never" class="metric-card">
        <span class="metric-label">{{ item.label }}</span>
        <strong class="metric-value">{{ item.value }}</strong>
        <div class="metric-foot">
          <el-tag size="small" effect="plain" type="primary">{{ item.trend }}</el-tag>
          <span>{{ item.detail }}</span>
        </div>
      </el-card>
    </div>

    <div class="dashboard-grid">
      <el-card shadow="never" class="panel-card">
        <template #header>
          <div class="panel-header">
            <div>
              <strong>半年度营收趋势</strong>
              <span>单位：万元，反映物业、停车与水电账单综合收缴情况</span>
            </div>
          </div>
        </template>

        <div class="bar-chart" v-if="revenueTrend.length">
          <div v-for="item in revenueTrend" :key="item.monthLabel" class="bar-chart-item">
            <div class="bar-value">{{ item.amount }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ height: `${Math.min(Number(item.amount) * 0.78, 100)}%` }" />
            </div>
            <span>{{ item.monthLabel }}</span>
          </div>
        </div>
        <el-empty v-else description="暂无营收数据" />
      </el-card>

      <el-card shadow="never" class="panel-card">
        <template #header>
          <div class="panel-header">
            <div>
              <strong>工单优先级分布</strong>
              <span>报修工单新增或处理后，这里会跟随数据库变化</span>
            </div>
          </div>
        </template>

        <div class="progress-stack" v-if="workOrderTrend.length">
          <div v-for="item in workOrderTrend" :key="item.label" class="progress-item">
            <div class="progress-label">
              <span>{{ item.label }}</span>
              <strong>{{ item.value }} 单</strong>
            </div>
            <el-progress :percentage="Math.min(Number(item.value) * 18, 100)" :stroke-width="9" :show-text="false" />
          </div>
        </div>
        <el-empty v-else description="暂无工单统计" />
      </el-card>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :xl="10">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <div class="panel-header">
              <div>
                <strong>模块数据联动</strong>
                <span>每个模块都对应真实数据库表和后台接口</span>
              </div>
            </div>
          </template>

          <div class="module-health" v-if="moduleHealth.length">
            <div v-for="item in moduleHealth" :key="item.module" class="module-health-item">
              <div>
                <strong>{{ item.module }}</strong>
                <span>{{ item.description }}</span>
              </div>
              <el-tag effect="plain" type="success">{{ item.count }} 条</el-tag>
            </div>
          </div>
          <el-empty v-else description="暂无模块统计" />
        </el-card>
      </el-col>

      <el-col :xs="24" :xl="14">
        <el-card shadow="never" class="panel-card">
          <template #header>
            <div class="panel-header">
              <div>
                <strong>最近公告</strong>
                <span>公告活动模块发布后会同步展示到首页</span>
              </div>
            </div>
          </template>

          <el-table :data="announcements" v-if="announcements.length">
            <el-table-column prop="title" label="公告标题" min-width="240" />
            <el-table-column prop="audience" label="触达范围" width="130" />
            <el-table-column prop="publishTime" label="发布时间" width="180" />
          </el-table>
          <el-empty v-else description="暂无公告" />
        </el-card>
      </el-col>
    </el-row>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchDashboard } from '../api/dashboard'
import { dashboardFallback } from '../data/mock'

const stats = ref(dashboardFallback.stats)
const revenueTrend = ref([])
const workOrderTrend = ref([])
const announcements = ref([])
const moduleHealth = ref([])

async function loadDashboard() {
  try {
    const response = await fetchDashboard()
    const data = response.data
    stats.value = (data.stats || []).map((item) => ({
      ...item,
      trend: item.trend || '',
      detail: item.detail || '',
      value: item.value,
    }))
    revenueTrend.value = data.revenueTrend || []
    workOrderTrend.value = data.workOrderTrend || []
    announcements.value = data.announcements || []
    moduleHealth.value = data.moduleHealth || []
  } catch (error) {
    ElMessage.warning('仪表盘数据暂时无法加载，已显示默认状态')
    stats.value = dashboardFallback.stats
    revenueTrend.value = dashboardFallback.revenueTrend
    workOrderTrend.value = dashboardFallback.workOrderTrend
    announcements.value = dashboardFallback.announcements
  }
}

async function refreshDashboard() {
  await loadDashboard()
  ElMessage.success('仪表盘数据已刷新')
}

onMounted(loadDashboard)
</script>
