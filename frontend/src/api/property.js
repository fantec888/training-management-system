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

export function fetchProperties() {
  return request.get('/api/properties')
}

export function createBuilding(data) {
  return request.post('/api/buildings', data)
}

export function updateBuilding(id, data) {
  return request.put(`/api/buildings/${id}`, data)
}

export function createRoom(data) {
  return request.post('/api/rooms', data)
}

export function updateRoom(id, data) {
  return request.put(`/api/rooms/${id}`, data)
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

export function fetchBilling() {
  return request.get('/api/billing')
}

export function createBill(data) {
  return request.post('/api/billing', data)
}

export function updateBillStatus(id, status) {
  return request.patch(`/api/billing/${id}/status?status=${encodeURIComponent(status)}`)
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
