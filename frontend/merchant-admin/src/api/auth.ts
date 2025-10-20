import api from './index'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  success: boolean
  data: {
    token: string
    merchant: {
      id: number
      username: string
      name: string
      contactName?: string
      phone?: string
      email?: string
      isAdmin: boolean
    }
  }
  message: string
}

export interface MerchantInfo {
  id: number
  username: string
  name: string
  contactName?: string
  phone?: string
  email?: string
  address?: string
  businessLicense?: string
  isAdmin: boolean
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// 商家登录
export const login = (data: LoginRequest): Promise<LoginResponse> => {
  return api.post('/MerchantAuth/login', data)
}

// 获取当前商家信息
export const getCurrentMerchant = (): Promise<{ success: boolean; data: MerchantInfo }> => {
  return api.get('/MerchantAuth/current')
}

// 修改密码
export const changePassword = (data: { oldPassword: string; newPassword: string }) => {
  return api.post('/MerchantAuth/change-password', data)
}

// 登出
export const logout = () => {
  return api.post('/MerchantAuth/logout')
}