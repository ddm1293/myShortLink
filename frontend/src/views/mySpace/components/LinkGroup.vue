<template>
  <div class="options-box">
      <AddLinkGroup />

      <!-- 拖动选项 -->
      <ul class="sortOptions">
        <li v-for="(item, index) in editableTabs" :key="item.gid">
          <LinkGroupItem :item="item" :index="index"/>
        </li>
      </ul>
      <div class="recycle-bin">
        <!-- 当selectIndex等于-1时代表选中的是回收站 -->
        <div class="recycle-box hover-box" :class="{ selectedItem: selectedIndex === -1 }" @click="recycleBin">
          回收站
          <el-icon style="margin-left: 5px; font-size: 20px">
            <Delete />
          </el-icon>
        </div>
      </div>
  </div>

</template>

<script setup>
import { ref, reactive, computed, getCurrentInstance } from 'vue'
import AddLinkGroup from './AddLinkGroup.vue'
import LinkGroupItem from './LinkGroupItem.vue'
const { proxy } = getCurrentInstance()

const editableTabs = computed(() => proxy.$store.state.mySpace.editableTabs)
const selectedIndex = computed(() => proxy.$store.state.mySpace.selectedIndex)

const recycleBin = () => {
  // isRecycleBin.value = true
  // selectedIndex.value = -1 
  // pageParams.current = 1
  // pageParams.size = 15

  // queryRecycleBinPage()
}

(async function fetchGroupsUponInit() {
  proxy.$store.dispatch('mySpace/refreshPage')
})()
</script>

<style lang="scss" scoped>

.options-box {
  display: flex;
  flex-direction: column;
  position: relative;
  height: 100%;
  width: 190px;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

.recycle-bin {
  position: absolute;
  display: flex;
  bottom: 0;
  height: 50px;
  width: 100%;
}

.recycle-box {
  flex: 1;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.sortOptions {
  height: calc(100% - 50px);
  margin-bottom: 50px;
  // height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>