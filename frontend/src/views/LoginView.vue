<template>
  <section class="login-page formal-login">
    <div class="login-visual">
      <span class="login-badge">PROPERTY MANAGEMENT</span>
      <h1>物业管理系统</h1>
      <p>面向物业运营、客服、工程维修和财务收费的一体化后台工作台。</p>

      <div class="login-highlights">
        <div class="highlight-item">
          <strong>住户</strong>
          <span>档案、房屋、车辆统一维护</span>
        </div>
        <div class="highlight-item">
          <strong>收费</strong>
          <span>账单、缴费、欠费状态联动</span>
        </div>
        <div class="highlight-item">
          <strong>工单</strong>
          <span>报修、派单、处理闭环</span>
        </div>
      </div>
    </div>

    <div class="login-panel-wrap">
      <el-card shadow="never" class="login-card formal-login-card">
        <div class="login-title">
          <small>ADMIN CONSOLE</small>
          <strong>账号登录</strong>
          <span>账号和密码由后端连接数据库用户表校验</span>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-form-item label="登录账号" prop="username">
            <el-input v-model="form.username" placeholder="请输入账号" autocomplete="username" size="large" />
          </el-form-item>
          <el-form-item label="登录密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="请输入密码"
              autocomplete="current-password"
              size="large"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-button" :loading="loading" size="large" @click="handleLogin">
              登录系统
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-tips">
          <span>演示账号：admin</span>
          <span>演示密码：123456</span>
        </div>
      </el-card>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { setToken, setUser } from '../utils/auth'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const response = await request.post('/api/auth/login', form)
    setToken(response.data.token)
    setUser(response.data.user)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>
