// pages/logistics/logistics.js
const express100Utils = require('../../utils/express100.js');

Page({
  data: {
    trackingNo: '',
    companyIndex: 0,
    companies: [],
    logisticsData: null,
    loading: false,
    searched: false
  },

  onLoad: function (options) {
    // 加载支持的快递公司列表
    this.loadSupportedCompanies();
    
    // 如果从订单详情页面跳转过来，自动填充信息
    if (options.company && options.trackingNo) {
      this.setData({
        trackingNo: options.trackingNo
      });
      // 查找对应的快递公司并自动查询
      this.findCompanyByName(options.company).then(() => {
        this.onSearch();
      });
    }
  },

  // 加载支持的快递公司列表
  async loadSupportedCompanies() {
    wx.showLoading({ title: '加载中...' });
    
    try {
      const result = await express100Utils.getSupportedCompanies();
      if (result.success && result.data && result.data.length > 0) {
        this.setData({
          companies: result.data
        });
      } else {
        // 使用默认公司列表作为备选
        this.setData({
          companies: [
            { name: '顺丰速运', code: 'shunfeng' },
            { name: '圆通速递', code: 'yuantong' },
            { name: '中通快递', code: 'zhongtong' },
            { name: '申通快递', code: 'shentong' },
            { name: '韵达速递', code: 'yunda' },
            { name: '百世快递', code: 'huitongkuaidi' },
            { name: '京东快递', code: 'jd' },
            { name: 'EMS', code: 'ems' }
          ]
        });
      }
    } catch (error) {
      console.error('加载快递公司列表失败:', error);
      // 使用默认列表
      this.setData({
        companies: [
          { name: '顺丰速运', code: 'shunfeng' },
          { name: '圆通速递', code: 'yuantong' },
          { name: '中通快递', code: 'zhongtong' },
          { name: '申通快递', code: 'shentong' },
          { name: '韵达速递', code: 'yunda' },
          { name: '百世快递', code: 'huitongkuaidi' },
          { name: '京东快递', code: 'jd' },
          { name: 'EMS', code: 'ems' }
        ]
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 根据快递公司名称查找索引
  async findCompanyByName(companyName) {
    const companies = this.data.companies;
    const index = companies.findIndex(item => 
      item.name === companyName || item.name.includes(companyName)
    );
    if (index >= 0) {
      this.setData({ companyIndex: index });
    }
  },

  // 快递公司选择
  onCompanyChange: function (e) {
    this.setData({
      companyIndex: parseInt(e.detail.value)
    });
  },

  // 运单号输入
  onTrackingNoInput: function (e) {
    this.setData({
      trackingNo: e.detail.value
    });
  },

  // 查询物流
  async onSearch() {
    if (!this.data.trackingNo.trim()) {
      wx.showToast({
        title: '请输入运单号',
        icon: 'none'
      });
      return;
    }

    if (this.data.companies.length === 0) {
      wx.showToast({
        title: '快递公司列表加载中，请稍后重试',
        icon: 'none'
      });
      return;
    }

    this.setData({
      loading: true,
      searched: true,
      logisticsData: null
    });

    const company = this.data.companies[this.data.companyIndex];
    
    try {
      // 使用快递100 API查询
      const result = await express100Utils.queryExpress(
        this.data.trackingNo,
        company.code
      );
      
      if (result.success) {
        this.setData({
          logisticsData: this.formatLogisticsData(result.data, company),
          loading: false
        });
      } else {
        throw new Error(result.message || '查询失败');
      }
    } catch (error) {
      console.error('查询物流信息失败:', error);
      this.setData({
        loading: false
      });
      wx.showToast({
        title: error.message || '查询失败，请重试',
        icon: 'none',
        duration: 2000
      });
    }
  },

  // 格式化物流数据
  formatLogisticsData(apiData, company) {
    const statusMap = {
      '0': '在途',
      '1': '揽件', 
      '2': '疑难',
      '3': '已签收',
      '4': '退签',
      '5': '派件',
      '6': '退回'
    };

    return {
      company: company.name,
      trackingNo: apiData.nu || this.data.trackingNo,
      status: statusMap[apiData.state] || '未知',
      updateTime: new Date().toLocaleString(),
      recipient: {
        name: '收件人',
        phone: '联系电话',
        address: '收件地址'
      },
      tracks: (apiData.data || []).map((track, index) => ({
        id: index + 1,
        description: track.context || '',
        time: track.time || '',
        location: track.location || ''
      }))
    };
  },

  // 使用快递100插件查看详细物流
  onViewDetail() {
    if (this.data.logisticsData) {
      const company = this.data.companies[this.data.companyIndex];
      express100Utils.openExpressPlugin(
        this.data.trackingNo,
        company.code,
        {
          title: '物流详情',
          appName: '微信商城'
        }
      );
    } else {
      wx.showToast({
        title: '请先查询物流信息',
        icon: 'none'
      });
    }
  },

  // 分享功能
  onShareAppMessage: function () {
    return {
      title: '物流查询',
      path: '/pages/logistics/logistics'
    };
  },

  // 下拉刷新
  onPullDownRefresh: function () {
    if (this.data.logisticsData) {
      this.onSearch();
    }
    wx.stopPullDownRefresh();
  }
});