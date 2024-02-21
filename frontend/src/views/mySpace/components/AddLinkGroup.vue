<template>
  <div class="add-link-group-container">
      <div class="option-title flex-box">
        <div>
          短链分组<span> 共{{ editableTabsLength }}组</span>
        </div>
        <div class="hover-box" style="width: 24px" @click="showAddGroup">
          <img src="@/assets/svg/添加.svg" alt="" />
        </div>
      </div>
    <el-dialog v-model="isAddGroup" title="新建短链接分组" style="width: 40%">
      <el-form :model="form">
        <el-form-item label="分组名称：" :label-width="formLabelWidth">
          <el-input autocomplete="off" v-model="newGroupName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="isAddGroup = false">取消</el-button>
          <el-button type="primary" @click="addGroup" :disabled="addGroupLoading"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref, computed, getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()
const editableTabsLength = computed(() => proxy.$store.state.mySpace.editableTabs?.length)
const API = proxy.$API

const isAddGroup = ref(false)
const newGroupName = ref()
const addGroupLoading = ref(false)

const showAddGroup = () => {
  newGroupName.value = ''
  isAddGroup.value = true
}

const addGroup = async () => {
  addGroupLoading.value = true
  try {
    await proxy.$store.dispatch('mySpace/addGroup', { groupName: newGroupName.value })
    await proxy.$store.dispatch('mySpace/queryPage', null)
    ElMessage.success('添加成功')
  } catch (err) {
    console.log('fail addGroup: ', err)
    ElMessage.error(err.message)
  }
  isAddGroup.value = false
  addGroupLoading.value = false
}
</script>

<style lang="scss" scoped>
  .option-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 56px;
    font-size: 15px;
    font-weight: 600;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);

    span {
      font-size: 12px;
      font-weight: 400;
    }
  }

  .hover-box:hover {
    cursor: pointer;
    color: rgba(40, 145, 206, 0.6);
    background-color: #f7f7f7;
    box-shadow: 0px 2px 8px 0px rgba(28, 41, 90, 0.1);
  }
</style>