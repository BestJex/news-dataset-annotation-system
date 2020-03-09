import request from '@/utils/request'

export function addAnnotation(annotation) {
  return request({
    url: '/annotation/add',
    method: 'post',
    data: {
      annotation
    }
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

export function getAnnotation(no) {
  return request({
    url: '/annotation/get',
    method: 'get',
    params: { no }
  })
}
