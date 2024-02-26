<template>
  <el-table-column :label="label" :prop="prop" width="120">
    <template #header>
      <el-dropdown>
        <div :class="{ 'order-index': orderIndex === dropdownIndex }" class="block" style="margin-top: 3px">
          <span>{{ label }}</span>
          <el-icon>
            <CaretBottom />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-item @click="() => handleOrderChange('today')">今日{{ label }}</el-dropdown-item>
          <el-dropdown-item @click="() => handleOrderChange('total')">累计{{ label }}</el-dropdown-item>
        </template>
      </el-dropdown>
    </template>
    <template #default="scope">
      <div class="times-box">
        <div class="today-box">
          <span>今日</span>
          <span>{{ scope.row[todayProp] }}</span>
        </div>
        <div class="total-box">
          <span>累计</span>
          <span>{{ scope.row[totalProp] }}</span>
        </div>
      </div>
    </template>
  </el-table-column>
</template>

<script setup>
import { computed, getCurrentInstance } from 'vue'
import { defineProps } from 'vue';

const { proxy } = getCurrentInstance()
const pageParams = computed(() => proxy.$store.state.mySpace.pageParams)

const props = defineProps({
  label: String,
  prop: String,
  todayProp: String,
  totalProp: String,
  dropdownIndex: Number,
  orderIndex: Number
});

const handleOrderChange = (type) => {
  
};
</script>
