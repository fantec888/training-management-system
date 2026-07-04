import request from '../utils/request'

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
