import request from '@/utils/request'

export function addUser(user) {
  return request({
    url: '/user/register',
    method: 'post',
    data: user
  })
}

export function updateUser(user) {
  return request({
    url: '/user/update',
    method: 'post',
    data: user
  })
}

export function deleteUser(username) {
  return request({
    url: '/user/delete',
    method: 'post',
    data: { username }
  })
}

export function getUserList() {
  return request({
    url: '/user/list',
    method: 'get'
  })
}

export function checkPassword(password) {
  return request({
    url: '/user/checkPassword',
    method: 'post',
    data: {
      password
    }
  })
}

export function changePassword(password) {
  return request({
    url: '/user/changePassword',
    method: 'post',
    data: {
      password
    }
  })
}

export function checkUsername(username) {
  return request({
    url: '/user/checkUsername',
    method: 'post',
    data: {
      username
    }
  })
}

