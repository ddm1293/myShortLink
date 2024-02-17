<template>
  <div class="register" :class="{ hidden: isLogin }">
        <h2>用户注册</h2>

        <el-form 
          ref="loginFormRef2" 
          :model="addForm" 
          label-width="50px" 
          class="form-container" 
          width="width"
          :rules="addFormRule"
        >
          <el-form-item prop="username">
            <el-input v-model="addForm.username" placeholder="请输入用户名" maxlength="11" show-word-limit clearable>
              <template v-slot:prepend> 用户名 </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="email">
            <el-input v-model="addForm.email" placeholder="请输入邮箱" show-word-limit clearable>
              <template v-slot:prepend> 邮<span class="second-font">箱</span> </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="phoneNumber">
            <el-input v-model="addForm.phoneNumber" placeholder="请输入手机号" show-word-limit clearable>
              <template v-slot:prepend> 手机号 </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="addForm.password" type="password" clearable placeholder="请输入密码" show-password>
              <template v-slot:prepend> 密<span class="second-font">码</span> </template>
            </el-input>
          </el-form-item>

          <div class="button-group">
            <div></div>
            <div>
              <el-button :loading="loading" type="primary" plain
                @click="addUser(loginFormRef2)">注册</el-button>
            </div>
          </div>
        </el-form>
      </div>
</template>

<script setup>
  	import { ref, reactive, inject, getCurrentInstance } from 'vue'
	import { setToken, setUsername } from '@/auth/auth.js'
	import { useRouter } from 'vue-router'
	import { ElMessage } from 'element-plus'
	const { proxy } = getCurrentInstance()
	const API = proxy.$API
	const router = useRouter()
  const isLogin = inject('isLogin')
  const loginFormRef2 = ref()
  const addForm = reactive({
    username: '',
    password: '',
    phoneNumber: '',
    email: ''
  })

  const addFormRule = reactive({
    phoneNumber: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      {
        // pattern: /^1[3|5|7|8|9]\d{9}$/,
        message: '请输入正确的手机号',
        trigger: 'blur'
      },
      { min: 11, max: 11, message: '手机号必须是11位', trigger: 'blur' }
    ],
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 8, max: 15, message: '密码长度请在八位以上', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      {
        pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
        message: '请输入正确的邮箱号',
        trigger: 'blur'
      }
    ]
  })

  const loading = ref(false)

  const startLoading = () => {
		loading.value = true;
	}

	const endLoading = () => {
		loading.value = false;
	}

  const addUser = async (formEl) => {
    if (!formEl) return
    startLoading

    try {
      const valid = await formEl.validate();
      if (valid) {
        // 检测用户名是否已经存在
        const hasUsernameRes = await API.user.hasUsername({ username: addForm.username })
        if (hasUsernameRes.data.success === true) {
          // 注册
          const addUserRes = await API.user.addUser(addForm)
          console.log("add user: ", addUserRes)
          if (addUserRes.data.success === false) {
            ElMessage.warning(addUserRes.data.message)
          } else {
            const userLoginRes = await API.user.login({ username: addForm.username, password: addForm.password })
            const token = userLoginRes?.data?.data?.token
            // 将username和token保存到cookies中和localStorage中
            if (token) {
              setToken(token)
              setUsername(addForm.username)
              localStorage.setItem('token', token)
              localStorage.setItem('username', addForm.username)
            }
            ElMessage.success('注册登录成功！')
            // router.push('/home')
          }
        } else {
          ElMessage.warning('用户名已存在！')
        }
      }
    } catch (error) {
      console.log("add user failed", error)
    } finally {
      endLoading
    }

}

</script>

<style scoped>

</style>