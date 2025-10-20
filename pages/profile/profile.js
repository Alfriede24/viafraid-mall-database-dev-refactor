// pages/profile/profile.js
const app = getApp();

Page({
  data: {
    userInfo: null,
    hasUserInfo: false,
    points: 0,
    // 主功能入口（带图标）
    mainActions: [
      {
        title: '订单信息',
        icon: '/images/order.svg',
        url: '/pages/order/list/list'
      },
      {
        title: '活动优惠',
        icon: '/images/coupon.svg',
        url: '/pages/coupon/coupon'
      },
      {
        title: '收藏商品',
        icon: '/images/favorite.png',
        url: '/pages/favorite/favorite'
      },
      {
        title: '在线客服',
        icon: '/images/phone-icon.svg',
        url: '/pages/customer-service/customer-service'
      }
    ],
    // 列表菜单项（无图标）
    menuItems: [
      {
        title: '地址簿',
        url: '/pages/address/list/list'
      },
      {
        title: '会员积分',
        url: '/pages/points/list/list' 
      },
      {
        title: '系统设置',
        url: '/pages/setting/setting'
      }
    ]
  },

  onShow() {
    this.checkUserInfo();
  },

  checkUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userInfo,
        hasUserInfo: true,
      });
      // 在这里可以调用加载积分的函数，如果需要的话
      // this.loadUserPoints();
    } else {
      this.setData({
        userInfo: null,
        hasUserInfo: false,
        points: 0
      });
    }
  },

  // onMenuTap, getUserProfile, onLogout, onEditProfile 等函数是此页面固有的功能
  onMenuTap(e) {
    const { url } = e.currentTarget.dataset;
    if (!url) return;

    if (!this.data.hasUserInfo) {
        wx.showModal({
            title: '尚未登录',
            content: '请先登录以使用此功能',
            confirmText: '去登录',
            cancelText: '取消',
            success: (res) => {
                if(res.confirm) {
                    this.getUserProfile();
                }
            }
        });
        return;
    }

    wx.navigateTo({ url });
  },
  
  getUserProfile() {
    // 模拟获取用户信息
    const mockUserInfo = {
      avatarUrl: '/images/default-avatar.svg',
      nickName: '微信用户'
    };
    wx.setStorageSync('userInfo', mockUserInfo);
    this.setData({
      userInfo: mockUserInfo,
      hasUserInfo: true
    });
    wx.showToast({title: '登录成功', icon: 'success'});
  },

  onLogout() {
     wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('userInfo');
          this.setData({
            userInfo: null,
            hasUserInfo: false
          });
        }
      }
    });
  },

  onEditProfile() {
    wx.navigateTo({
      url: '/pages/profile/edit/edit'
    });
  }
});