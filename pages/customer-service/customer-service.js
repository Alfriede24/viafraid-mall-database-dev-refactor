// pages/customer-service/customer-service.js
Page({
  data: {
    faqList: [],
    activeIndex: null
  },

  onLoad(options) {
    this.fetchServiceList();
  },

  // 从后端拉取客服列表
  fetchServiceList() {
    const app = getApp();
    app.request({
      url: '/costum-service/list',
      method: 'GET'
    }).then((res) => {
      const list = res.data || [];
      // 映射到页面展示结构
      const faqList = list.map(item => ({
        id: item.id,
        question: `客服编号：${item.uniqueId ?? ''}`,
        answer: `工作时间：${item.workTime ?? '暂无'}`
      }));
      this.setData({ faqList });
    }).catch((err) => {
      wx.showToast({ title: err.message || '获取客服列表失败，请先登录', icon: 'none' });
    });
  },

  // 切换问答展开/折叠
  toggleFaq(e) {
    const index = e.currentTarget.dataset.index;
    if (this.data.activeIndex === index) {
      this.setData({ activeIndex: null });
    } else {
      this.setData({ activeIndex: index });
    }
  },

  // 联系人工客服（保留原逻辑）
  contactService() {
    wx.showToast({
      title: '正在接入人工客服...',
      icon: 'none'
    });
  }
});