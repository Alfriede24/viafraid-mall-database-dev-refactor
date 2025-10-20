// pages/setting/setting.js
Page({
  data: {
    cacheSize: '5.2 MB',
    notifications: true,
  },
  onLoad(options) {},

  onNotificationChange(e) {
    this.setData({
      notifications: e.detail.value,
    });
    wx.showToast({
      title: e.detail.value ? '已开启通知' : '已关闭通知',
      icon: 'none',
    });
  },

  clearCache() {
    wx.showModal({
      title: '确认',
      content: `确定要清除 ${this.data.cacheSize} 的缓存吗？`,
      success: (res) => {
        if (res.confirm) {
          this.setData({ cacheSize: '0 KB' });
          wx.showToast({ title: '缓存已清除' });
        }
      },
    });
  },

  onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除登录缓存并返回“我的”页面
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          
          // 使用 reLaunch 跳转到 Tab Bar 页面，并刷新状态
          wx.reLaunch({
            url: '/pages/profile/profile',
          });
        }
      },
    });
  },

  onAboutUs() {
    // 您可以在此跳转到“关于我们”的页面
    wx.showToast({ title: '关于我们页面待创建', icon: 'none' });
  },
});