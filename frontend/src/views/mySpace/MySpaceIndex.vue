<template>
  <div class="my-space-container">
    <LinkGroupSidebar />
    <ContentBox />
  </div>
</template>

<script setup>
import LinkGroupSidebar from './components/LinkGroupSidebar.vue';
import ContentBox from './components/ContentBox.vue'

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