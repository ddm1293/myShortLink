import apiClient from '@/api/axios'
export default {
  queryGroup(data) {
    return apiClient({
      url: '/group',
      method: 'get',
      params: data
    })
  },

  addGroup(data) {
    return apiClient({
      url: '/group',
      method: 'post',
      data
    })
  },

  editGroup(data) {
    return apiClient({
      url: '/group',
      method: 'put',
      data
    })
  },

  deleteGroup(data) {
    return apiClient({
      url: '/group',
      method: 'delete',
      params: data
    })
  },
  
  sortGroup(data) {
    return apiClient({
      url: '/group/sort',
      method: 'post',
      data
    })
  }
}