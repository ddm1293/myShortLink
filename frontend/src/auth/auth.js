import Cookies from 'js-cookie'

const tokenKey = 'token'

export function getToken() {
  return Cookies.get(tokenKey)
}

export function getUsername() {
  return Cookies.get('username')
}

export function setToken(token) {
  return Cookies.set(tokenKey, token)
}

export function setUsername(username) {
  return Cookies.set('username', username)
}

export function removeKey() {
  return Cookies.remove(tokenKey)
}

export function removeUsername() {
  return Cookies.remove('username')
}