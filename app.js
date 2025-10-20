// app.js
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 初始化用户信息
    this.initUserInfo();

    // 微信登录 - 暂时注释，等待后续开启
    /* 
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        console.log('登录成功', res.code)
      }
    })
    */
  },
  
  globalData: {
    userInfo: null,
    apiBase: 'http://localhost:8080/api', // 后端API地址
    apiBaseUrl: 'http://localhost:8080/api', // 兼容旧版本
    token: null,
    userId: null,
    isGuest: false, // 游客模式标识
    version: '1.0.0'
  },

  // 全局方法
  // 显示加载提示
  showLoading(title = '加载中...') {
    wx.showLoading({
      title: title,
      mask: true
    })
  },

  // HTTP请求封装
  request(options) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: this.globalData.apiBase + options.url,
        method: options.method || 'GET',
        data: options.data || {},
        header: {
          'Content-Type': 'application/json',
          'Authorization': wx.getStorageSync('token') ? `Bearer ${wx.getStorageSync('token')}` : '',
          ...options.header
        },
        success: (res) => {
          if (res.statusCode === 200) {
            if (res.data.code === 200) {
              resolve(res.data)
            } else {
              this.showError(res.data.message || '请求失败')
              reject(res.data)
            }
          } else {
            this.showError('网络请求失败')
            reject(res)
          }
        },
        fail: (err) => {
          this.showError('网络连接失败')
          reject(err)
        }
      })
    })
  },

  // 错误提示
  showError(message = '发生错误') {
    wx.showToast({
      title: message,
      icon: 'none'
    })
  },

  // 初始化用户信息
  initUserInfo() {
    try {
      const userInfo = wx.getStorageSync('userInfo') || null
      const token = wx.getStorageSync('token') || null
      const userId = wx.getStorageSync('userId') || null
      const isGuest = wx.getStorageSync('isGuest') || false

      this.globalData.userInfo = userInfo
      this.globalData.token = token
      this.globalData.userId = userId
      this.globalData.isGuest = isGuest
    } catch (e) {
      console.warn('读取本地用户信息失败', e)
    }
  }
})
App({
  onLaunch: function () {
    const that = this;
    // ... (您其他的 onLaunch 程式碼保持不變)
  },

  // ... (您其他的 app.js 內容)

  globalData: {
    userInfo: null,
    token: null,
    httpClient: null,
    // !! 新增這一行 !!
    navigateToCategoryState: null // 用於儲存跳轉到分類頁的狀態
  }
});