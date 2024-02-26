import API from '@/api'

const mySpaceStore = {
  namespaced: true,
  state () {
    return {
      editableTabs: [],
      pageParams: {
        gid: null,
        currentPage: 1,
        size: 15,
        orderTag: null
      },
      selectedIndex: 0,
      totalNums: 0,
      tableData: [],
      nums: 0,
      isRecycleBin: false,
      isEditGroup: false,
      isAddSmallLink: false,
      isAddSmallLinks: false,
      editGroup: {
        gid: null,
        groupName: ''
      },
      recycleBinNums: 0,
      orderIndex: 0,
    }
  },
  mutations: {
    changeSelectedIndex (state, changedTo) {
      state.selectedIndex = changedTo
    },
    setEditableTabs (state, groups) {
      state.editableTabs = groups
    },
    setTableData (state, data) {
      state.tableData = data
    },
    setTotalNums (state, totalNums) {
      state.totalNums = totalNums
    },
    setNums (state, nums) {
      state.nums = nums
    },
    updatePageParamsGID (state, gid) {
      state.pageParams.gid = gid
    },
    updatePageParams (state, payload) {
      Object.keys(payload).forEach(key => {
        if (key in state.pageParams) {
          state.pageParams[key] = payload[key];
        }
      })
    },
    setSelectedIndex (state, index) {
      state.selectedIndex = index
    },
    setIsRecycleBin (state, changedTo) {
      state.isRecycleBin = changedTo
    },
    setIsEditGroup (state, changedTo) {
      state.isEditGroup = changedTo
    },
    setEditGroup (state, group) {
      state.editGroup.gid = group.gid
      state.editGroup.groupName = group.groupName
    },
    setRecycleBinNums (state, num) {
      state.recycleBinNums = num
    },
    setIsAddSmallLink (state, changedTo) {
      state.isAddSmallLink = changedTo
    },
    setIsAddSmallLinks (state, changedTo) {
      state.isAddSmallLinks = changedTo
    },
    setOrderIndex (state, changedTo) {
      state.orderIndex = changedTo
    }
  },
  actions: {
    async addGroup({ dispatch }, { groupName }) {
      const res = await API.group.addGroup({ groupName });
      if (res?.data.success) {
        console.log("see addGroup success: ", res)
        await dispatch('getGroupInfo');
      } else {
        throw new Error(res.data.message);
      }
    },
    async getGroupInfo({ commit }) {
      const res = await API.group.queryGroup()
      if (res?.data.success) {
        console.log('see getGroupInfo success: ', res)
        const groups = res.data.data.reverse()
        commit('setEditableTabs', groups);
      } else {
        throw new Error(res.data.message);
      }
    },
    async queryPage({ state, commit }) {
      const { editableTabs, selectedIndex, pageParams } = state;
      const selectedTab = editableTabs[selectedIndex]
      if (selectedTab) {
        commit('updatePageParamsGID', selectedTab.gid)
        commit('setNums', selectedTab.count || 0)
        console.log('------', editableTabs, selectedIndex)
        const res = await API.link.queryPage(pageParams)
        if (res?.data.success) {
          commit('setTableData', res.data?.data?.content)
          commit('setTotalNums', +res.data?.data?.totalElements)
        } else {
          throw new Error(res.data.message);
        }
      }
    },
    async refreshPage({ dispatch }) {
      await dispatch('getGroupInfo')
      await dispatch('queryPage') 
    },
    async deleteGroup({ commit, dispatch }, gid) {
      const res = await API.group.deleteGroup({ gid })
      if (res?.data.success) {
        commit('setSelectedIndex', 0)
        await dispatch('refreshPage')
      } else {
        throw new Error(res.data.message)
      }
    },
    async sortGroup(state, formData) {
      const res = await API.group.sortGroup(formData)
      if (!res?.data.success) {
        throw new Error(res.data.message)
      }
    },
  }
}

export default mySpaceStore