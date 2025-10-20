// pages/points/list/list.js
const app = getApp();

Page({
  data: {
    totalPoints: 0,
    history: [],
    loading: true,
    page: 1,
    pageSize: 20,
    hasMore: true
  },

  onLoad(options) {
    this.loadSummary();
    this.loadHistory(true);
  },

  async loadSummary() {
    try {
      // 模拟API
      this.setData({ totalPoints: 1250 });
    } catch (error) {
      console.error("加载积分概要失败", error);
    }
  },

  async loadHistory(refresh = false) {
    if (this.data.loading && !refresh) return;
    this.setData({ loading: true });

    const page = refresh ? 1 : this.data.page;

    // 模拟API
    setTimeout(() => {
        const newHistory = [
            { description: "订单完成，返还积分", createdAt: "2024-05-20 10:30", points: 199 },
            { description: "积分兑换商品", createdAt: "2024-05-18 15:00", points: -500 },
            { description: "订单完成，返还积分", createdAt: "2024-05-15 12:10", points: 88 }
        ];

        this.setData({
            history: refresh ? newHistory : [...this.data.history, ...newHistory],
            page: page + 1,
            hasMore: newHistory.length >= this.data.pageSize,
            loading: false
        });
        wx.stopPullDownRefresh();
    }, 500);
  },

  onPullDownRefresh() {
    this.loadHistory(true);
  },

  onReachBottom() {
    if (this.data.hasMore) {
      this.loadHistory();
    }
  }
});