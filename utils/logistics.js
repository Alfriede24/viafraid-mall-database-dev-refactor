// utils/logistics.js
// 物流相关工具函数

const LOGISTICS_STATUS = {
  PENDING: '待发货',
  SHIPPED: '运输中',
  DELIVERING: '派件中',
  DELIVERED: '已签收',
  EXCEPTION: '异常',
  RETURNED: '退回'
};

const EXPRESS_STATE_MAP = {
  '0': LOGISTICS_STATUS.SHIPPED,
  '1': LOGISTICS_STATUS.SHIPPED,
  '2': LOGISTICS_STATUS.EXCEPTION,
  '3': LOGISTICS_STATUS.DELIVERED,
  '4': LOGISTICS_STATUS.RETURNED,
  '5': LOGISTICS_STATUS.DELIVERING,
  '6': LOGISTICS_STATUS.RETURNED
};

const getAppInstance = () => (typeof getApp === 'function' ? getApp() : null);

const buildApiUrl = (path) => {
  if (/^https?:\/\//i.test(path)) {
    return path;
  }

  const app = getAppInstance();
  const baseUrl = app?.globalData?.apiBaseUrl || '';
  const base = baseUrl.replace(/\/$/, '');
  const suffix = path.startsWith('/') ? path : `/${path}`;
  return `${base}${suffix}`;
};

const resolveStatusLabel = (status) => {
  if (!status) {
    return '';
  }

  if (Object.values(LOGISTICS_STATUS).includes(status)) {
    return status;
  }

  const upper = status.toString().toUpperCase();
  return LOGISTICS_STATUS[upper] || status;
};

const createMockLogistics = (company, trackingNo) => {
  const now = new Date();
  const twoHoursAgo = new Date(now.getTime() - 2 * 60 * 60 * 1000);

  return {
    company,
    trackingNo,
    status: LOGISTICS_STATUS.SHIPPED,
    updateTime: now.toLocaleString(),
    recipient: {
      name: '张**',
      phone: '138****8888',
      address: '广东省深圳市南山区科技园南区深南大道10000号'
    },
    tracks: [
      {
        id: 1,
        description: '快件已到达【深圳南山营业点】',
        time: now.toLocaleString(),
        location: '深圳市'
      },
      {
        id: 2,
        description: '快件离开【深圳转运中心】已发往【深圳南山营业点】',
        time: twoHoursAgo.toLocaleString(),
        location: '深圳市'
      }
    ],
    isMock: true
  };
};

const normalizeLogisticsData = (data, company, trackingNo) => {
  const normalized = { ...(data || {}) };
  normalized.company = normalized.company || company;
  normalized.trackingNo = normalized.trackingNo || trackingNo;

  const mappedStatus = normalized.state !== undefined ? EXPRESS_STATE_MAP[`${normalized.state}`] : undefined;
  const rawStatus = normalized.status || normalized.orderStatus || mappedStatus;
  normalized.status = resolveStatusLabel(rawStatus) || LOGISTICS_STATUS.SHIPPED;

  if (!normalized.updateTime) {
    normalized.updateTime =
      normalized.latestTime ||
      normalized.lastUpdate ||
      (Array.isArray(normalized.tracks) && normalized.tracks.length > 0
        ? normalized.tracks[0].time || normalized.tracks[0].ftime || normalized.tracks[0].datetime
        : null) ||
      new Date().toLocaleString();
  }

  if (Array.isArray(normalized.tracks)) {
    normalized.tracks = normalized.tracks.map((track, index) => ({
      id: track.id || track._id || index + 1,
      description: track.description || track.context || track.status || '',
      time: track.time || track.ftime || track.datetime || new Date().toLocaleString(),
      location: track.location || track.area || track.address || ''
    }));
  }

  normalized.isMock = !!normalized.isMock;
  return normalized;
};

const requestLogisticsFromApi = (company, trackingNo) =>
  new Promise((resolve, reject) => {
    const url = buildApiUrl('/logistics/track');

    wx.request({
      url,
      method: 'GET',
      data: { company, trackingNo },
      success: (res) => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          const payload = res.data || {};
          const responseData = payload.data !== undefined ? payload.data : payload;
          const code = payload.code;
          const isSuccess = code === undefined || code === 0 || code === 200;

          if (isSuccess) {
            resolve(responseData);
            return;
          }

          const error = new Error(payload.message || '物流查询失败');
          error.payload = payload;
          reject(error);
          return;
        }

        const error = new Error(`物流查询失败(${res.statusCode})`);
        error.statusCode = res.statusCode;
        reject(error);
      },
      fail: (err) => {
        reject(err);
      }
    });
  });

/**
 * 获取物流信息
 * @param {string} company 快递公司
 * @param {string} trackingNo 运单号
 * @returns {Promise<object>} 物流信息
 */
function getLogisticsInfo(company, trackingNo) {
  if (!company || !trackingNo) {
    return Promise.reject(new Error('快递公司和运单号不能为空'));
  }

  const app = getAppInstance();
  if (!app || !app.globalData) {
    return Promise.resolve(createMockLogistics(company, trackingNo));
  }

  return requestLogisticsFromApi(company, trackingNo)
    .then((data) => normalizeLogisticsData(data, company, trackingNo))
    .catch((error) => {
      console.warn('获取物流信息失败，返回模拟数据:', error);
      return createMockLogistics(company, trackingNo);
    });
}

/**
 * 订阅物流状态更新
 * @param {string} orderId 订单ID
 * @param {function} callback 回调函数
 * @param {object} options 附加参数，如 { company, trackingNo, interval }
 */
function subscribeLogisticsUpdate(orderId, callback, options = {}) {
  const { company, trackingNo, interval = 30000 } = options;

  const timer = setInterval(() => {
    const randomUpdate = Math.random() > 0.8;
    if (!randomUpdate) {
      return;
    }

    if (company && trackingNo) {
      getLogisticsInfo(company, trackingNo)
        .then((logistics) => {
          callback({
            orderId,
            status: logistics.status,
            updateTime: logistics.updateTime,
            data: logistics
          });
        })
        .catch(() => {
          const mock = createMockLogistics(company, trackingNo);
          callback({
            orderId,
            status: mock.status,
            updateTime: mock.updateTime,
            data: mock
          });
        });
      return;
    }

    const now = new Date().toLocaleString();
    callback({
      orderId,
      status: LOGISTICS_STATUS.DELIVERING,
      updateTime: now,
      newTrack: {
        id: Date.now(),
        description: '快件正在派送中，请保持电话畅通',
        time: now,
        location: '深圳市'
      }
    });
  }, interval);

  return timer;
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
  const label = resolveStatusLabel(status) || '未知状态';

  const statusConfig = {
    [LOGISTICS_STATUS.PENDING]: {
      color: '#ff9500',
      icon: '/images/order-pending.png',
      label: LOGISTICS_STATUS.PENDING
    },
    [LOGISTICS_STATUS.SHIPPED]: {
      color: '#007aff',
      icon: '/images/truck.png',
      label: LOGISTICS_STATUS.SHIPPED
    },
    [LOGISTICS_STATUS.DELIVERING]: {
      color: '#ff6b35',
      icon: '/images/package.png',
      label: LOGISTICS_STATUS.DELIVERING
    },
    [LOGISTICS_STATUS.DELIVERED]: {
      color: '#34c759',
      icon: '/images/order-completed.png',
      label: LOGISTICS_STATUS.DELIVERED
    },
    [LOGISTICS_STATUS.EXCEPTION]: {
      color: '#ff3b30',
      icon: '/images/order-cancelled.png',
      label: LOGISTICS_STATUS.EXCEPTION
    },
    [LOGISTICS_STATUS.RETURNED]: {
      color: '#8e8e93',
      icon: '/images/order-cancelled.png',
      label: LOGISTICS_STATUS.RETURNED
    }
  };

  return statusConfig[label] || {
    color: '#8e8e93',
    icon: '/images/order.png',
    label
  };
}

/**
 * 检查是否需要显示物流信息
 * @param {string} orderStatus 订单状态
 * @returns {boolean} 是否显示
 */
function shouldShowLogistics(orderStatus) {
  if (!orderStatus) {
    return false;
  }

  const normalized = orderStatus.toString().toLowerCase();
  const visibleStatuses = ['shipped', 'shipping', 'completed', 'delivering', 'delivered'];
  return visibleStatuses.includes(normalized);
}

/**
 * 发送物流状态变更通知
 * @param {object} logisticsData 物流数据
 */
function sendLogisticsNotification(logisticsData) {
  const statusInfo = formatLogisticsStatus(logisticsData?.status);
  wx.showToast({
    title: `物流状态更新：${statusInfo.label}`,
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
