<template>
    <div class="content-box" >
      <div class="table-box">
        <!-- 默认展示创建短链输入框和按钮 -->
        <div v-if="!isRecycleBin" class="buttons-box">
          <div style="width: 100%; display: flex">
            <!-- <el-input style="flex: 1; margin-right: 20px" placeholder="请输入http://或https://开头的连接或引用跳转程序"></el-input> -->
            <el-button class="addButton" type="primary" style="width: 130px; margin-right: 10px" @click="openAddLink">
              创建短链
            </el-button>
            <el-button style="width: 130px; margin-right: 10px" @click="openAddLinks">
              批量创建
            </el-button>
          </div>
        </div>
        <!-- 展示回收站信息 -->
        <div v-else class="recycle-bin-box">
          <span>回收站</span>
          <span>一共{{ recycleBinNums }}条短链接</span>
        </div>
        <!-- 表格展示区域 -->
        <ContentTable />
        <!-- 分页器 -->
        <div class="pagination-block">
          <el-pagination v-model:current-page="pageParams.currentPage" v-model:page-size="pageParams.size"
            :page-sizes="[10, 15, 20, 30]" layout="total, sizes, prev, pager, next, jumper" :total="totalNums"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
      </div>
    </div>
</template>

<script setup>
import { computed, getCurrentInstance } from 'vue'
import ContentTable from './ContentTable.vue'
const { proxy } = getCurrentInstance()

const isRecycleBin = computed(() => proxy.$store.state.mySpace.isRecycleBin)
const recycleBinNums = computed(() => proxy.$store.state.mySpace.recycleBinNums)
const pageParams = computed(() => proxy.$store.state.mySpace.pageParams)
const totalNums = computed(() => proxy.$store.state.mySpace.totalNums)

const openAddLink = () => {
  proxy.$store.commit('mySpace/setIsAddSmallLink', true)
  proxy.$store.commit('mySpace/setIsAddSmallLinks', false)
}

const openAddLinks = () => {
  proxy.$store.commit('mySpace/setIsAddSmallLinks', true)
  proxy.$store.commit('mySpace/setIsAddSmallLink', false)
}

const handleSizeChange = () => {
//  !isRecycleBin.value ? queryPage() : queryRecycleBinPage()
}

const handleCurrentChange = () => {
//  !isRecycleBin.value ? queryPage() : queryRecycleBinPage()
}
</script>

<style lang="scss" scoped>
  .block:hover {
    color: rgb(121, 187, 255);

    .el-icon {
      color: rgb(121, 187, 255) !important;
    }
  }
  
  .table-edit {
    font-size: 20px;
    margin-right: 20px;
    color: #3677c2;
    cursor: pointer;
  }

  .table-edit:hover {
    color: #98cafe;
  }

  .content-box {
    flex: 1;
    padding: 16px;
    background-color: #eef0f5;
    position: relative;

    .table-box {
      background-color: #ffffff;
      height: 100%;

      .buttons-box {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16px;
      }

      .pagination-block {
        position: absolute;
        bottom: 4%;
        left: 50%;
        transform: translate(-50%, 0);
      }

      .recycle-bin-box {
        height: 64px;
        display: flex;
        align-items: center;
        padding-left: 16px;

        span:nth-child(1) {
          font-size: 20px;
          margin-right: 5px;
        }
      }
    }
  }

  .table-link-box {
    display: flex;
    align-items: center;

    .name-date {
      display: flex;
      flex-direction: column;
      margin-left: 10px;

      span:nth-child(1) {
        font-size: 15px;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box; //作为弹性伸缩盒子模型显示。
        -webkit-box-orient: vertical; //设置伸缩盒子的子元素排列方式--从上到下垂直排列
        -webkit-line-clamp: 1; //显示的行
      }

      .time {
        display: flex;
        align-items: center;

        span {
          font-size: 12px;
        }

        img {
          margin-left: 10px;
        }

        div {
          border: 1.5px solid rgb(253, 81, 85);
          border-radius: 8px;
          line-height: 20px;
          font-size: 12px;
          transform: scale(0.7);
          color: rgb(253, 81, 85);
          padding: 0 4px;
          background-color: rgba(250, 210, 211);

          span {
            font-weight: bolder;
          }
        }
      }
    }
  }

  .isExpire {
    .name-date {
      span:nth-child(1) {
        color: rgba(0, 0, 0, 0.3);
      }

      .time {
        div {
          span {
            font-weight: bolder;
            color: red;
          }
        }
      }
    }
  }

  .table-url-box {
    display: flex;
    flex-direction: column;
    align-items: flex-start;

    span {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box; //作为弹性伸缩盒子模型显示。
      -webkit-box-orient: vertical; //设置伸缩盒子的子元素排列方式--从上到下垂直排列
      -webkit-line-clamp: 1; //显示的行
      color: rgba(0, 0, 0, 0.4);
    }
  }

  .times-box {
    display: flex;
    flex-direction: column;

    .today-box {
      span {
        font-size: 13px;
        font-weight: 600;
        margin-right: 10px;
      }

      span:nth-child(1) {
        font-weight: 400;
        color: rgba(0, 0, 0, 0.4);
      }
    }

    .total-box {
      span {
        font-size: 13px;
        font-weight: 400;
        margin-right: 10px;
      }

      span:nth-child(1) {
        font-weight: 400;
        color: rgba(0, 0, 0, 0.4);
      }
    }
  }

  .copy-url {
    margin-left: 10px;
  }

  .orderIndex {
    color: #3677c2;
  }
</style>