// pages/profile/edit/edit.js
const app = getApp();

Page({
  data: {
    userInfo: {
      avatarUrl: '/images/default-avatar.svg',
      nickName: '微信用户',
      phone: '138****8888',
      gender: 0
    },
    genderArray: ['未知', '男', '女'],
    genderIndex: 0,
    loading: false,
    saving: false
  },

  onLoad() {
    this.loadUserProfile();
  },

  async loadUserProfile() {
    this.setData({ loading: true });

    try {
      const res = await app.request({
        url: '/miniapp/member/profile',
        method: 'GET',
        silent: true
      });

      const profile = res?.data || {};
      const mappedInfo = {
        avatarUrl: profile.avatarUrl || '/images/default-avatar.svg',
        nickName: profile.nickname || '微信用户',
        phone: profile.phone || '未绑定电话',
        gender: typeof profile.gender === 'number' ? profile.gender : 0
      };

      this.setData({
        userInfo: mappedInfo,
        genderIndex: mappedInfo.gender,
        loading: false
      });
    } catch (error) {
      this.setData({ loading: false });
      wx.showToast({
        title: error?.message || '加载资料失败',
        icon: 'none'
      });
    }
  },

  // 更换头像
  onChangeAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          'userInfo.avatarUrl': res.tempFiles[0].tempFilePath
        });
      }
    });
  },

  // 性别选择
  onGenderChange(e) {
    this.setData({
      genderIndex: e.detail.value,
      'userInfo.gender': parseInt(e.detail.value)
    });
  },

  // 保存
  async onSave() {
    if (this.data.saving) {
      return;
    }

    const payload = {
      nickname: this.data.userInfo.nickName,
      avatarUrl: this.data.userInfo.avatarUrl,
      phone: this.data.userInfo.phone,
      gender: this.data.userInfo.gender
    };

    this.setData({ saving: true });

    try {
      const res = await app.request({
        url: '/miniapp/member/profile',
        method: 'PUT',
        data: payload
      });

      const saved = res?.data || payload;
      wx.setStorageSync('userInfo', {
        avatarUrl: saved.avatarUrl || this.data.userInfo.avatarUrl,
        nickName: saved.nickname || this.data.userInfo.nickName
      });

      wx.showToast({
        title: '保存成功',
        icon: 'success'
      });

      setTimeout(() => {
        wx.navigateBack();
      }, 800);
    } catch (error) {
      wx.showToast({
        title: error?.message || '保存失败',
        icon: 'none'
      });
    } finally {
      this.setData({ saving: false });
    }
  }
});