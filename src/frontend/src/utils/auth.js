const TOKEN_KEY = 'property-demo-token'
const USER_KEY = 'property-demo-user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getUser() {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null

  try {
    return JSON.parse(raw)
  } catch {
    clearAuth()
    return null
  }
}

export function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function hasValidUser() {
  const user = getUser()
  return Boolean(user?.id && user?.roleCode)
}

export function clearAuth() {
  removeToken()
  localStorage.removeItem(USER_KEY)
}
