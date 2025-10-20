import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { MerchantInfo } from '@/api/auth'
import { login, getCurrentMerchant, logout } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('merchant_token'))
  const merchant = ref<MerchantInfo | null>(null)
  const loading = ref(false)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => merchant.value?.isAdmin || false)

  // 登录
  const loginAction = async (username: string, password: string) => {
    loading.value = true
    try {
      // 临时测试登录逻辑
      if ((username === '我注册123' && password === '123') || 
          (username === '123' && password === '123')) {
        token.value = 'test-token-123'
        merchant.value = {
          id: 1,
          username: username,
          name: '测试商家',
          contactName: '张三',
          phone: '13800138000',
          email: 'test@example.com',
          address: '',
          businessLicense: '',
          status: 1,
          isAdmin: false,
          lastLoginTime: new Date().toISOString(),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
          permissions: []
        }
        localStorage.setItem('merchant_token', token.value)
        ElMessage.success('登录成功')
        return true
      }
      
      const response = await login({ username, password })
      if (response.success) {
        token.value = response.data.token
        merchant.value = response.data.merchant
        localStorage.setItem('merchant_token', response.data.token)
        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(response.message || '登录失败')
        return false
      }
    } catch (error) {
      console.error('Login error:', error)
      ElMessage.error('用户名或密码错误')
      return false
    } finally {
      loading.value = false
    }
  }

  // 获取当前用户信息
  const fetchCurrentMerchant = async () => {
    if (!token.value) return false
    
    try {
      const response = await getCurrentMerchant()
      if (response.success) {
        merchant.value = response.data
        return true
      }
    } catch (error) {
      console.error('Fetch merchant error:', error)
      // 如果获取用户信息失败，清除token
      logoutAction()
    }
    return false
  }

  // 登出
  const logoutAction = async () => {
    try {
      if (token.value) {
        await logout()
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      token.value = null
      merchant.value = null
      localStorage.removeItem('merchant_token')
      ElMessage.success('已退出登录')
    }
  }

  // 初始化
  const init = async () => {
    if (token.value) {
      await fetchCurrentMerchant()
    }
  }

  return {
    token,
    merchant,
    loading,
    isLoggedIn,
    isAdmin,
    loginAction,
    fetchCurrentMerchant,
    logoutAction,
    init
  }
})