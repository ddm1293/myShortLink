<template>
  <div class="item-box flex-box hover-box" :class="{ selectedItem: selectedIndex === index }"
    @click="changeSelectIndex(index)">
    <div style="display: flex">
      <img src="@/assets/svg/移动竖.svg" width="13" style="margin-right: 3px" alt="" />
      <span class="over-text">{{ item.groupName }}</span>
    </div>
    <div class="flex-box">
      <!-- 图标 -->
      <el-tooltip :show-after="500" class="box-item" effect="dark" :content="'查看图表'" placement="bottom-end">
        <!-- 传group是为了表示这个请求是查询分组图表数据 -->
        <el-icon v-show="!(item.count === 0 || item.count === null)" class="edit"
          :class="{ zero: item.count === 0 }"
          @click="chartsVisible({ describe: item.groupName, gid: item.gid, group: true })">
          <Histogram />
        </el-icon>
      </el-tooltip>
      <!-- 编辑按钮 -->
      <el-dropdown>
        <div class="block">
          <el-icon class="edit" v-if="item.groupName !== 'DEFAULT'">
            <Tools />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="showEditGroup(item.gid, item.groupName)">编辑</el-dropdown-item>
            <el-dropdown-item @click="deleteGroup(item.gid)">删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <span class="item-length">{{ item.count ?? 0 }}</span>
    </div>
  </div>
</template>

<script setup>
  import { ElMessage } from 'element-plus'
  import { computed, getCurrentInstance } from 'vue'
  const { proxy } = getCurrentInstance()
  const { item, index } = defineProps({
    item: Object,
    index: Number
  })

  const selectedIndex = computed(() => proxy.$store.state.mySpace.selectedIndex)

  const changeSelectIndex = (index) => {
    proxy.$store.commit('mySpace/setSelectedIndex', index)
    proxy.$store.commit('mySpace/setIsRecycleBin', false)
  }

  const chartsVisible = () => {}

  const showEditGroup = (gid, groupName) => {
    proxy.$store.commit('mySpace/setIsEditGroup', true)
    proxy.$store.commit('mySpace/setEditGroup', { gid, groupName })
  }

  const deleteGroup = async (gid) => {
    try {
      await proxy.$store.dispatch('mySpace/deleteGroup', gid)
      ElMessage.success('删除成功')
    } catch (err) {
      console.log('fail deleteGroup: ', err)
      ElMessage.error(err.message)
    }
  }
</script>

<style lang="scss" scoped>
.item-box {
  height: 43px;
  width: 190px;
  font-family:
    PingFangSC-Semibold,
    PingFang SC;
  font-weight: 520;
}

.item-box:hover {
  .flex-box {
    .edit {
      display: block;
    }

    .item-length {
      display: none !important;
    }
  }
}

.recycle-bin {
  position: absolute;
  display: flex;
  bottom: 0;
  height: 50px;
  width: 100%;
}

.edit {
  display: none;
  margin-left: 5px;
  color: rgb(83, 97, 97);
  font-size: 20px;
}

.edit:hover {
  color: #2991ce;
  cursor: pointer;
}

.zero {
  color: rgb(83, 97, 97) !important;
}

// 提示框样式
.tooltip-base-box {
  width: 600px;
}

.tooltip-base-box .row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tooltip-base-box .center {
  justify-content: center;
}

.tooltip-base-box .box-item {
  width: 110px;
}

.selectedItem {
  color: #3464e0 !important;
  background-color: #ebeffa !important;
  font-weight: 600 !important;
}

.block:hover {
  color: rgb(121, 187, 255);

  .el-icon {
    color: rgb(121, 187, 255) !important;
  }
}

.over-text {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box; //作为弹性伸缩盒子模型显示。
  -webkit-box-orient: vertical; //设置伸缩盒子的子元素排列方式--从上到下垂直排列
  -webkit-line-clamp: 1; //显示的行
}

</style>