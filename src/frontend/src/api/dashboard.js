import request from '../utils/request'

export function fetchDashboard() {
  return request.get('/api/dashboard')
}
