<template>
  <div class="content-table">
    <el-table 
      :data="tableData" 
      height="calc(100vh - 240px)" 
      style="width: calc(100vw - 230px)"
      :header-cell-style="{ background: '#f7f8fa', color: '#606266' }"
    >
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
                <span>{{ scope.row.description }}</span>
              </el-tooltip>
              <div class="time" style="display: flex">
                <span>{{ scope.row.createdAt }}</span>
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
              :href="'http://' + scope.row.fullShortUrl">
              {{ scope.row.domain + '/' + scope.row.shortUri }}
            </el-link>
            <el-tooltip show-after="500" :content="scope.row.originalUrl">
              <span>{{ scope.row.originalUrl }}</span>
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
  </div>
</template>

<script setup>
  import { computed, getCurrentInstance } from 'vue'
  import { ElMessage } from 'element-plus'
  import defaultImg from '@/assets/png/短链默认图标.png'
  import QRCode from '../components/QRCode.vue'
  import StatisticsColumn from '../components/StatisticsColumn.vue'

  const { proxy } = getCurrentInstance()

  const tableData = computed(() => proxy.$store.state.mySpace.tableData)
  const orderIndex = computed(() => proxy.$store.state.mySpace.orderIndex)
  const pageParams = computed(() => proxy.$store.state.mySpace.pageParams)
  const selectedIndex = computed(() => proxy.$store.state.mySpace.selectedIndex)

  const isExpire = (validDate) => {
    if (validDate) {
      const date = new Date(validDate).getTime()
      return new Date().getTime() < date
    }
  }

  const getImgUrl = (url) => {
    return url ?? defaultImg
  }

  const copyUrl = (url) => {
    navigator.clipboard.writeText(url).then(() => {
      ElMessage.success('链接复制成功!')
    }).catch((err) => {
      console.error('failing copying url: ', err)
    })
  }

  const chartsVisible = () => {}

  const editLink = () => {}

  const toRecycleBin = () => {}

  const recoverLink = () => {}

  const removeLink = () => {}

</script>

<style lang="scss" scoped>

</style>