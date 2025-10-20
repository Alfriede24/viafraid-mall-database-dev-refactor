// pages/address/edit/edit.js
Page({
  data: {
    formData: {
      name: '',
      phone: '',
      region: [],
      detail: '',
      isDefault: false
    }
  },

  onLoad(options) {
    if (options.id) {
      wx.setNavigationBarTitle({ title: '编辑地址' });
      // 模拟加载现有地址数据
      this.setData({
        formData: {
          name: '张三',
          phone: '13800138000',
          region: ['广东省', '深圳市', '南山区'],
          detail: '科技园南区深南大道10000号',
          isDefault: true
        }
      });
    } else {
      wx.setNavigationBarTitle({ title: '新建地址' });
    }
  },

  onRegionChange(e) {
    this.setData({
      'formData.region': e.detail.value
    });
  },

  onDefaultChange(e) {
    this.setData({
      'formData.isDefault': e.detail.value
    });
  },

  onSave() {
    // 在此处理保存逻辑
    wx.showToast({
      title: '保存成功',
      icon: 'success'
    });
    setTimeout(() => {
      wx.navigateBack();
    }, 1500);
  }
});