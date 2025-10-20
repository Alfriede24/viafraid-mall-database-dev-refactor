// 快递100工具类
const express100Utils = {
  // 快递公司编码映射
  companyMap: {
    '顺丰速运': 'shunfeng',
    'EMS': 'ems',
    '邮政包裹': 'youzhengguonei',
    '申通快递': 'shentong',
    '中通快递': 'zhongtong',
    '圆通速递': 'yuantong',
    '韵达速递': 'yunda',
    '京东快递': 'jd',
    '天天快递': 'tiantian',
    '百世快递': 'huitongkuaidi'
  },

  /**
   * 查询快递信息
   */
  async queryExpress(trackingNumber, companyCode, phone = '') {
    try {
      const response = await wx.request({
        url: `${getApp().globalData.apiBaseUrl}/api/Express100/query`,
        method: 'GET',
        data: {
          trackingNumber,
          companyCode,
          phone
        }
      });

      if (response.statusCode === 200) {
        return {
          success: true,
          data: response.data
        };
      } else {
        return {
          success: false,
          message: response.data?.message || '查询失败'
        };
      }
    } catch (error) {
      console.error('查询快递信息失败:', error);
      return {
        success: false,
        message: '网络错误，请稍后重试'
      };
    }
  },

  /**
   * 获取支持的快递公司列表
   */
  async getSupportedCompanies() {
    try {
      const response = await wx.request({
        url: `${getApp().globalData.apiBaseUrl}/api/Express100/companies`,
        method: 'GET'
      });

      if (response.statusCode === 200) {
        return {
          success: true,
          data: response.data
        };
      } else {
        return {
          success: false,
          message: '获取快递公司列表失败'
        };
      }
    } catch (error) {
      console.error('获取快递公司列表失败:', error);
      return {
        success: false,
        message: '网络错误，请稍后重试'
      };
    }
  },

  /**
   * 使用快递100插件查看物流
   */
  openExpressPlugin(trackingNumber, companyCode = '', options = {}) {
    const params = {
      num: trackingNumber,
      appName: '微信商城',
      com: companyCode,
      title: options.title || '物流信息',
      rss: options.rss !== undefined ? options.rss : 1,
      return: options.return !== undefined ? options.return : 1,
      mobile: options.mobile || '',
      name: options.name || '',
      phone: options.phone || '',
      address: options.address || '',
      backgroundColor: options.backgroundColor || '#007AFF'
    };

    // 构建插件URL
    const paramStr = Object.keys(params)
      .filter(key => params[key] !== '')
      .map(key => `${key}=${encodeURIComponent(params[key])}`)
      .join('&');

    const pluginUrl = `plugin://kdPlugin/index?${paramStr}`;

    wx.navigateTo({
      url: pluginUrl,
      fail: (error) => {
        console.error('打开快递插件失败:', error);
        wx.showToast({
          title: '打开物流信息失败',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 根据快递公司名称获取编码
   */
  getCompanyCode(companyName) {
    return this.companyMap[companyName] || '';
  },

  /**
   * 格式化快递状态
   */
  formatExpressStatus(statusCode) {
    const statusMap = {
      '0': { name: '在途', color: '#1890ff', icon: 'truck' },
      '1': { name: '揽件', color: '#52c41a', icon: 'check-circle' },
      '2': { name: '疑难', color: '#faad14', icon: 'exclamation-circle' },
      '3': { name: '已签收', color: '#52c41a', icon: 'check-circle-fill' },
      '4': { name: '退签', color: '#f5222d', icon: 'close-circle' },
      '5': { name: '派件', color: '#1890ff', icon: 'send' },
      '6': { name: '退回', color: '#fa8c16', icon: 'undo' }
    };

    return statusMap[statusCode] || { name: '未知', color: '#d9d9d9', icon: 'question' };
  },

  /**
   * 显示快递查询结果
   */
  showExpressResult(result) {
    if (!result.success) {
      wx.showToast({
        title: result.message,
        icon: 'none'
      });
      return;
    }

    const expressData = result.data;
    const status = this.formatExpressStatus(expressData.state);
    
    wx.showModal({
      title: '物流信息',
      content: `快递单号：${expressData.nu}\n快递状态：${status.name}\n最新动态：${expressData.data[0]?.context || '暂无'}`,
      showCancel: true,
      cancelText: '关闭',
      confirmText: '查看详情',
      success: (res) => {
        if (res.confirm) {
          this.openExpressPlugin(expressData.nu, expressData.com);
        }
      }
    });
  }
};

module.exports = express100Utils;