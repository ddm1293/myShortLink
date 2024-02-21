<template>
  <div class="my-space-container">
    <LinkGroup />
    <!-- <ContentBox /> -->
  </div>
</template>

<script setup>
import LinkGroup from './components/LinkGroup.vue';
import ContentBox from './components/ContentBox.vue'
import { ref, reactive, provide, getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

</script>

<style lang="scss" scoped>

.my-space-container {
  display: flex;
  height: 100%;
}

:deep .flex-box {
  display: flex;
  align-items: center;
  padding: 0 10px;
  justify-content: space-between;
}

</style>