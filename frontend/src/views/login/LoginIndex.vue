<template>
	<div class="login-page">
    <VantaBackground />

    <h1 class="title">My Short Link</h1>

    <div class="login-box">
			<LoginBox />
			<RegisterBox />
      <LoginSlider />
  	</div>

    
  </div>
</template>

<script setup>
  import VantaBackground from './components/VantaBackground.vue'
	import LoginBox from './components/LoginBox.vue'
	import RegisterBox from './components/RegisterBox.vue'
  import LoginSlider from './components/LoginSlider.vue'
  import { ref, provide } from 'vue'

	const isLogin = ref(true)
  const moveRef = ref(null)
  provide('isLogin', isLogin)
  provide('moveRef', moveRef)
</script>

<style lang="less" scoped>
  .login-page {
    position: relative;
    width: 100vw;
    height: 100vh;
    overflow: hidden;
  }

  .title {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    top: 15%;
    z-index: 999;
    font-size: 40px;
    color: #fff;
    font-weight: bolder;
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

  @keyframes hideIndex {
    0% {
      opacity: 0;
      transform: translate(7.3125rem, -50%);
    }

    100% {
      opacity: 1;
      transform: translate(-50%, -50%);
    }
  }

  .hidden {
    animation: hidden 1s;
    animation-fill-mode: forwards;
  }

  @keyframes hidden {
    0% {
      opacity: 1;
    }

    70% {
      opacity: 0;
    }

    100% {
      opacity: 0;
    }
  }

  :deep h2 {
    font-size: 30px;
    font-family:
      PingFangSC-Semibold,
      PingFang SC;
    font-weight: 600;
    color: #3a3f63;
    width: 100%;
    text-align: center;
    padding: 20px;
  }
  
  :deep .el-form-item {
    margin-bottom: 23px;
  }

  :deep .el-checkbox {
    width: 100%;
    text-align: center;
    margin-top: 1rem;
  }

  :deep .button-group {
		margin-top: 30px;
		display: flex;
		justify-content: space-between;
		margin-bottom: 20px;

		.el-button {
			width: 100px;
		}
	}

  :deep .el-form-item__content {
    margin-left: 0 !important;
  }

  :deep(.el-input__suffix-inner) {
    width: 60px;
  }

  :deep .second-font {
    margin-left: 13px;
  }
</style>