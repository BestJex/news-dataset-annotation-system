import request from '@/utils/request'

export function addArticle(article) {
  return request({
    url: '/annotation/addArticle',
    method: 'post',
    data: {
      article
    }
  })
}

export function updateArticle(article) {
  return request({
    url: '/annotation/updateArticle',
    method: 'post',
    data: {
      article
    }
  })
}

export function deleteArticle(article) {
  return request({
    url: '/annotation/deleteArticle',
    method: 'post',
    data: {
      article
    }
  })
}

export function getArticleList() {
  return request({
    url: '/annotation/getArticleList',
    method: 'get'
  })
}
