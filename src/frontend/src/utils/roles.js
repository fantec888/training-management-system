export const ROLE_NAMES = {
  SUPER_ADMIN: '管理员',
  SERVICE_MANAGER: '物业人员',
  FINANCE_ADMIN: '财务',
  ENGINEER_LEAD: '维修',
}

export const ROLE_MODULES = {
  SUPER_ADMIN: ['dashboard', 'residents', 'properties', 'repairs', 'billing', 'parking', 'notices', 'system-users'],
  SERVICE_MANAGER: ['dashboard', 'residents', 'properties', 'repairs', 'parking', 'notices'],
  FINANCE_ADMIN: ['dashboard', 'billing', 'parking', 'residents'],
  ENGINEER_LEAD: ['dashboard', 'properties', 'repairs', 'parking'],
}

export const ROLE_PRESETS = [
  { name: '管理员', code: 'SUPER_ADMIN', department: '平台运营中心', scope: '系统全模块配置、账号管理和核心运营数据查看' },
  { name: '财务', code: 'FINANCE_ADMIN', department: '财务部', scope: '收费账单、缴费确认、欠费提醒和收缴统计' },
  { name: '维修', code: 'ENGINEER_LEAD', department: '工程维修部', scope: '报修工单、派单处理、楼栋巡检和设备维护' },
  { name: '物业人员', code: 'SERVICE_MANAGER', department: '客户服务部', scope: '住户档案、公告活动、报修跟进和日常服务' },
]

export function roleName(roleCode) {
  return ROLE_NAMES[roleCode] || roleCode || '未分配角色'
}

export function canAccessModule(user, moduleKey) {
  if (!user?.roleCode) return false
  return ROLE_MODULES[user.roleCode]?.includes(moduleKey) || false
}
