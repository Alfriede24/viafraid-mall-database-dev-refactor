// pages/login/login.js
Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    hasUserInfo: false,
    userInfo: {},
    loading: false,
    // 账号登录弹窗及字段
    showPhoneModal: false,
    username: '',
    password: '',
    submitLoading: false
  },

  onLoad: function (options) {
    // 检查是否已经授权
    this.checkUserInfo();
  },

  // 检查用户信息
  checkUserInfo: function () {
    const app = getApp();
    if (app.globalData.userInfo) {
      this.setData({
        hasUserInfo: true,
        userInfo: app.globalData.userInfo
      });
    }
  },

  // 微信登录 - 直接进入应用（保留原有模拟逻辑）
  onWechatLogin: function () {
    const app = getApp();
    const self = this;
    this.setData({ loading: true });
    setTimeout(() => {
      const mockUserInfo = {
        nickName: '微信用户',
        avatarUrl: '/images/default-avatar.svg',
        gender: 1,
        country: '中国',
        province: '广东',
        city: '深圳'
      };
      app.globalData.userInfo = mockUserInfo;
      app.globalData.token = 'mock_token_' + Date.now();
      app.globalData.userId = 'mock_user_' + Date.now();
      app.globalData.isGuest = false;
      wx.setStorageSync('userInfo', mockUserInfo);
      wx.setStorageSync('token', app.globalData.token);
      wx.setStorageSync('userId', app.globalData.userId);
      wx.setStorageSync('isGuest', false);
      self.setData({ hasUserInfo: true, userInfo: mockUserInfo, loading: false });
      wx.showToast({ title: '登录成功', icon: 'success' });
      setTimeout(() => {
        const pages = getCurrentPages();
        if (pages.length > 1) {
          wx.navigateBack();
        } else {
          wx.switchTab({ url: '/pages/index/index' });
        }
      }, 1000);
    }, 500);
  },

  // 打开/关闭账号登录弹窗
  openPhoneLogin() {
    this.setData({ showPhoneModal: true });
  },
  closePhoneLogin() {
    if (!this.data.submitLoading) {
      this.setData({ showPhoneModal: false });
    }
  },

  // 输入处理
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },
  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  // 提交账号登录
  onSubmitAccountLogin() {
    const app = getApp();
    const { username, password } = this.data;
    if (!username || !password) {
      wx.showToast({ title: '请输入账号和密码', icon: 'none' });
      return;
    }
    this.setData({ submitLoading: true });
    app.request({
      url: '/auth/login',
      method: 'POST',
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      data: { username, password }
    }).then((res) => {
      // 后端返回 ApiResultResponse.ok("登录成功", jwt)
      const token = res.data;
      wx.setStorageSync('token', token);
      app.globalData.token = token;
      // 生成基础用户信息（后端账号登录未返回用户信息）
      const basicUserInfo = {
        nickName: username,
        avatarUrl: '/images/default-avatar.svg'
      };
      wx.setStorageSync('userInfo', basicUserInfo);
      app.globalData.userInfo = basicUserInfo;
      this.setData({ hasUserInfo: true, userInfo: basicUserInfo, submitLoading: false, showPhoneModal: false });
      wx.showToast({ title: '登录成功', icon: 'success' });
      setTimeout(() => {
        const pages = getCurrentPages();
        if (pages.length > 1) {
          wx.navigateBack();
        } else {
          wx.switchTab({ url: '/pages/index/index' });
        }
      }, 800);
    }).catch((err) => {
      this.setData({ submitLoading: false });
      wx.showToast({ title: err.message || '登录失败', icon: 'none' });
    });
  },

  // 用户协议
  onUserAgreement: function () {
    wx.showModal({
      title: '用户协议',
      content: '这里是用户协议的内容...',
      showCancel: false
    });
  },

  // 隐私政策
  onPrivacyPolicy: function () {
    wx.showModal({
      title: '隐私政策',
      content: '这里是隐私政策的内容...',
      showCancel: false
    });
  }
});