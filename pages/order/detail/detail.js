// pages/order/detail/detail.js
const logisticsUtil = require('../../../utils/logistics.js');
const app = getApp();

Page({
  data: {
    orderId: '',
    order: null,
    loading: true,
    logisticsUpdateInterval: null,
    statusMap: {
      'pending': '待付款',
      'paid': '待发货',
      'shipped': '待收货',
      'completed': '已完成',
      'cancelled': '已取消'
    }
  },

  onLoad: function (options) {
    const orderId = options?.id || options?.orderId;
    if (orderId) {
      this.setData({ orderId });
      this.loadOrderDetail();
    }
  },

  // 加载订单详情
  loadOrderDetail: async function () {
    if (!this.data.orderId) {
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await app.request({
        url: `/miniapp/orders/${this.data.orderId}`,
        method: 'GET'
      });

      const order = res?.data || null;
      this.setData({ order, loading: false });

      if (order && order.logistics && logisticsUtil.shouldShowLogistics(order.status)) {
        this.startLogisticsSubscription(order);
      } else {
        this.stopLogisticsSubscription();
      }
    } catch (error) {
      this.setData({ loading: false, order: null });
      wx.showToast({ title: error?.message || '加载订单失败', icon: 'none' });
    }
  },

  // 复制订单号
  onCopyOrderNo: function () {
    wx.setClipboardData({
      data: this.data.order.orderNo,
      success: function () {
        wx.showToast({
          title: '订单号已复制',
          icon: 'success'
        });
      }
    });
  },

  // 复制运单号
  onCopyTrackingNo: function () {
    const logistics = this.data.order.logistics;
    if (logistics && logistics.trackingNo) {
      wx.setClipboardData({
        data: logistics.trackingNo,
        success: function () {
          wx.showToast({
            title: '运单号已复制',
            icon: 'success'
          });
        }
      });
    }
  },

  // 联系客服
  onContactService: function () {
    wx.showModal({
      title: '联系客服',
      content: '是否拨打客服电话：400-123-4567？',
      success: function (res) {
        if (res.confirm) {
          wx.makePhoneCall({
            phoneNumber: '4001234567'
          });
        }
      }
    });
  },

  // 取消订单
  onCancelOrder: function () {
    if (!this.data.orderId) {
      return;
    }

    wx.showModal({
      title: '取消订单',
      content: '确定要取消这个订单吗？',
      success: async (res) => {
        if (!res.confirm) {
          return;
        }

        try {
          wx.showLoading({ title: '处理中...' });
          const resp = await app.request({
            url: `/miniapp/orders/${this.data.orderId}/cancel`,
            method: 'POST'
          });
          wx.hideLoading();

          const updated = resp?.data || null;
          this.setData({ order: updated });
          wx.showToast({ title: '订单已取消', icon: 'success' });
          this.stopLogisticsSubscription();
        } catch (error) {
          wx.hideLoading();
          wx.showToast({ title: error?.message || '取消失败', icon: 'none' });
        }
      }
    });
  },

  // 去付款
  onPayOrder: function () {
    if (!this.data.orderId) {
      return;
    }

    wx.showLoading({ title: '调起支付...' });

    app.request({
      url: `/miniapp/orders/${this.data.orderId}/pay`,
      method: 'POST'
    }).then((res) => {
      wx.hideLoading();
      const updated = res?.data || null;
      this.setData({ order: updated });
      wx.showToast({ title: '支付成功', icon: 'success' });
      if (updated && updated.logistics && logisticsUtil.shouldShowLogistics(updated.status)) {
        this.startLogisticsSubscription(updated);
      }
    }).catch((error) => {
      wx.hideLoading();
      wx.showToast({ title: error?.message || '支付失败', icon: 'none' });
    });
  },

  // 确认收货
  onConfirmReceive: function () {
    if (!this.data.orderId) {
      return;
    }

    wx.showModal({
      title: '确认收货',
      content: '确认已收到商品吗？',
      success: async (res) => {
        if (!res.confirm) {
          return;
        }

        try {
          wx.showLoading({ title: '处理中...' });
          const resp = await app.request({
            url: `/miniapp/orders/${this.data.orderId}/confirm`,
            method: 'POST'
          });
          wx.hideLoading();

          const updated = resp?.data || null;
          this.setData({ order: updated });
          wx.showToast({ title: '确认收货成功', icon: 'success' });
          this.stopLogisticsSubscription();
        } catch (error) {
          wx.hideLoading();
          wx.showToast({ title: error?.message || '操作失败', icon: 'none' });
        }
      }
    });
  },

  // 查看物流
  onViewLogistics: function () {
    const logistics = this.data.order.logistics;
    if (logistics && logistics.company && logistics.trackingNo) {
      wx.navigateTo({
        url: `/pages/logistics/logistics?company=${logistics.company}&trackingNo=${logistics.trackingNo}`
      });
    } else {
      wx.showToast({
        title: '暂无物流信息',
        icon: 'none'
      });
    }
  },

  // 申请售后
  onApplyAfterSale: function () {
    wx.showToast({
      title: '申请售后功能开发中',
      icon: 'none'
    });
  },

  // 再次购买
  onBuyAgain: function () {
    const firstItem = this.data.order?.items?.[0];
    if (!firstItem) {
      return;
    }
    wx.navigateTo({
      url: '/pages/product/detail/detail?id=' + firstItem.id
    });
  },

  // 启动物流状态订阅
  startLogisticsSubscription: function (orderData) {
    const self = this;
    if (this.data.logisticsUpdateInterval) {
      logisticsUtil.unsubscribeLogisticsUpdate(this.data.logisticsUpdateInterval);
    }

    const logistics = orderData?.logistics || this.data.order?.logistics || {};
    const intervalId = logisticsUtil.subscribeLogisticsUpdate(
      this.data.orderId,
      function (updateData) {
        // 更新物流状态
        self.setData({
          'order.logistics.status': updateData.status,
          'order.logistics.updateTime': updateData.updateTime
        });

        // 发送通知
        logisticsUtil.sendLogisticsNotification(updateData);
      },
      {
        company: logistics.company,
        trackingNo: logistics.trackingNo
      }
    );
    
    this.setData({
      logisticsUpdateInterval: intervalId
    });
  },

  // 停止物流状态订阅
  stopLogisticsSubscription: function () {
    if (this.data.logisticsUpdateInterval) {
      logisticsUtil.unsubscribeLogisticsUpdate(this.data.logisticsUpdateInterval);
      this.setData({
        logisticsUpdateInterval: null
      });
    }
  },

  // 页面显示时
  onShow: function () {
    // 如果有物流信息，重新启动订阅
    if (this.data.order && this.data.order.logistics && logisticsUtil.shouldShowLogistics(this.data.order.status)) {
      this.startLogisticsSubscription(this.data.order);
    }
  },

  // 页面隐藏时
  onHide: function () {
    this.stopLogisticsSubscription();
  },

  // 页面卸载时
  onUnload: function () {
    this.stopLogisticsSubscription();
  },

  // 下拉刷新
  onPullDownRefresh: function () {
    const loadPromise = this.loadOrderDetail();
    if (loadPromise && typeof loadPromise.finally === 'function') {
      loadPromise.finally(() => {
        wx.stopPullDownRefresh();
      });
    } else {
      wx.stopPullDownRefresh();
    }
  }
});