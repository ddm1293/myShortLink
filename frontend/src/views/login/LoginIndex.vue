<template>
	<div class="login-page">

    <!-- <VantaBackground /> -->
    
    <h1 class="title">My Short Link</h1>

    <div class="login-box">
			<LoginBox />
			
			<!-- <RegisterBox /> -->

      <!-- move -->
  	</div>

    
  </div>
</template>

<script setup>
  import VantaBackground from './components/VantaBackground.vue'
	import LoginBox from './components/LoginBox.vue'
	import RegisterBox from './components/RegisterBox.vue'
</script>

<style lang="less" scoped>
    /* .title {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    top: 15%;
    z-index: 999;
    font-size: 40px;
    color: #fff;
    font-weight: bolder;
  } */

  .login-page {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  .login-box {
    border: 2px solid #0984e3;
    overflow: hidden;
    display: flex;
    justify-content: space-between;
    border-radius: 20px;
    padding: 0 40px 0 40px;
    width: 700px;
    // background-color: #eee;
    position: absolute;
    z-index: 999;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-sizing: border-box;
    // border: 1px solid #eee;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    background-color: #fff;
    animation: hideIndex 0.5s;
  }
</style>