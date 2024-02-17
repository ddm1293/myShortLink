<template>
  <div class="move" ref="moveRef">
    <span style="font-size: 18px; margin-bottom: 25px; color: rgb(225, 238, 250)">
      {{ !isLogin ? '已有账号？' : '还没有账号？' }}
    </span>

    <span style="font-size: 16px; color: rgb(225, 238, 250)">
      {{ !isLogin ? '欢迎登录账号！' : '欢迎注册账号！' }}
    </span>

    <el-button style="width: 100px; margin-top: 30px" @click="changeLogin">
      {{ !isLogin ? '去登录' : '去注册' }}
    </el-button>
  </div>
</template>

<script setup>
import { ref, inject } from 'vue'
const isLogin = inject('isLogin')
const moveRef = inject('moveRef')

const changeLogin = () => {
  isLogin.value = !isLogin.value
  if (isLogin.value) {
    moveRef.value.style.transform = 'translate(0, 0)'
  } else {
    moveRef.value.style.transform = 'translate(-420px, 0)'
  }
}
</script>

<style scoped>
  .move {
    position: absolute;
    right: 0;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 40%;
    transition-duration: 0.5s;
    align-items: center;
    background: #06beb6;
    /* fallback for old browsers */
    background: -webkit-linear-gradient(to right, #0984e3, #0984e3);
    /* Chrome 10-25, Safari 5.1-6 */
    background: linear-gradient(to right,
        #1a8fd5,
        #0984e3);
    /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  }
</style>