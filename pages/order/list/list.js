// pages/order/list/list.js
Page({
  data: {
    tabs: ['全部', '待付款', '待发货', '待收货', '已完成'],
    activeTab: 0,
    orders: [],
    loading: true
  },

  onLoad(options) {
    const tabIndex = parseInt(options.status || 0);
    this.setData({ activeTab: tabIndex });
    this.loadOrders(tabIndex);
  },

  onTabClick(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ activeTab: index });
    this.loadOrders(index);
  },

  loadOrders(tabIndex) {
    this.setData({ loading: true });

    // 改为后端数据：按用户ID拉取订单
    const app = getApp();
    const rawUserId = wx.getStorageSync('userId');
    const numericUserId = /^\d+$/.test(String(rawUserId)) ? Number(rawUserId) : null;
    if (!numericUserId) {
      this.setData({ orders: [], loading: false });
      wx.showToast({ title: '请先登录后查看订单', icon: 'none' });
      return;
    }

    app.request({
      url: `/client/user-order/detail/${numericUserId}`,
      method: 'GET'
    }).then((res) => {
      const list = res.data || [];

      // 将后端返回的 UserOrderVo 列表扁平为订单列表
      const ordersRaw = (Array.isArray(list) ? list : []).flatMap(uo => {
        const userOrders = uo.orders || [];
        return userOrders.map(o => {
          // 状态映射
          const payState = (o.paymentState || '').trim();
          const state = (o.state || '').trim();
          let status = 2, statusText = '待发货';
          if (payState === '未支付' || state === '待支付') {
            status = 1; statusText = '待付款';
          } else if (state === '已发货' || state === '待收货') {
            status = 3; statusText = '待收货';
          } else if (state === '已完成' || state === '已签收') {
            status = 4; statusText = '已完成';
          }

          // 商品信息
          const product = o.product || {};
          const item = {
            id: product.id || o.id,
            name: product.name || `商品编号 ${product.id || ''}`,
            image: product.imageUrl || '/images/product1.png'
          };

          return {
            id: o.id,
            orderNo: String(o.id),
            status,
            statusText,
            items: [item],
            totalAmount: Number(o.amountCount) || 0
          };
        });
      });

      const filteredOrders = tabIndex === 0
        ? ordersRaw
        : ordersRaw.filter(order => order.status === tabIndex);

      this.setData({ orders: filteredOrders, loading: false });
    }).catch((err) => {
      wx.showToast({ title: err.message || '订单加载失败', icon: 'none' });
      this.setData({ orders: [], loading: false });
    });
  },

  onOrderDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/order/detail/detail?id=${id}`
    });
  }
});