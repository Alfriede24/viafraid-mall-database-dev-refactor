// utils/logistics.js
// 物流相关工具函数

const app = getApp();

/**
 * 物流状态映射
 */
const LOGISTICS_STATUS = {
  PENDING: '待发货',
  SHIPPED: '运输中',
  DELIVERING: '派件中',
  DELIVERED: '已签收',
  EXCEPTION: '异常',
  RETURNED: '退回'
};

/**
 * 获取物流信息
 * @param {string} company 快递公司
 * @param {string} trackingNo 运单号
 * @returns {Promise} 物流信息
 */
function getLogisticsInfo(company, trackingNo) {
  return new Promise((resolve, reject) => {
    // 这里应该调用后端API
    // wx.request({
    //   url: `${app.globalData.apiUrl}/api/logistics/track`,
    //   method: 'GET',
    //   data: {
    //     company: company,
    //     trackingNo: trackingNo
    //   },
    //   success: (res) => {
    //     if (res.data.success) {
    //       resolve(res.data.data);
    //     } else {
    //       reject(res.data.message);
    //     }
    //   },
    //   fail: (err) => {
    //     reject(err);
    //   }
    // });
    
    // 模拟API调用
    setTimeout(() => {
      const mockData = {
        company: company,
        trackingNo: trackingNo,
        status: LOGISTICS_STATUS.SHIPPED,
        updateTime: new Date().toLocaleString(),
        recipient: {
          name: '张**',
          phone: '138****8888',
          address: '广东省深圳市南山区科技园南区深南大道10000号'
        },
        tracks: [
          {
            id: 1,
            description: '快件已到达【深圳南山营业点】',
            time: new Date().toLocaleString(),
            location: '深圳市'
          },
          {
            id: 2,
            description: '快件离开【深圳转运中心】已发往【深圳南山营业点】',
            time: new Date(Date.now() - 2 * 60 * 60 * 1000).toLocaleString(),
            location: '深圳市'
          }
        ]
      };
      resolve(mockData);
    }, 1000);
  });
}

/**
 * 订阅物流状态更新
 * @param {string} orderId 订单ID
 * @param {function} callback 回调函数
 */
function subscribeLogisticsUpdate(orderId, callback) {
  // 这里可以实现WebSocket连接或定时轮询
  const interval = setInterval(() => {
    // 模拟状态更新
    const randomUpdate = Math.random() > 0.8; // 20%概率有更新
    if (randomUpdate) {
      callback({
        orderId: orderId,
        status: LOGISTICS_STATUS.DELIVERING,
        updateTime: new Date().toLocaleString(),
        newTrack: {
          id: Date.now(),
          description: '快件正在派送中，请保持电话畅通',
          time: new Date().toLocaleString(),
          location: '深圳市'
        }
      });
    }
  }, 30000); // 30秒检查一次
  
  return interval;
}

/**
 * 取消订阅物流状态更新
 * @param {number} intervalId 定时器ID
 */
function unsubscribeLogisticsUpdate(intervalId) {
  if (intervalId) {
    clearInterval(intervalId);
  }
}

/**
 * 格式化物流状态
 * @param {string} status 状态
 * @returns {object} 格式化后的状态信息
 */
function formatLogisticsStatus(status) {
  const statusConfig = {
    [LOGISTICS_STATUS.PENDING]: {
      color: '#ff9500',
      icon: '/images/order-pending.png'
    },
    [LOGISTICS_STATUS.SHIPPED]: {
      color: '#007aff',
      icon: '/images/truck.png'
    },
    [LOGISTICS_STATUS.DELIVERING]: {
      color: '#ff6b35',
      icon: '/images/package.png'
    },
    [LOGISTICS_STATUS.DELIVERED]: {
      color: '#34c759',
      icon: '/images/order-completed.png'
    },
    [LOGISTICS_STATUS.EXCEPTION]: {
      color: '#ff3b30',
      icon: '/images/order-cancelled.png'
    },
    [LOGISTICS_STATUS.RETURNED]: {
      color: '#8e8e93',
      icon: '/images/order-cancelled.png'
    }
  };
  
  return statusConfig[status] || {
    color: '#8e8e93',
    icon: '/images/order.png'
  };
}

/**
 * 检查是否需要显示物流信息
 * @param {string} orderStatus 订单状态
 * @returns {boolean} 是否显示
 */
function shouldShowLogistics(orderStatus) {
  return ['shipped', 'completed'].includes(orderStatus);
}

/**
 * 发送物流状态变更通知
 * @param {object} logisticsData 物流数据
 */
function sendLogisticsNotification(logisticsData) {
  // 发送模板消息或推送通知
  wx.showToast({
    title: `物流状态更新：${logisticsData.status}`,
    icon: 'none',
    duration: 3000
  });
}

module.exports = {
  LOGISTICS_STATUS,
  getLogisticsInfo,
  subscribeLogisticsUpdate,
  unsubscribeLogisticsUpdate,
  formatLogisticsStatus,
  shouldShowLogistics,
  sendLogisticsNotification
};