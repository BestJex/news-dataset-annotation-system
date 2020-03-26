import request from '@/utils/request'
import sk from 'element-ui/src/locale/lang/sk'

export function saveAnnotation(annotation) {
  return request({
    url: '/annotation/add',
    method: 'post',
    data: annotation
  })
}

export function getAnnotationListByUsername() {
  return request({
    url: '/annotation/task/list',
    method: 'get'
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
    url: '/annotation/id/list',
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

export function createTask(task) {
  return request({
    url: '/annotation/task/create',
    method: 'post',
    data: task
  })
}

export function checkAnnotation() {
  return request({
    url: '/annotation/check',
    method: 'post'
  })
}

export function createFoundationSkill(skill) {
  return request({
    url: '/annotation/task/machine/skill/create/' + skill,
    method: 'get'
  })
}

export function getOrSetMachineLearningModel() {
  return request({
    url: '/annotation/task/machine/model/init',
    method: 'post'
  })
}

export function getAndUpdateMachineLearningTaskState() {
  return request({
    url: '/annotation/task/machine/state/get',
    method: 'post'
  })
}

export function createModelInfo(modelName, modelIntroduction, modelFileUrl) {
  return request({
    url: '/annotation/task/machine/model/info',
    data: {
      modelName, modelIntroduction, modelFileUrl
    },
    method: 'post'
  })
}
