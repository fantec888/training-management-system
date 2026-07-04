import request from '../utils/request'

export function fetchParking() {
  return request.get('/api/parking')
}

export function fetchSecurity() {
  return request.get('/api/security')
}

export function createAccessControl(data) {
  return request.post('/api/access-controls', data)
}

export function updateAccessControl(id, data) {
  return request.put(`/api/access-controls/${id}`, data)
}

export function updateAccessStatus(id, status) {
  return request.patch(`/api/access-controls/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deleteAccessControl(id) {
  return request.delete(`/api/access-controls/${id}`)
}

export function createPatrol(data) {
  return request.post('/api/patrols', data)
}

export function updatePatrol(id, data) {
  return request.put(`/api/patrols/${id}`, data)
}

export function finishPatrol(id, data) {
  return request.patch(`/api/patrols/${id}/finish`, data)
}

export function deletePatrol(id) {
  return request.delete(`/api/patrols/${id}`)
}
