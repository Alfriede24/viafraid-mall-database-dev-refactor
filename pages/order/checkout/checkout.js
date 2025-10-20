// pages/order/checkout/checkout.js
Page({
  data: {
    shippingAddress: null,
    orderItems: [],
    totalAmount: 0,
    loading: true
  },

  onLoad(options) {
    this.options = options || {};
    this.loadCheckoutData(this.options);
  },

  onShow() {
    // 返回后刷新选中的地址
    const selected = wx.getStorageSync('selectedAddress') || null;
    if (selected) {
      this.setData({
        shippingAddress: {
          id: selected.id,
          name: selected.name,
          phone: selected.phone,
          address: [selected.region, selected.detail].filter(Boolean).join(' ')
        }
      });
    }
  },

  loadCheckoutData(options) {
    const app = getApp();
    this.setData({ loading: true });

    const selected = wx.getStorageSync('selectedAddress') || null;
    const address = selected ? {
      id: selected.id,
      name: selected.name,
      phone: selected.phone,
      address: [selected.region, selected.detail].filter(Boolean).join(' ')
    } : null;

    const { productId, quantity } = options;
    const buyQty = parseInt(quantity || 1, 10);

    if (productId) {
      // 走“立即购买”单品结算流程
      const app = getApp();
      app.request({
        url: `/common/product/${productId}`,
        method: 'GET'
      }).then((res) => {
        const p = res.data || {};
        const item = {
          id: p.id,
          name: p.name || `商品编号 ${p.id}`,
          price: Number(p.price) || 0,
          quantity: buyQty,
          image: p.imageUrl || '/images/product1.png'
        };
        const totalAmount = Number((item.price * item.quantity).toFixed(2));
        this.setData({
          shippingAddress: address,
          orderItems: [item],
          totalAmount,
          loading: false
        });
      }).catch((err) => {
        wx.showToast({ title: err.message || '加载商品失败', icon: 'none' });
        this.setData({ loading: false });
      });
    } else {
      // 未来可扩展：从购物车结算
      const cartItems = wx.getStorageSync('cartItems') || [];
      const items = cartItems.filter(ci => ci.selected).map(ci => ({
        id: ci.id,
        name: ci.name,
        price: Number(ci.price) || 0,
        quantity: Number(ci.quantity) || 1,
        image: ci.image
      }));
      const totalAmount = Number(items.reduce((sum, it) => sum + it.price * it.quantity, 0).toFixed(2));
      this.setData({
        shippingAddress: address,
        orderItems: items,
        totalAmount,
        loading: false
      });
    }
  },

  selectAddress() {
    wx.navigateTo({
      url: '/pages/address/list/list'
    });
  },

  onSubmitOrder() {
    if (!this.data.shippingAddress || !this.data.shippingAddress.id) {
      wx.showToast({
        title: '请选择收货地址',
        icon: 'none'
      });
      return;
    }
    if (!this.data.orderItems || this.data.orderItems.length === 0) {
      wx.showToast({ title: '无结算商品', icon: 'none' });
      return;
    }

    const app = getApp();
    // --- 修复点：仅在数值型 userId 时传递 ---
    const rawUserId = wx.getStorageSync('userId');
    const numericUserId = /^\d+$/.test(String(rawUserId)) ? Number(rawUserId) : undefined;
    const firstItem = this.data.orderItems[0];

    const payload = {
      amountCount: Number(this.data.totalAmount).toFixed(2),
      shipping: '0.00',
      addressId: this.data.shippingAddress.id,
      state: '待支付',
      paymentState: '未支付',
      productId: Number(firstItem.id)
    };
    if (numericUserId !== undefined) {
      payload.userId = numericUserId;
    }

    wx.showLoading({ title: '正在提交...' });
    app.request({
      url: '/client/orders',
      method: 'POST',
      data: payload
    }).then(() => {
      wx.hideLoading();
      wx.showToast({ title: '订单提交成功', icon: 'success' });
      wx.redirectTo({ url: '/pages/order/list/list?status=1' });
    }).catch((err) => {
      wx.hideLoading();
      wx.showToast({ title: err.message || '提交失败', icon: 'none' });
    });
  }
});