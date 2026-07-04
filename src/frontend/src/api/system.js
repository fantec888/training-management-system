import request from '../utils/request'

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

export function deleteSystemUser(id) {
  return request.delete(`/api/system-users/${id}`)
}

export function fetchCurrentRbac() {
  return request.get('/api/rbac/current')
}

export function fetchRoles() {
  return request.get('/api/rbac/roles')
}

export function createRole(data) {
  return request.post('/api/rbac/roles', data)
}

export function updateRole(id, data) {
  return request.put(`/api/rbac/roles/${id}`, data)
}

export function deleteRole(id) {
  return request.delete(`/api/rbac/roles/${id}`)
}

export function fetchPermissions() {
  return request.get('/api/rbac/permissions')
}

export function createPermission(data) {
  return request.post('/api/rbac/permissions', data)
}

export function updatePermission(id, data) {
  return request.put(`/api/rbac/permissions/${id}`, data)
}

export function deletePermission(id) {
  return request.delete(`/api/rbac/permissions/${id}`)
}

export function fetchUserRoles(userId) {
  return request.get(`/api/rbac/users/${userId}/roles`)
}

export function assignUserRoles(userId, roleCodes) {
  return request.put(`/api/rbac/users/${userId}/roles`, { roleCodes })
}

export function fetchRolePermissions(roleId) {
  return request.get(`/api/rbac/roles/${roleId}/permissions`)
}

export function assignRolePermissions(roleId, permissionCodes) {
  return request.put(`/api/rbac/roles/${roleId}/permissions`, { permissionCodes })
}
