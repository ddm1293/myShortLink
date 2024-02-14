import apiClient from '@/api/axios'

export default {
    // register a new user
    addUser(data) {
      return apiClient({
        url: '/user',
        method: 'post',
        data
      })
    },

    // update user information
    editUser(data) {
      return apiClient({
        url: '/user',
        method: 'put',
        data
      })
    },

    // user login
    login(data) {
      return apiClient({
        url: '/user/login',
        method: 'post',
        data
      })
    },

    // user logout
    logout(data) {
      return apiClient({
        url: '/user/logout?token=' + data.token + '&username=' + data.username,
        method: 'delete'
      })
    },

    // check username existentce
    hasUsername(data) {
      return apiClient({
        url: '/user/checkUsernameExistence',
        method: 'get',
        params: data
      })
    },

    // fetch user based on username
    queryUserInfo(data) {
      return apiClient({
        url: '/user/' + data,
        method: 'get'
      })
    }
  }
  