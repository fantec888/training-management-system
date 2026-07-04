import request from '../utils/request'

export function fetchSmartServices() {
  return request.get('/api/smart-services')
}

export function createActivity(data) {
  return request.post('/api/activities', data)
}

export function updateActivity(id, data) {
  return request.put(`/api/activities/${id}`, data)
}

export function updateActivityStatus(id, status) {
  return request.patch(`/api/activities/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deleteActivity(id) {
  return request.delete(`/api/activities/${id}`)
}

export function createPackage(data) {
  return request.post('/api/packages', data)
}

export function updatePackage(id, data) {
  return request.put(`/api/packages/${id}`, data)
}

export function updatePackageStatus(id, status) {
  return request.patch(`/api/packages/${id}/status?status=${encodeURIComponent(status)}`)
}

export function deletePackage(id) {
  return request.delete(`/api/packages/${id}`)
}
