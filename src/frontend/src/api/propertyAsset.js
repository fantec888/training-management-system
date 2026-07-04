import request from '../utils/request'

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
