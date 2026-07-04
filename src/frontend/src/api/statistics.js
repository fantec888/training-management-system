import request from '../utils/request'

export function fetchAdvancedStatistics() {
  return request.get('/api/statistics/advanced')
}
