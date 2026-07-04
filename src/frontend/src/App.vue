<template>
  <router-view v-if="route.path === '/login'" />

  <el-container v-else class="app-shell">
    <el-aside width="248px" class="sidebar">
      <div class="brand">
        <div class="brand-mark">
          <el-icon><OfficeBuilding /></el-icon>
        </div>
        <div>
          <strong>云栖物业平台</strong>
          <span>Property Operations</span>
        </div>
      </div>

      <el-scrollbar class="nav-scroll">
        <el-menu
          :default-active="route.path"
          router
          class="side-menu"
          background-color="transparent"
          text-color="#d8e5f6"
          active-text-color="#ffffff"
        >
          <el-menu-item
            v-for="item in visibleMenuItems"
            :key="item.path"
            :index="item.path"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>

      <div class="role-footnote">
        <span>当前权限</span>
        <strong>{{ roleName(currentUser?.roleCode) }}</strong>
      </div>
    </el-aside>

    <el-container>
      <el-header class="topbar">
        <div class="topbar-left">
          <div>
            <strong>{{ currentTitle }}</strong>
            <span>{{ currentDescription }}</span>
          </div>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>物业管理系统</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="topbar-right">
          <el-input
            v-model="globalKeyword"
            class="top-search"
            placeholder="搜索住户、工单、账单"
            :prefix-icon="Search"
            @keyup.enter="handleGlobalSearch"
          />
          <el-badge :value="notifications.length" :hidden="!notifications.length" class="notification-badge">
            <el-button circle plain @click="noticeVisible = true">
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>
          <div class="operator-panel">
            <el-avatar :size="36">{{ avatarText }}</el-avatar>
            <div>
              <strong>{{ currentUser?.realName || '运营管理员' }}</strong>
              <el-tag size="small" effect="plain" type="primary">{{ roleName(currentUser?.roleCode) }}</el-tag>
            </div>
          </div>
          <el-button plain @click="logout">退出</el-button>
        </div>
      </el-header>

      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>

    <el-drawer v-model="noticeVisible" title="消息通知" size="360px">
      <div v-if="notifications.length" class="notice-stack">
        <button
          v-for="item in notifications"
          :key="item.title"
          class="notice-item notice-action"
          type="button"
          @click="openNotification(item)"
        >
          <strong>{{ item.title }}</strong>
          <span>{{ item.content }}</span>
          <el-tag size="small" :type="item.type" effect="plain">{{ item.level }}</el-tag>
        </button>
      </div>
      <el-empty v-else description="暂无新的消息提醒" />
    </el-drawer>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Bell,
  CreditCard,
  DataBoard,
  Document,
  House,
  Location,
  OfficeBuilding,
  Search,
  SetUp,
  Tickets,
  User,
  Van,
} from '@element-plus/icons-vue'
import { clearAuth, getUser } from './utils/auth'
import { canAccessModule, roleName } from './utils/roles'

const route = useRoute()
const router = useRouter()
const currentUser = computed(() => getUser())
const noticeVisible = ref(false)
const globalKeyword = ref('')

const menuItems = [
  { module: 'communities', path: '/communities', label: '小区管理', icon: Location },
  { module: 'complaints', path: '/complaints', label: '投诉建议', icon: Document },
  { module: 'dashboard', path: '/', label: '首页仪表盘', icon: DataBoard },
  { module: 'residents', path: '/residents', label: '住户管理', icon: User },
  { module: 'properties', path: '/properties', label: '楼栋房屋', icon: House },
  { module: 'repairs', path: '/repairs', label: '报修工单', icon: Tickets },
  { module: 'billing', path: '/billing', label: '收费管理', icon: CreditCard },
  { module: 'parking', path: '/parking', label: '停车管理', icon: Van },
  { module: 'notices', path: '/notices', label: '公告活动', icon: Document },
  { module: 'smart-services', path: '/smart-services', label: '智能服务', icon: Tickets },
  { module: 'statistics', path: '/statistics', label: '数据统计', icon: DataBoard },
  { module: 'security', path: '/security', label: '安防管理', icon: SetUp },
  { module: 'system-users', path: '/system-users', label: '系统用户', icon: SetUp },
]

const visibleMenuItems = computed(() => {
  const user = currentUser.value
  return menuItems.filter((item) => canAccessModule(user, item.module))
})

const notifications = ref([
  { title: '高优先级工单待处理', content: '地下车库排风异常，请工程组优先跟进。', level: '工单', type: 'danger', path: '/repairs' },
  { title: '本月账单催缴', content: '仍有部分账单未缴费，建议财务发起提醒。', level: '收费', type: 'warning', path: '/billing' },
  { title: '公告触达完成', content: '7 月供水管道检修通知已推送给全体住户。', level: '公告', type: 'success', path: '/notices' },
  { title: '楼栋巡检提醒', content: 'C1 楼栋今日需要完成公共区域巡检。', level: '巡检', type: 'info', path: '/properties' },
])

const searchTargets = [
  { module: 'communities', path: '/communities', label: '小区管理', keywords: ['小区', '社区', '物业公司', '地址', '负责人'] },
  { module: 'complaints', path: '/complaints', label: '投诉建议', keywords: ['投诉', '建议', '反馈', '回复', '处理'] },
  { module: 'residents', path: '/residents', label: '住户管理', keywords: ['住户', '业主', '租户', '房号', '房间', '姓名', '手机', '电话'] },
  { module: 'properties', path: '/properties', label: '楼栋房屋', keywords: ['楼栋', '房屋', '房源', '入住', '空置', '装修'] },
  { module: 'repairs', path: '/repairs', label: '报修工单', keywords: ['工单', '报修', '维修', '派单', '处理', '漏水', '门禁'] },
  { module: 'billing', path: '/billing', label: '收费管理', keywords: ['账单', '收费', '缴费', '欠费', '逾期', '物业费', '水电'] },
  { module: 'parking', path: '/parking', label: '停车管理', keywords: ['停车', '车位', '车辆', '车牌', '月租', '临停'] },
  { module: 'notices', path: '/notices', label: '公告活动', keywords: ['公告', '通知', '活动', '发布', '下架'] },
  { module: 'smart-services', path: '/smart-services', label: '智能服务', keywords: ['智能', '活动', '快递', '代收', '通知'] },
  { module: 'statistics', path: '/statistics', label: '数据统计', keywords: ['统计', '图表', '缴费率', '投诉时效', '数据'] },
  { module: 'security', path: '/security', label: '安防管理', keywords: ['安防', '门禁', '巡检', '车辆', '道闸'] },
  { module: 'system-users', path: '/system-users', label: '系统用户', keywords: ['用户', '账号', '角色', '权限', '管理员'] },
]

const currentTitle = computed(() => route.meta.title || '首页仪表盘')
const currentDescription = computed(
  () => route.meta.description || '企业级物业运营管理演示系统',
)
const avatarText = computed(() => currentUser.value?.realName?.slice(0, 1) || '管')

function handleGlobalSearch() {
  const keyword = globalKeyword.value.trim()
  if (!keyword) {
    ElMessage.info('请输入搜索关键词')
    return
  }

  const target = searchTargets.find((item) => (
    canAccessModule(currentUser.value, item.module)
    && item.keywords.some((word) => keyword.includes(word))
  ))
    || visibleMenuItems.value.find((item) => item.path !== '/')

  if (!target) {
    ElMessage.warning('当前账号没有可跳转的业务页面')
    return
  }

  router.push({ path: target.path, query: { keyword } })
  ElMessage.success(`已跳转到${target.label || '对应页面'}：${keyword}`)
}

function openNotification(item) {
  notifications.value = notifications.value.filter((current) => current.title !== item.title)
  noticeVisible.value = false
  router.push(item.path)
  ElMessage.success(`已进入${item.level}页面，消息已处理`)
}

function logout() {
  clearAuth()
  router.push('/login')
}
</script>
