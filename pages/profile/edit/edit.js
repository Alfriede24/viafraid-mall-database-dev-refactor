// pages/profile/edit/edit.js
Page({
  data: {
    userInfo: {
      avatarUrl: '/images/default-avatar.svg',
      nickName: '微信用户',
      phone: '138****8888',
      gender: 1 // 0: 未知, 1: 男, 2: 女
    },
    genderArray: ['未知', '男', '女'],
    genderIndex: 0
  },

  onLoad(options) {
    this.loadUserProfile();
  },

  loadUserProfile() {
    // 模拟加载用户数据，实际应从API或缓存获取
    const mockUserInfo = {
      avatarUrl: '/images/default-avatar.svg',
      nickName: '微信用户',
      phone: '138****8888',
      gender: 1
    };
    this.setData({
      userInfo: mockUserInfo,
      genderIndex: mockUserInfo.gender
    });
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
  onSave() {
    // 在这里添加将 this.data.userInfo 保存到后端的逻辑
    console.log('保存的用户信息:', this.data.userInfo);
    wx.showToast({
      title: '保存成功',
      icon: 'success'
    });
    setTimeout(() => {
      wx.navigateBack();
    }, 1500);
  }
});