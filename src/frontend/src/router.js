import { createRouter, createWebHistory } from 'vue-router'
import BillingView from './views/BillingView.vue'
import CommunitiesView from './views/CommunitiesView.vue'
import ComplaintsView from './views/ComplaintsView.vue'
import DashboardView from './views/DashboardView.vue'
import LoginView from './views/LoginView.vue'
import NoticesView from './views/NoticesView.vue'
import ParkingView from './views/ParkingView.vue'
import PropertiesView from './views/PropertiesView.vue'
import RepairsView from './views/RepairsView.vue'
import ResidentsView from './views/ResidentsView.vue'
import SecurityView from './views/SecurityView.vue'
import SmartServicesView from './views/SmartServicesView.vue'
import StatisticsView from './views/StatisticsView.vue'
import SystemUsersView from './views/SystemUsersView.vue'
import { clearAuth, getToken, getUser, hasValidUser } from './utils/auth'
import { canAccessModule } from './utils/roles'

const routes = [
  {
    path: '/login',
    component: LoginView,
    meta: {
      title: '登录',
      description: '物业后台登录入口',
      public: true,
    },
  },
  {
    path: '/',
    component: DashboardView,
    meta: {
      module: 'dashboard',
      title: '首页仪表盘',
      description: '查看小区经营指标、待办事项与重点业务趋势。',
    },
  },
  {
    path: '/residents',
    component: ResidentsView,
    meta: {
      module: 'residents',
      title: '住户管理',
      description: '统一维护业主、租户、联系方式与认证状态。',
    },
  },
  {
    path: '/communities',
    component: CommunitiesView,
    meta: {
      module: 'communities',
      title: '小区管理',
      description: '维护小区基础档案、物业公司、负责人和房屋规模。',
    },
  },
  {
    path: '/properties',
    component: PropertiesView,
    meta: {
      module: 'properties',
      title: '楼栋房屋',
      description: '查看楼栋分布、房屋状态、入住与空置情况。',
    },
  },
  {
    path: '/repairs',
    component: RepairsView,
    meta: {
      module: 'repairs',
      title: '报修工单',
      description: '管理维修派单、优先级、处理进度与回访。',
    },
  },
  {
    path: '/complaints',
    component: ComplaintsView,
    meta: {
      module: 'complaints',
      title: '投诉建议',
      description: '跟踪住户投诉建议、处理回复和服务闭环。',
    },
  },
  {
    path: '/billing',
    component: BillingView,
    meta: {
      module: 'billing',
      title: '收费管理',
      description: '追踪物业费、水电费、账单状态与收缴情况。',
    },
  },
  {
    path: '/parking',
    component: ParkingView,
    meta: {
      module: 'parking',
      title: '停车管理',
      description: '掌握车位、车辆、月租与访客停车使用情况。',
    },
  },
  {
    path: '/notices',
    component: NoticesView,
    meta: {
      module: 'notices',
      title: '公告活动',
      description: '发布社区公告、活动与触达范围。',
    },
  },
  {
    path: '/smart-services',
    component: SmartServicesView,
    meta: {
      module: 'smart-services',
      title: '智能服务',
      description: '统一管理公告通知、社区活动和快递代收服务。',
    },
  },
  {
    path: '/statistics',
    component: StatisticsView,
    meta: {
      module: 'statistics',
      title: '数据统计',
      description: '查看综合数据看板、报修统计、缴费率和投诉处理统计。',
    },
  },
  {
    path: '/security',
    component: SecurityView,
    meta: {
      module: 'security',
      title: '安防管理',
      description: '管理门禁设备、车辆档案和巡检任务。',
    },
  },
  {
    path: '/system-users',
    component: SystemUsersView,
    meta: {
      module: 'system-users',
      title: '系统用户',
      description: '维护后台账号、角色权限与登录安全状态。',
    },
  },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const token = getToken()
  if (to.meta.public) {
    if (token && to.path === '/login') {
      return '/'
    }
    return true
  }

  if (!token) {
    return '/login'
  }

  if (!hasValidUser()) {
    clearAuth()
    return '/login'
  }

  const moduleKey = to.meta.module
  if (moduleKey && !canAccessModule(getUser(), moduleKey)) {
    return '/'
  }

  return true
})
