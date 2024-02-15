<template>
	<div class="login" :class="{ hidden: !isLogin }" >
		<h2>Log In</h2>

		<el-form 
			:model="loginForm" ref="loginFormRef" 
			label-width="50px" 
			:rules="loginFormRules" 
			@submit.prevent="login(loginFormRef)"
		>
			<div class="login-form-container">
				<el-form-item>
					<el-input v-model="loginForm.username" placeholder="请输入用户名" maxlength="11" show-word-limit clearable>
                <template v-slot:prepend> 用户名 </template>
          </el-input>
				</el-form-item>

				<el-form-item>
					<el-input v-model="loginForm.password" type="password" clearable placeholder="请输入密码" show-password
                style="margin-top: 20px">
                <template v-slot:prepend> 密<span class="second-font">码</span> </template>
              </el-input>
				</el-form-item>
			</div>

			<div class="button-group">
				<el-checkbox class="remeber-password" v-model="checked" style="color: #a0a0a0; margin: 0 0 0px 0">
					记住密码
				</el-checkbox>

				<el-button :loading="loading" type="primary" plain @keyup.enter="login(loginFormRef)" @click="login(loginFormRef)">
					登陆
				</el-button>
			</div>
		</el-form>
	</div>
</template>

<script setup>
	import { ref, reactive } from 'vue'
	const isLogin = ref(true)
	const loading = ref(false)
	const checked = ref(true)
	
	const loginForm = reactive({
  username: '',
  password: '',
})

	const loginFormRef = ref()

	const loginFormRules = reactive({
		username: [{ required: true, message: '请输入您的真实姓名', trigger: 'blur' }],
		password: [
			{ required: true, message: '请输入密码', trigger: 'blur' },
			{ min: 8, max: 15, message: '密码长度请在八位以上', trigger: 'blur' }
		],
	})

	const login = async (formEl) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				console.log('submit!')
			} else {
				console.log('error submit!', fields)
				return
			}
		})
	}
</script>

<style lang="less" scoped>
	.login {
			display: flex;
			flex-direction: column;
			justify-content: space-between;
	}
	
	h2 {
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

	.el-form-item {
    margin-bottom: 23px;
  }

	.button-group {
    margin-top: 30px;
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .el-button {
      width: 100px;
    }

		.el-checkbox {
			width: 100%;
			text-align: center;
			margin-top: 1rem;
  	}

    .remeber-password {
      left: 0;
      line-height: 0.5rem;
    }
  }

	/deep/ .el-form-item__content {
  	margin-left: 0 !important;
	}

	:deep(.el-input__suffix-inner) {
		width: 60px;
	}

</style>

