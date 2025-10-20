// pages/coupon/coupon.js
const app = getApp();

Page({
  data: {
    tabs: ['未使用', '已使用', '已过期'],
    activeTab: 0,
    coupons: [],
    loading: true
  },

  onLoad() {
    this.loadCoupons(this.data.activeTab);
  },

  onTabClick(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ activeTab: index });
    this.loadCoupons(index);
  },

  async loadCoupons(tabIndex) {
    this.setData({ loading: true });

    const statusMap = ['unused', 'used', 'expired'];
    const status = statusMap[tabIndex] || 'unused';

    try {
      const res = await app.request({
        url: '/miniapp/member/assets/coupons',
        method: 'GET',
        data: { status },
        silent: true
      });

      const list = res?.data || [];
      this.setData({ coupons: list, loading: false });
    } catch (error) {
      this.setData({ loading: false, coupons: [] });
      wx.showToast({ title: error?.message || '加载优惠券失败', icon: 'none' });
    }
  }
});