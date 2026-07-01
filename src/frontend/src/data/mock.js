export const dashboardFallback = {
  stats: [
    { label: '总管理户数', value: '0', trend: '+0%', detail: '等待数据加载' },
    { label: '本月收缴率', value: '0%', trend: '+0%', detail: '等待数据加载' },
    { label: '待处理工单', value: '0', trend: '0', detail: '等待数据加载' },
    { label: '停车使用率', value: '0%', trend: '0%', detail: '等待数据加载' },
  ],
  revenueTrend: [],
  workOrderTrend: [],
  todoList: [],
  announcements: [],
}

export const residentsFallback = []
export const propertiesFallback = { summary: [], buildings: [] }
export const repairsFallback = []
export const billsFallback = { summary: {}, bills: [] }
export const parkingFallback = { stats: [], vehicles: [] }
export const noticesFallback = { summary: [], list: [] }
export const systemUsersFallback = []
