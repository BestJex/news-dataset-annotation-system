const tokens = {
  admin: {
    token: 'admin'
  },
  editor: {
    token: 'editor'
  }
}

const users = {
  'admin': {
    role: 'admin',
    introduction: 'I am a super administrator',
    avatar: 'https://hezepeng-1252705718.cos.ap-guangzhou.myqcloud.com/icon/1%20(17).jpg',
    name: 'Super Admin'
  },
  'editor': {
    role: 'editor',
    introduction: 'I am an editor',
    avatar: 'https://hezepeng-1252705718.cos.ap-guangzhou.myqcloud.com/icon/1%20(64).jpg',
    name: 'Normal Editor'
  }
}

export default [
  // user login
  {
    url: '/login',
    type: 'post',
    response: config => {
      const { username } = config.body
      console.log(config.body)
      const token = tokens[username]
      // mock error
      if (!token) {
        return {
          status: 10,
          msg: '用户名密码错误'
        }
      }

      return {
        status: 0,
        data: token,
        msg: '登陆成功，欢迎回来!'
      }
    }
  },

  // get user info
  {
    url: '/getUserInfo',
    type: 'get',
    response: config => {
      console.log(config.headers)
      const token = config.headers['x-token']
      const info = users[token]

      // mock error
      if (!info) {
        return {
          status: 10,
          msg: 'Login failed, unable to get user details.'
        }
      }

      return {
        status: 0,
        data: info
      }
    }
  },

  // user logout
  {
    url: '/logout',
    type: 'post',
    response: _ => {
      return {
        status: 0,
        data: 'success'
      }
    }
  }
]
