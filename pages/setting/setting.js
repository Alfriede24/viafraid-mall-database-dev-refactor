// pages/setting/setting.js
const app = getApp();

const readStoredApiBaseUrl = () => {
  try {
    return wx.getStorageSync('customApiBaseUrl') || '';
  } catch (error) {
    console.warn('读取自定义接口地址失败', error);
    return '';
  }
};

Page({
  data: {
    cacheSize: '5.2 MB',
    notifications: true,
    apiBaseUrl: '',
    customApiBaseUrl: '',
    savingApiBase: false,
  },

  onLoad() {
    this.refreshApiBaseUrl();
  },

  onShow() {
    this.refreshApiBaseUrl();
  },

  refreshApiBaseUrl() {
    app?.ensureHttpClient?.();
    const apiBaseUrl = app?.globalData?.apiBaseUrl || '';
    const customApiBaseUrl = readStoredApiBaseUrl();

    this.setData({
      apiBaseUrl,
      customApiBaseUrl,
    });
  },

  onApiBaseInput(e) {
    this.setData({
      customApiBaseUrl: e.detail.value,
    });
  },

  onSaveApiBase() {
    if (this.data.savingApiBase) {
      return;
    }

    const inputValue = (this.data.customApiBaseUrl || '').trim();
    if (!inputValue) {
      wx.showToast({ title: '请输入完整的接口地址', icon: 'none' });
      return;
    }

    if (!/^https?:\/\//i.test(inputValue)) {
      wx.showToast({ title: '接口地址需以 http(s) 开头', icon: 'none' });
      return;
    }

    this.setData({ savingApiBase: true });

    try {
      app?.setApiBaseUrl?.(inputValue, { persist: true });
      this.refreshApiBaseUrl();
      wx.showToast({ title: '接口地址已保存', icon: 'success' });
    } catch (error) {
      console.error('保存接口地址失败', error);
      wx.showToast({ title: '保存失败，请重试', icon: 'none' });
    } finally {
      this.setData({ savingApiBase: false });
    }
  },

  onResetApiBase() {
    app?.resetApiBaseUrl?.();
    this.refreshApiBaseUrl();
    wx.showToast({ title: '已恢复默认接口', icon: 'none' });
  },

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
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');

          wx.reLaunch({
            url: '/pages/profile/profile',
          });
        }
      },
    });
  },

  onAboutUs() {
    wx.showToast({ title: '关于我们页面待创建', icon: 'none' });
  },
});
