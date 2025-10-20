const SUCCESS_CODES = new Set([0, 200]);

function createHttpClient(appInstance) {
  const normalizeUrl = (url = '') => {
    if (!url) {
      return (appInstance?.globalData?.apiBaseUrl || '').replace(/\/$/, '');
    }

    if (/^https?:\/\//i.test(url)) {
      return url;
    }

    const base = (appInstance?.globalData?.apiBaseUrl || '').replace(/\/$/, '');
    const relativePath = url.startsWith('/') ? url : `/${url}`;
    return `${base}${relativePath}`;
  };

  const sendRequest = (method, url, data = {}, options = {}) => {
    const requestUrl = normalizeUrl(url);
    const headers = Object.assign(
      {
        'Content-Type': options.contentType || 'application/json'
      },
      options.header || {}
    );

    const token = appInstance?.globalData?.token || wx.getStorageSync('token');
    if (token && !headers.Authorization) {
      headers.Authorization = `Bearer ${token}`;
    }

    return new Promise((resolve, reject) => {
      wx.request({
        url: requestUrl,
        method,
        data,
        header: headers,
        timeout: options.timeout || 15000,
        success(res) {
          if (res.statusCode >= 200 && res.statusCode < 300) {
            resolve(res);
          } else {
            const error = new Error(`Request failed with status code ${res.statusCode}`);
            error.response = res;
            error.statusCode = res.statusCode;
            reject(error);
          }
        },
        fail(err) {
          reject(err);
        }
      });
    });
  };

  return {
    request(method, url, data = {}, options = {}) {
      return sendRequest(method, url, data, options);
    },
    get(url, options = {}) {
      return sendRequest('GET', url, options.data || {}, options);
    },
    post(url, data = {}, options = {}) {
      return sendRequest('POST', url, data, options);
    },
    put(url, data = {}, options = {}) {
      return sendRequest('PUT', url, data, options);
    },
    delete(url, data = {}, options = {}) {
      return sendRequest('DELETE', url, data, options);
    }
  };
}

const isSuccessfulResponse = (payload) => {
  if (!payload || typeof payload !== 'object') {
    return true;
  }

  if (payload.code === undefined) {
    return true;
  }

  return SUCCESS_CODES.has(payload.code);
};

App({
  onLaunch() {
    const logs = wx.getStorageSync('logs') || [];
    logs.unshift(Date.now());
    wx.setStorageSync('logs', logs);

    this.initUserInfo();

    this.globalData.httpClient = createHttpClient(this);
    if (typeof this.userInfoReadyCallback === 'function') {
      this.userInfoReadyCallback();
    }
  },

  globalData: {
    userInfo: null,
    apiBase: 'http://localhost:8080/api',
    apiBaseUrl: 'http://localhost:8080/api',
    apiUrl: 'http://localhost:8080/api',
    token: null,
    userId: null,
    isGuest: false,
    version: '1.0.0',
    httpClient: null,
    navigateToCategoryState: null
  },

  showLoading(title = '加载中...') {
    wx.showLoading({
      title,
      mask: true
    });
  },

  request(options = {}) {
    const { url, method = 'GET', data = {}, header = {}, silent = false, contentType, timeout } = options;

    if (!url) {
      return Promise.reject(new Error('请求地址不能为空'));
    }

    if (!this.globalData.httpClient) {
      this.globalData.httpClient = createHttpClient(this);
    }

    return this.globalData.httpClient
      .request(method, url, data, { header, contentType, timeout })
      .then((res) => {
        const payload = res.data;
        if (isSuccessfulResponse(payload)) {
          return payload;
        }

        if (!silent) {
          this.showError(payload?.message || '请求失败');
        }

        const error = new Error(payload?.message || '请求失败');
        error.response = res;
        error.payload = payload;
        throw error;
      })
      .catch((err) => {
        if (err && err.payload) {
          throw err;
        }

        if (!silent) {
          const message =
            err?.message ||
            (err?.statusCode ? `网络请求失败（${err.statusCode}）` : '网络连接失败');
          this.showError(message);
        }

        throw err;
      });
  },

  showError(message = '发生错误') {
    wx.showToast({
      title: message,
      icon: 'none'
    });
  },

  initUserInfo() {
    try {
      const userInfo = wx.getStorageSync('userInfo') || null;
      const token = wx.getStorageSync('token') || null;
      const userId = wx.getStorageSync('userId') || null;
      const isGuest = wx.getStorageSync('isGuest') || false;

      this.globalData.userInfo = userInfo;
      this.globalData.token = token;
      this.globalData.userId = userId;
      this.globalData.isGuest = isGuest;
    } catch (e) {
      console.warn('读取本地用户信息失败', e);
    }
  }
});
