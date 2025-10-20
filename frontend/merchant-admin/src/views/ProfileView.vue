<template>
  <Layout>
    <div class="profile">
      <div class="page-header">
        <h1>个人资料</h1>
        <p>管理您的账户信息和设置</p>
      </div>
      
      <div class="profile-content">
        <!-- 基本信息 -->
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
              <el-button type="primary" @click="editMode = !editMode">
                {{ editMode ? '取消编辑' : '编辑信息' }}
              </el-button>
            </div>
          </template>
          
          <el-form
            ref="profileFormRef"
            :model="profileForm"
            :rules="profileRules"
            label-width="120px"
            :disabled="!editMode"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="profileForm.username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商家名称" prop="name">
                  <el-input v-model="profileForm.name" placeholder="请输入商家名称" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contactName">
                  <el-input v-model="profileForm.contactName" placeholder="请输入联系人姓名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="phone">
                  <el-input v-model="profileForm.phone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="邮箱地址" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱地址" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="营业执照" prop="businessLicense">
                  <el-input v-model="profileForm.businessLicense" placeholder="请输入营业执照号" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="商家地址" prop="address">
              <el-input
                v-model="profileForm.address"
                type="textarea"
                :rows="3"
                placeholder="请输入详细地址"
              />
            </el-form-item>
            
            <el-form-item v-if="editMode">
              <el-button type="primary" @click="saveProfile" :loading="saving">
                {{ saving ? '保存中...' : '保存' }}
              </el-button>
              <el-button @click="cancelEdit">取消</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 修改密码 -->
        <el-card class="password-card">
          <template #header>
            <div class="card-header">
              <span>修改密码</span>
            </div>
          </template>
          
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="120px"
            style="max-width: 500px"
          >
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入当前密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="changePassword" :loading="changingPassword">
                {{ changingPassword ? '修改中...' : '修改密码' }}
              </el-button>
              <el-button @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 账户状态 -->
        <el-card class="status-card">
          <template #header>
            <div class="card-header">
              <span>账户状态</span>
            </div>
          </template>
          
          <div class="status-info">
            <div class="status-item">
              <span class="status-label">账户状态：</span>
              <el-tag :type="authStore.merchant?.isActive ? 'success' : 'danger'">
                {{ authStore.merchant?.isActive ? '正常' : '已禁用' }}
              </el-tag>
            </div>
            
            <div class="status-item">
              <span class="status-label">账户类型：</span>
              <el-tag :type="authStore.merchant?.isAdmin ? 'warning' : 'info'">
                {{ authStore.merchant?.isAdmin ? '管理员' : '普通商家' }}
              </el-tag>
            </div>
            
            <div class="status-item">
              <span class="status-label">注册时间：</span>
              <span>{{ formatDate(authStore.merchant?.createdAt) }}</span>
            </div>
            
            <div class="status-item">
              <span class="status-label">最后更新：</span>
              <span>{{ formatDate(authStore.merchant?.updatedAt) }}</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElForm } from 'element-plus'
import Layout from '@/components/Layout.vue'
import { useAuthStore } from '@/stores/auth'
import { changePassword as changePasswordApi } from '@/api/auth'

const authStore = useAuthStore()
const editMode = ref(false)
const saving = ref(false)
const changingPassword = ref(false)
const profileFormRef = ref<InstanceType<typeof ElForm>>()
const passwordFormRef = ref<InstanceType<typeof ElForm>>()

// 个人信息表单
const profileForm = reactive({
  username: '',
  name: '',
  contactName: '',
  phone: '',
  email: '',
  address: '',
  businessLicense: ''
})

// 原始数据备份
const originalProfile = reactive({})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 个人信息验证规则
const profileRules = {
  name: [
    { required: true, message: '请输入商家名称', trigger: 'blur' },
    { min: 2, max: 100, message: '商家名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  contactName: [
    { max: 50, message: '联系人姓名不能超过 50 个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  businessLicense: [
    { max: 50, message: '营业执照号不能超过 50 个字符', trigger: 'blur' }
  ],
  address: [
    { max: 200, message: '地址不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' },
    { max: 50, message: '密码长度不能超过 50 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 初始化个人信息
const initProfile = () => {
  if (authStore.merchant) {
    Object.assign(profileForm, {
      username: authStore.merchant.username,
      name: authStore.merchant.name,
      contactName: authStore.merchant.contactName || '',
      phone: authStore.merchant.phone || '',
      email: authStore.merchant.email || '',
      address: authStore.merchant.address || '',
      businessLicense: authStore.merchant.businessLicense || ''
    })
    
    // 备份原始数据
    Object.assign(originalProfile, profileForm)
  }
}

// 保存个人信息
const saveProfile = async () => {
  if (!profileFormRef.value) return
  
  try {
    const valid = await profileFormRef.value.validate()
    if (!valid) return
    
    saving.value = true
    
    // TODO: 调用API保存个人信息
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success('个人信息保存成功')
    editMode.value = false
    
    // 更新备份数据
    Object.assign(originalProfile, profileForm)
    
    // 刷新用户信息
    await authStore.fetchCurrentMerchant()
  } catch (error) {
    console.error('Save profile error:', error)
  } finally {
    saving.value = false
  }
}

// 取消编辑
const cancelEdit = () => {
  // 恢复原始数据
  Object.assign(profileForm, originalProfile)
  editMode.value = false
  profileFormRef.value?.clearValidate()
}

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  try {
    const valid = await passwordFormRef.value.validate()
    if (!valid) return
    
    changingPassword.value = true
    
    await changePasswordApi({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    
    ElMessage.success('密码修改成功')
    resetPasswordForm()
  } catch (error) {
    console.error('Change password error:', error)
  } finally {
    changingPassword.value = false
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  Object.assign(passwordForm, {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  })
  passwordFormRef.value?.clearValidate()
}

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  initProfile()
})
</script>

<style scoped>
.profile {
  width: 100%;
  margin: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 16px;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-card,
.password-card,
.status-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.status-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.status-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  width: 120px;
  color: #606266;
  font-weight: 500;
}

:deep(.el-card__body) {
  padding: 24px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background-color: #f5f7fa;
}

@media (max-width: 768px) {
  .profile {
    margin: 0 16px;
  }
  
  :deep(.el-col) {
    width: 100% !important;
  }
}
</style>