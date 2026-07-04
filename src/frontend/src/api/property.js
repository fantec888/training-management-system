import request from '../utils/request'

export function fetchResidents() {
  return request.get('/api/residents')
}

export function createResident(data) {
  return request.post('/api/residents', data)
}

export function updateResident(id, data) {
  return request.put(`/api/residents/${id}`, data)
}

export function updateResidentStatus(id, status) {
  return request.patch(`/api/residents/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deleteResident(id) {
  return request.delete(`/api/residents/${id}`)
}

export function fetchCommunities() {
  return request.get('/api/communities')
}

export function createCommunity(data) {
  return request.post('/api/communities', data)
}

export function updateCommunity(id, data) {
  return request.put(`/api/communities/${id}`, data)
}

export function deleteCommunity(id) {
  return request.delete(`/api/communities/${id}`)
}

export function fetchProperties() {
  return request.get('/api/properties')
}

export function createBuilding(data) {
  return request.post('/api/buildings', data)
}

export function updateBuilding(id, data) {
  return request.put(`/api/buildings/${id}`, data)
}

export function deleteBuilding(id) {
  return request.delete(`/api/buildings/${id}`)
}

export function createRoom(data) {
  return request.post('/api/rooms', data)
}

export function updateRoom(id, data) {
  return request.put(`/api/rooms/${id}`, data)
}

export function deleteRoom(id) {
  return request.delete(`/api/rooms/${id}`)
}

export function bindRoomResident(id, data) {
  const params = new URLSearchParams()
  if (data.residentName) params.set('residentName', data.residentName)
  if (data.status) params.set('status', data.status)
  return request.patch(`/api/rooms/${id}/resident?${params.toString()}`)
}

export function fetchRepairs() {
  return request.get('/api/repairs')
}

export function createRepair(data) {
  return request.post('/api/repairs', data)
}

export function updateRepairProgress(id, data) {
  return request.patch(`/api/repairs/${id}/progress`, data)
}

export function deleteRepair(id) {
  return request.delete(`/api/repairs/${id}`)
}

export function fetchComplaints() {
  return request.get('/api/complaints')
}

export function createComplaint(data) {
  return request.post('/api/complaints', data)
}

export function replyComplaint(id, data) {
  return request.patch(`/api/complaints/${id}/reply`, data)
}

export function deleteComplaint(id) {
  return request.delete(`/api/complaints/${id}`)
}

export function fetchBilling() {
  return request.get('/api/billing')
}

export function createBill(data) {
  return request.post('/api/billing', data)
}

export function updateBillStatus(id, status) {
  return request.patch(`/api/billing/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deleteBill(id) {
  return request.delete(`/api/billing/${id}`)
}

export function fetchFeeItems() {
  return request.get('/api/fee-items')
}

export function createFeeItem(data) {
  return request.post('/api/fee-items', data)
}

export function updateFeeItem(id, data) {
  return request.put(`/api/fee-items/${id}`, data)
}

export function deleteFeeItem(id) {
  return request.delete(`/api/fee-items/${id}`)
}

export function createPayment(data) {
  return request.post('/api/payments', data)
}

export function fetchParking() {
  return request.get('/api/parking')
}

export function fetchNotices() {
  return request.get('/api/notices')
}

export function createNotice(data) {
  return request.post('/api/notices', data)
}

export function updateNoticeStatus(id, status) {
  return request.patch(`/api/notices/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deleteNotice(id) {
  return request.delete(`/api/notices/${id}`)
}

export function fetchSystemUsers() {
  return request.get('/api/system-users')
}

export function createSystemUser(data) {
  return request.post('/api/system-users', data)
}

export function updateSystemUser(id, data) {
  return request.put(`/api/system-users/${id}`, data)
}

export function updateSystemUserStatus(id, enabled) {
  return request.patch(`/api/system-users/${id}/status?enabled=${enabled}`)
}

export function deleteSystemUser(id) {
  return request.delete(`/api/system-users/${id}`)
}

export function fetchCurrentRbac() {
  return request.get('/api/rbac/current')
}

export function fetchRoles() {
  return request.get('/api/rbac/roles')
}

export function createRole(data) {
  return request.post('/api/rbac/roles', data)
}

export function updateRole(id, data) {
  return request.put(`/api/rbac/roles/${id}`, data)
}

export function deleteRole(id) {
  return request.delete(`/api/rbac/roles/${id}`)
}

export function fetchPermissions() {
  return request.get('/api/rbac/permissions')
}

export function createPermission(data) {
  return request.post('/api/rbac/permissions', data)
}

export function updatePermission(id, data) {
  return request.put(`/api/rbac/permissions/${id}`, data)
}

export function deletePermission(id) {
  return request.delete(`/api/rbac/permissions/${id}`)
}

export function fetchUserRoles(userId) {
  return request.get(`/api/rbac/users/${userId}/roles`)
}

export function assignUserRoles(userId, roleCodes) {
  return request.put(`/api/rbac/users/${userId}/roles`, { roleCodes })
}

export function fetchRolePermissions(roleId) {
  return request.get(`/api/rbac/roles/${roleId}/permissions`)
}

export function assignRolePermissions(roleId, permissionCodes) {
  return request.put(`/api/rbac/roles/${roleId}/permissions`, { permissionCodes })
}
