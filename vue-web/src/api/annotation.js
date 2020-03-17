import request from '@/utils/request'

export function saveAnnotation(annotation) {
  return request({
    url: '/annotation/add',
    method: 'post',
    data: annotation
  })
}

export function getAnnotationListByUsername(username) {
  return request({
    url: '/annotation/list',
    method: 'get',
    params: { username }
  })
}

export function updateAnnotation(annotation) {
  return request({
    url: '/annotation/update',
    method: 'post',
    data: {
      annotation
    }
  })
}

export function deleteAnnotation(annotation) {
  return request({
    url: '/annotation/delete',
    method: 'post',
    data: {
      annotation
    }
  })
}

export function getAnnotationIdList() {
  return request({
    url: '/annotation/getAnnotationIdList',
    method: 'get'
  })
}

export function getAnnotation(id) {
  return request({
    url: '/annotation/get/' + id,
    method: 'get'
  })
}

export function initMongo() {
  return request({
    url: '/mongo/init',
    method: 'get'
  })
}
