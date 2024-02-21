<template>
    <div class="content-box">
      <div class="table-box">
        <!-- 默认展示创建短链输入框和按钮 -->
        <div v-if="!isRecycleBin" class="buttons-box">
          <div style="width: 100%; display: flex">
            <!-- <el-input style="flex: 1; margin-right: 20px" placeholder="请输入http://或https://开头的连接或引用跳转程序"></el-input> -->
            <el-button class="addButton" type="primary" style="width: 130px; margin-right: 10px"
              @click="isAddSmallLink = true">创建短链</el-button>
            <el-button style="width: 130px; margin-right: 10px" @click="isAddSmallLinks = true">批量创建</el-button>
          </div>
        </div>
        <!-- 展示回收站信息 -->
        <div v-else class="recycle-bin-box">
          <span>回收站</span>
          <span>一共{{ recycleBinNums }}条短链接</span>
        </div>
        <!-- 表格展示区域 -->
        <el-table :data="tableData" height="calc(100vh - 240px)" style="width: calc(100vw - 230px)"
          :header-cell-style="{ background: '#f7f8fa', color: '#606266' }">
          <!-- 数据为空时展示的内容 -->
          <template #empty>
            <div style="height: 60vh; display: flex; align-items: center; justify-content: center">
              暂无链接
            </div>
          </template>
          <el-table-column type="selection" width="35" />
          <el-table-column label="短链接信息" prop="info" min-width="300">
            <template #header>
              <el-dropdown>
                <div :class="{ orderIndex: orderIndex === 0 }" class="block" style="margin-top: 3px">
                  <span>短链接信息</span>
                  <el-icon>
                    <CaretBottom />
                  </el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-item @click="pageParams.orderTag = null, orderIndex = 0">创建时间</el-dropdown-item>
                </template>
              </el-dropdown>
            </template>
            <template #default="scope">
              <div class="table-link-box" :class="{
                isExpire: scope?.row?.validDateType === 1 && !isExpire(scope?.row?.validDate)
              }">
                <img :src="getImgUrl(scope.row.favicon)" :key="scope?.row?.id" width="20" height="20" alt="" />
                <div class="name-date">
                  <el-tooltip show-after="500" :content="scope.row.describe">
                    <span>{{ scope.row.describe }}</span>
                  </el-tooltip>
                  <div class="time" style="display: flex">
                    <span>{{ scope.row.createTime }}</span>
                    <el-tooltip show-after="500" v-if="scope?.row?.validDate" :content="'到期时间：' + scope?.row?.validDate">
                      <img v-if="isExpire(scope?.row?.validDate)" width="18" height="18" src="@/assets/png/沙漏倒计时.png"
                        alt="" />
                      <div v-else><span>已失效</span></div>
                    </el-tooltip>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="短链接网址" prop="url" min-width="300">
            <template #default="scope">
              <div class="table-url-box">
                <!-- 当失效后就不能在点击跳转了 -->
                <el-link type="primary" :underline="false" target="_blank"
                  :disabled="scope?.row?.validDateType === 1 && !isExpire(scope?.row?.validDate)"
                  :href="'http://' + scope.row.fullShortUrl">{{ scope.row.domain + '/' + scope.row.shortUri }}</el-link>
                <el-tooltip show-after="500" :content="scope.row.originUrl">
                  <span>{{ scope.row.originUrl }}</span>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="copy" width="170">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <!-- 二维码 -->
                <QRCode :url="'http://' + scope.row.fullShortUrl"></QRCode>
                <!-- 表格中的复制链接按钮 -->
                <el-tooltip show-after="500" class="box-item" effect="dark" content="复制链接" placement="bottom-end">
                  <el-icon @click="copyUrl('http://' + scope.row.fullShortUrl)" class="table-edit copy-url">
                    <Share />
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="访问次数" prop="times" width="120">
            <template #header>
              <el-dropdown>
                <div :class="{ orderIndex: orderIndex === 1 }" class="block" style="margin-top: 3px">
                  <span>访问次数</span>
                  <el-icon>
                    <CaretBottom />
                  </el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-item @click="pageParams.orderTag = 'todayPv', orderIndex = 1">今日访问次数</el-dropdown-item>
                  <el-dropdown-item @click="pageParams.orderTag = 'totalPv', orderIndex = 1">累计访问次数</el-dropdown-item>
                </template>
              </el-dropdown>
            </template>
            <template #default="scope">
              <div class="times-box">
                <div class="today-box">
                  <span>今日</span>
                  <span>{{ scope.row.todayPv }}</span>
                </div>
                <div class="total-box">
                  <span>累计</span>
                  <span>{{ scope.row.totalPv }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="访问人数" prop="people" width="120">
            <template #header>
              <el-dropdown>
                <div :class="{ orderIndex: orderIndex === 2 }" class="block" style="margin-top: 3px">
                  <span>访问人数</span>
                  <el-icon>
                    <CaretBottom />
                  </el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-item @click="pageParams.orderTag = 'todayUv', orderIndex = 2">今日访问人数</el-dropdown-item>
                  <el-dropdown-item @click="pageParams.orderTag = 'totalUv', orderIndex = 2">累计访问人数</el-dropdown-item>
                </template>
              </el-dropdown>
            </template>
            <template #default="scope">
              <div class="times-box">
                <div class="today-box">
                  <span>今日</span>
                  <span>{{ scope.row.todayUv }}</span>
                </div>
                <div class="total-box">
                  <span>累计</span>
                  <span>{{ scope.row.totalUv }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="IP数" prop="IP" width="120">
            <template #header>
              <el-dropdown>
                <div :class="{ orderIndex: orderIndex === 3 }" class="block" style="margin-top: 3px">
                  <span>IP数</span>
                  <el-icon>
                    <CaretBottom />
                  </el-icon>
                </div>
                <template #dropdown>
                  <el-dropdown-item @click="pageParams.orderTag = 'todayUip', orderIndex = 3">今日IP数</el-dropdown-item>
                  <el-dropdown-item @click="pageParams.orderTag = 'totalUip', orderIndex = 3">累计IP数</el-dropdown-item>
                </template>
              </el-dropdown>
            </template>
            <template #default="scope">
              <div class="times-box">
                <div class="today-box">
                  <span>今日</span>
                  <span>{{ scope.row.todayUip }}</span>
                </div>
                <div class="total-box">
                  <span>累计</span>
                  <span>{{ scope.row.totalUip }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <!-- <el-link
                :underline="false"
                class="el-link"
                type="primary"
                @click="chartsVisible(row?.info)"
                >数据</el-link
              >
              <el-link :underline="false" class="el-link" type="primary">编辑</el-link> -->
                <!-- 表格中的数据按钮 -->
                <el-tooltip show-after="500" class="box-item" effect="dark" content="查看图表" placement="bottom-end">
                  <el-icon class="table-edit" @click="chartsVisible(scope.row)">
                    <Histogram />
                  </el-icon>
                </el-tooltip>
                <!-- 正常页面展示编辑和删除 -->
                <template v-if="selectedIndex !== -1">
                  <!-- 表格中的编辑按钮 -->
                  <el-tooltip show-after="500" class="box-item" effect="dark" content="编辑" placement="bottom-end">
                    <el-icon @click="editLink(scope.row)" class="table-edit">
                      <Tools />
                    </el-icon>
                  </el-tooltip>
                  <!-- 删除按钮 -->
                  <el-tooltip show-after="500" class="box-item" effect="dark" content="删除" placement="bottom-end">
                    <el-popconfirm width="100" title="是否移入回收站" @confirm="toRecycleBin(scope.row)">
                      <template #reference>
                        <el-icon class="table-edit">
                          <Delete />
                        </el-icon>
                      </template>
                    </el-popconfirm>
                  </el-tooltip>
                </template>
                <!-- 回收站操作 -->
                <template v-else>
                  <!-- 回收站中的恢复按钮 -->
                  <el-tooltip show-after="500" class="box-item" effect="dark" content="恢复" placement="bottom-end">
                    <el-icon @click="recoverLink(scope.row)" class="table-edit">
                      <HelpFilled />
                    </el-icon>
                  </el-tooltip>
                  <!-- 回收站中的删除按钮 -->
                  <el-tooltip show-after="500" class="box-item" effect="dark" content="删除" placement="bottom-end">
                    <el-popconfirm width="300" title="删除后短链跳转会失效，同时停止数据统计，这是一个不可逆的操作，是否删除?"
                      @confirm="removeLink(scope.row)">
                      <template #reference>
                        <el-icon class="table-edit">
                          <Delete />
                        </el-icon>
                      </template>
                    </el-popconfirm>
                  </el-tooltip>
                </template>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页器 -->
        <div class="pagination-block">
          <el-pagination v-model:current-page="pageParams.current" v-model:page-size="pageParams.size"
            :page-sizes="[10, 15, 20, 30]" layout="total, sizes, prev, pager, next, jumper" :total="totalNums"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
      </div>
    </div>
</template>

<script setup>
import { reactive } from 'vue'
const pageParams = reactive({
  gid: null,
  current: 1,
  size: 15,
  orderTag: null
})
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