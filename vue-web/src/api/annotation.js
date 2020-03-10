import request from '@/utils/request'

export function saveAnnotation(annotation) {
  return request({
    url: '/api/annotation/add',
    method: 'post',
    data: annotation
  })
}

export function updateAnnotation(annotation) {
  return request({
    url: '/api/annotation/update',
    method: 'post',
    data: {
      annotation
    }
  })
}

export function deleteAnnotation(annotation) {
  return request({
    url: '/api/annotation/delete',
    method: 'post',
    data: {
      annotation
    }
  })
}

export function getAnnotationIdList() {
  return request({
    url: '/api/annotation/getAnnotationIdList',
    method: 'get'
  })
}

export function getAnnotation(id) {
  return request({
    url: '/api/annotation/get/' + id,
    method: 'get'
  })
}

export function initMongo() {
  return request({
    url: '/api/mongo/init',
    method: 'get'
  })
}
