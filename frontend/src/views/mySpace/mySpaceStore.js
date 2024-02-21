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
      nums: 0
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
    }
  }
}

export default mySpaceStore