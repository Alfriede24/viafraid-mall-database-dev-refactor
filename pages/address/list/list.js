// pages/address/list/list.js
Page({
  data: {
    addressList: []
  },

  onShow() {
    this.loadAddresses();
  },

  // 从后端加载地址列表
  loadAddresses() {
    const app = getApp();
    app.request({
      url: '/common/address/list',
      method: 'GET'
    }).then((res) => {
      // res.data 是 ApiResultResponse，实际数据在 res.data
      const list = res.data || [];
      // 将后端 Address 映射为前端展示结构
      const mapped = list.map(a => ({
        id: a.id,
        name: (wx.getStorageSync('userInfo') || {}).nickName || '收货人',
        phone: '未绑定电话',
        region: [a.province, a.city, a.district, a.town].filter(Boolean).join(' '),
        detail: [a.mainAddress, a.remark].filter(Boolean).join(' '),
        isDefault: !!a.hasAddress
      }));
      this.setData({ addressList: mapped });
    }).catch((err) => {
      wx.showToast({ title: err.message || '加载地址失败', icon: 'none' });
    });
  },

  addAddress() {
    wx.navigateTo({
      url: '/pages/address/edit/edit'
    });
  },

  editAddress(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/address/edit/edit?id=${id}`
    });
  },

  deleteAddress(e) {
    const { id } = e.currentTarget.dataset;
    wx.showModal({
      title: '提示',
      content: '确定要删除该地址吗？',
      success: (res) => {
        if (res.confirm) {
          // 在此处理删除逻辑（后端删除接口暂未接入）
          const newList = this.data.addressList.filter(item => item.id !== id);
          this.setData({ addressList: newList });
          wx.showToast({ title: '删除成功' });
        }
      }
    });
  },
  
  setDefault(e) {
    const { id } = e.currentTarget.dataset;
    const newList = this.data.addressList.map(item => {
      item.isDefault = item.id === id;
      return item;
    });
    this.setData({ addressList: newList });
    wx.showToast({ title: '设置成功' });
  },

  // 选择地址并返回
  onSelectAddress(e) {
    const { id } = e.currentTarget.dataset;
    const selected = this.data.addressList.find(item => item.id === id);
    if (selected) {
      wx.setStorageSync('selectedAddress', selected);
      wx.navigateBack();
    }
  }
});