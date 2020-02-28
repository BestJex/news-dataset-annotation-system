import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/api/user/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function save(s) {
  return request({
    url: '/api/user/save',
    method: 'post',
    data: s,
    headers: {
      'Content-Type': 'application/html; charset=utf-8'
    }
  })
}

export function getUserInfo() {
  return request({
    url: '/api/user/info',
    method: 'get'
  })
}

export function logout(token) {
  return request({
    url: '/logout',
    method: 'post',
    data: {
      token
    }
  })
}
