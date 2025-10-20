// pages/order/detail/detail.js
const logisticsUtil = require('../../../utils/logistics.js');

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
    if (options.orderId) {
      this.setData({
        orderId: options.orderId
      });
      this.loadOrderDetail();
    }
  },

  // 加载订单详情
  loadOrderDetail: function () {
    const app = getApp();
    this.setData({ loading: true });

    // 模拟API调用
    setTimeout(() => {
      const mockOrder = {
        id: this.data.orderId,
        status: 'shipped',
        statusText: '待收货',
        createTime: '2024-01-15 14:30:00',
        payTime: '2024-01-15 14:32:15',
        totalAmount: 299.00,
        freight: 10.00,
        discount: 20.00,
        actualAmount: 289.00,
        payMethod: '微信支付',
        orderNo: 'WX' + Date.now(),
        
        // 收货地址
        address: {
          name: '张三',
          phone: '138****8888',
          region: '广东省 深圳市 南山区',
          detail: '科技园南区深南大道10000号'
        },
        
        // 商品列表
        items: [
          {
            id: 1,
            name: 'iPhone 15 Pro Max',
            image: 'https://via.placeholder.com/300x300',
            price: 149.00,
            quantity: 2,
            specs: '深空黑色 256GB'
          }
        ],
        
        // 物流信息
        logistics: {
          company: '顺丰速运',
          trackingNo: 'SF1234567890',
          status: '运输中',
          updateTime: '2024-01-16 10:30:00'
        }
      };

      this.setData({
        order: mockOrder,
        loading: false
      });
      
      // 如果订单有物流信息且状态为已发货或已完成，启动物流状态订阅
      if (mockOrder.logistics && logisticsUtil.shouldShowLogistics(mockOrder.status)) {
        this.startLogisticsSubscription();
      }
    }, 1000);
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
    const self = this;
    wx.showModal({
      title: '取消订单',
      content: '确定要取消这个订单吗？',
      success: function (res) {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' });
          
          // 模拟API调用
          setTimeout(() => {
            wx.hideLoading();
            wx.showToast({
              title: '订单已取消',
              icon: 'success'
            });
            
            // 更新订单状态
            self.setData({
              'order.status': 'cancelled',
              'order.statusText': '已取消'
            });
          }, 1500);
        }
      }
    });
  },

  // 去付款
  onPayOrder: function () {
    wx.showLoading({ title: '调起支付...' });
    
    // 模拟支付
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({
        title: '支付成功',
        icon: 'success'
      });
      
      // 更新订单状态
      this.setData({
        'order.status': 'paid',
        'order.statusText': '待发货',
        'order.payTime': new Date().toLocaleString()
      });
    }, 2000);
  },

  // 确认收货
  onConfirmReceive: function () {
    const self = this;
    wx.showModal({
      title: '确认收货',
      content: '确认已收到商品吗？',
      success: function (res) {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' });
          
          // 模拟API调用
          setTimeout(() => {
            wx.hideLoading();
            wx.showToast({
              title: '确认收货成功',
              icon: 'success'
            });
            
            // 更新订单状态
            self.setData({
              'order.status': 'completed',
              'order.statusText': '已完成'
            });
          }, 1500);
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
    wx.navigateTo({
      url: '/pages/product/detail/detail?id=' + this.data.order.items[0].id
    });
  },

  // 启动物流状态订阅
  startLogisticsSubscription: function () {
    const self = this;
    if (this.data.logisticsUpdateInterval) {
      logisticsUtil.unsubscribeLogisticsUpdate(this.data.logisticsUpdateInterval);
    }
    
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
    if (this.data.order && this.data.order.logistics && 
        logisticsUtil.shouldShowLogistics(this.data.order.status)) {
      this.startLogisticsSubscription();
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
    this.loadOrderDetail();
    wx.stopPullDownRefresh();
  }
});