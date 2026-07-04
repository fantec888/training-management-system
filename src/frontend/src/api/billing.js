import request from '../utils/request'

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
