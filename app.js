const SUCCESS_CODES = new Set([0, 200]);
const CUSTOM_API_STORAGE_KEY = 'customApiBaseUrl';

// 根据小程序环境划分的默认接口域名，可按需替换为真实地址
const DEFAULT_BASE_URLS = {
  develop: 'http://127.0.0.1:8080/api',
  trial: 'https://trial-api.wechatmall.example.com/api',
  release: 'https://api.wechatmall.example.com/api'
};

const sanitizeBaseUrl = (url) => {
  if (typeof url !== 'string') {
    return '';
  }

  const trimmed = url.trim();
  if (!trimmed) {
    return '';
  }

  return trimmed.replace(/\/$/, '');
};

const extractEnvVersion = () => {
  if (typeof wx === 'undefined' || typeof wx.getAccountInfoSync !== 'function') {
    return 'develop';
  }

  try {
    const accountInfo = wx.getAccountInfoSync() || {};
    return accountInfo?.miniProgram?.envVersion || 'develop';
  } catch (error) {
    console.warn('读取小程序环境信息失败', error);
    return 'develop';
  }
};

const resolveDefaultBaseUrl = () => {
  const envVersion = extractEnvVersion();
  return DEFAULT_BASE_URLS[envVersion] || DEFAULT_BASE_URLS.develop;
};

const readStoredBaseUrl = () => {
  if (typeof wx === 'undefined' || typeof wx.getStorageSync !== 'function') {
    return '';
  }

  try {
    return sanitizeBaseUrl(wx.getStorageSync(CUSTOM_API_STORAGE_KEY));
  } catch (error) {
    console.warn('读取自定义接口域名失败', error);
    return '';
  }
};

const determineInitialBaseUrl = () => {
  const stored = readStoredBaseUrl();
  if (stored) {
    return stored;
  }

  return sanitizeBaseUrl(resolveDefaultBaseUrl());
};

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

    const initialBaseUrl = determineInitialBaseUrl();
    if (initialBaseUrl) {
      this.updateApiBaseUrl(initialBaseUrl);
    }

    this.globalData.httpClient = createHttpClient(this);
    if (typeof this.userInfoReadyCallback === 'function') {
      this.userInfoReadyCallback();
    }
  },

  globalData: {
    userInfo: null,
    apiBase: '',
    apiBaseUrl: '',
    apiUrl: '',
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

    this.ensureHttpClient();

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

  ensureHttpClient() {
    if (!this.globalData.apiBaseUrl) {
      const fallbackBaseUrl = determineInitialBaseUrl();
      if (fallbackBaseUrl) {
        this.updateApiBaseUrl(fallbackBaseUrl);
      }
    }

    if (!this.globalData.httpClient) {
      this.globalData.httpClient = createHttpClient(this);
    }
  },

  updateApiBaseUrl(baseUrl) {
    const sanitized = sanitizeBaseUrl(baseUrl);
    if (!sanitized) {
      console.warn('设置的接口基础地址为空，将保持现有配置');
      return;
    }

    this.globalData.apiBase = sanitized;
    this.globalData.apiBaseUrl = sanitized;
    this.globalData.apiUrl = sanitized;

    if (this.globalData.httpClient) {
      this.globalData.httpClient = createHttpClient(this);
    }
  },

  setApiBaseUrl(baseUrl, options = {}) {
    const sanitized = sanitizeBaseUrl(baseUrl);
    if (!sanitized) {
      this.resetApiBaseUrl();
      return;
    }

    try {
      const shouldPersist = !options || options.persist !== false;
      if (shouldPersist && typeof wx !== 'undefined' && typeof wx.setStorageSync === 'function') {
        wx.setStorageSync(CUSTOM_API_STORAGE_KEY, sanitized);
      }
    } catch (error) {
      console.warn('保存自定义接口域名失败', error);
    }

    this.updateApiBaseUrl(sanitized);
  },

  resetApiBaseUrl() {
    const defaultBaseUrl = determineInitialBaseUrl();

    try {
      if (typeof wx !== 'undefined' && typeof wx.removeStorageSync === 'function') {
        wx.removeStorageSync(CUSTOM_API_STORAGE_KEY);
      }
    } catch (error) {
      console.warn('清除自定义接口域名失败', error);
    }

    if (defaultBaseUrl) {
      this.updateApiBaseUrl(defaultBaseUrl);
    }
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

