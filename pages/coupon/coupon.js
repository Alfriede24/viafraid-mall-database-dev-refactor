// pages/coupon/coupon.js
Page({
  data: {
    tabs: ['未使用', '已使用', '已过期'],
    activeTab: 0,
    coupons: [],
    loading: true
  },

  onLoad(options) {
    this.loadCoupons(this.data.activeTab);
  },

  onTabClick(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ activeTab: index });
    this.loadCoupons(index);
  },

  loadCoupons(tabIndex) {
    this.setData({ loading: true });

    // 模拟数据加载
    setTimeout(() => {
      // 完整的模拟数据
      const allCoupons = [
        { id: 1, name: '新人专享券', description: '满100元可用', value: 20, validPeriod: '2025.10.31', status: 'unused' },
        { id: 2, name: '全场通用券', description: '满200元可用', value: 50, validPeriod: '2025.12.31', status: 'unused' },
        { id: 3, name: '会员生日礼券', description: '无门槛', value: 100, validPeriod: '2025.08.31', status: 'used' },
        { id: 4, name: '活动体验券', description: '满50元可用', value: 10, validPeriod: '2025.07.31', status: 'expired' }
      ];

      // 根据tabIndex筛选优惠券
      const statusMap = ['unused', 'used', 'expired'];
      const currentStatus = statusMap[tabIndex];
      const filteredCoupons = allCoupons.filter(coupon => coupon.status === currentStatus);

      this.setData({
        coupons: filteredCoupons,
        loading: false
      });
    }, 500);
  }
});