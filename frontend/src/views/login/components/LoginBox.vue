<template>
	<div class="login" :class="{ hidden: !isLogin }" >
		<h2>Log In</h2>

		<el-form 
			:model="loginForm" ref="loginFormRef" 
			label-width="50px" 
			:rules="loginFormRules" 
		>
			<div class="login-form-container">
				<el-form-item prop="username">
					<el-input v-model="loginForm.username" placeholder="请输入用户名" maxlength="11" show-word-limit clearable>
                <template v-slot:prepend> 用户名 </template>
          </el-input>
				</el-form-item>

				<el-form-item prop="password">
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
	import { ref, reactive, inject, getCurrentInstance } from 'vue'
	import { setToken, setUsername, getUsername } from '@/auth/auth.js'
	import { useRouter } from 'vue-router'
	import { ElMessage } from 'element-plus'
	const { proxy } = getCurrentInstance()
	const API = proxy.$API
	const router = useRouter()

	const isLogin = inject('isLogin')
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

	const startLoading = () => {
		loading.value = true;
	}

	const endLoading = () => {
		loading.value = false;
	}

	const login = async (formEl) => {
		if (!formEl) return
		startLoading();
		try {
			const valid = await formEl.validate();
			if (valid) {
				console.log('submit!: ', loginForm)
			  const res = await API.user.login(loginForm)
				console.log('see res', res)
				if (res.data.code === '0') {
					const token = res?.data?.data?.token
					// 将username和token保存到cookies中和localStorage中
					if (token) {
						setToken(token)
						setUsername(loginForm.username)
						localStorage.setItem('token', token)
						localStorage.setItem('username', loginForm.username)
					}
					ElMessage.success('登录成功！')
					// router.push('/home')
				} else if (res.data.message === 'User HAS ALREADY LOGGED IN') {
					const cookiesUsername = getUsername()
					if (cookiesUsername === loginForm.username) {
						ElMessage.success('登录成功！')
						console.log('登录成功！')
						// router.push('/home')
					} else {
						ElMessage.warning('用户已在别处登录，请勿重复登录！')
					}
				} else if (res.data.message === 'Null User Found') {
					ElMessage.error('请输入正确的账号密码!')
				}
			}
		} catch (error) {
			console.error("validation failed: ", error);
		} finally {
			endLoading();
		}
	}
</script>

<style lang="less" scoped>
	.login {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.remeber-password {
		left: 0;
		line-height: 0.1rem;
	}

	.login-form-container {
		transform: translateY(-70%);
	}

</style>

