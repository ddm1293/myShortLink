import { createStore } from 'vuex'
import mySpaceStore from '../views/mySpace/mySpaceStore'

const store = createStore({
  modules: {
    mySpace: mySpaceStore
  }
})

export default store
