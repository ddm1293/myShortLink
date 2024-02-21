import apiClient from '@/api/axios'
const urlPrefix = '/admin/remote'
export default {
  queryPage(data) {
    return apiClient({
      url: urlPrefix + '/link/page',
      method: 'get',
      params: data
    })
  },
  addSmallLink(data) {
    return apiClient({
      url: urlPrefix + '/link/create',
      method: 'post',
      data
    })
  },
  editSmallLink(data) {
    return apiClient({
      url: urlPrefix + '/link/update',
      method: 'post',
      data
    })
  },
  // 通过链接查询标题
  queryTitle(data) {
    return apiClient({
      url: urlPrefix + '/link/title',
      method: 'get',
      params: data
    })
  },
  // 移动到回收站
  toRecycleBin(data) {
    return apiClient({
      url: urlPrefix + '/recycleBin/save',
      method: 'post',
      data
    })
  },
  // 查询回收站数据
  queryRecycleBin(data) {
    return apiClient({
      url: urlPrefix + '/recycleBin/page',
      method: 'get',
      params: data
    })
  },
  // 恢复短链接
  recoverLink(data) {
    return apiClient({
      url: urlPrefix + '/recycleBin/recover',
      method: 'post',
      data
    }) 
  },
  removeLink(data) {
    return apiClient({
      url: urlPrefix + '/recycleBin/remove',
      method: 'post',
      data
    })
  },
  queryLinkStats(data) {
    return apiClient({
      url: urlPrefix + '/link/stats',
      method: 'get',
      params: data,
    })
  }
}
