import apiClient from '@/api/axios'
export default {
  queryPage(data) {
    return apiClient({
      url: '/page',
      method: 'get',
      params: data
    })
  },
  addSmallLink(data) {
    return apiClient({
      url: '/create',
      method: 'post',
      data
    })
  },
  editSmallLink(data) {
    return apiClient({
      url: '/update',
      method: 'post',
      data
    })
  },
  // 通过链接查询标题
  queryTitle(data) {
    return apiClient({
      method: 'get',
      url: '/title',
      params: data
    })
  },
  // 移动到回收站
  toRecycleBin(data) {
    return apiClient({
      url: '/recycle-bin/save',
      method: 'post',
      data
    })
  },
  // 查询回收站数据
  queryRecycleBin(data) {
    return apiClient({
      url: '/recycle-bin/page',
      method: 'get',
      params: data
    })
  },
  // 恢复短链接
  recoverLink(data) {
    return apiClient({
      method: 'post',
      url: '/recycle-bin/recover',
      data
    }) 
  },
  removeLink(data) {
    return apiClient({
      method: 'post',
      url: '/recycle-bin/remove',
      data
    })
  },
  queryLinkStats(data) {
    return apiClient({
      method: 'get',
      params: data,
      url: 'stats'
    })
  }
}
