import request from '../utils/request'

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
