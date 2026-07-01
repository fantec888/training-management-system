import { createRouter, createWebHistory } from 'vue-router'
import BillingView from './views/BillingView.vue'
import DashboardView from './views/DashboardView.vue'
import LoginView from './views/LoginView.vue'
import NoticesView from './views/NoticesView.vue'
import ParkingView from './views/ParkingView.vue'
import PropertiesView from './views/PropertiesView.vue'
import RepairsView from './views/RepairsView.vue'
import ResidentsView from './views/ResidentsView.vue'
import SystemUsersView from './views/SystemUsersView.vue'
import { getToken } from './utils/auth'

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
      title: '首页仪表盘',
      description: '查看小区经营指标、待办事项与重点业务趋势。',
    },
  },
  {
    path: '/residents',
    component: ResidentsView,
    meta: {
      title: '住户管理',
      description: '统一维护业主、租户、联系方式与认证状态。',
    },
  },
  {
    path: '/properties',
    component: PropertiesView,
    meta: {
      title: '楼栋房屋',
      description: '查看楼栋分布、房屋状态、入住与空置情况。',
    },
  },
  {
    path: '/repairs',
    component: RepairsView,
    meta: {
      title: '报修工单',
      description: '管理维修派单、优先级、处理进度与回访。',
    },
  },
  {
    path: '/billing',
    component: BillingView,
    meta: {
      title: '收费管理',
      description: '追踪物业费、水电费、账单状态与收缴情况。',
    },
  },
  {
    path: '/parking',
    component: ParkingView,
    meta: {
      title: '停车管理',
      description: '掌握车位、车辆、月租与访客停车使用情况。',
    },
  },
  {
    path: '/notices',
    component: NoticesView,
    meta: {
      title: '公告活动',
      description: '发布社区公告、活动与触达范围。',
    },
  },
  {
    path: '/system-users',
    component: SystemUsersView,
    meta: {
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

  return true
})
