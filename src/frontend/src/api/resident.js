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
