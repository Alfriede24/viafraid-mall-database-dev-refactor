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

  onLoad() {
    this.loadSummary();
    this.loadHistory(true);
  },

  async loadSummary() {
    try {
      const res = await app.request({
        url: '/miniapp/member/assets/points/summary',
        method: 'GET',
        silent: true
      });
      const summary = res?.data || {};
      this.setData({ totalPoints: summary.availablePoints || 0 });
    } catch (error) {
      wx.showToast({ title: error?.message || '加载积分失败', icon: 'none' });
    }
  },

  async loadHistory(refresh = false) {
    if (this.data.loading && !refresh) return;
    this.setData({ loading: true });

    const page = refresh ? 1 : this.data.page;

    try {
      const res = await app.request({
        url: '/miniapp/member/assets/points/history',
        method: 'GET',
        data: { page, pageSize: this.data.pageSize },
        silent: true
      });

      const payload = res?.data || {};
      const items = payload.items || [];

      this.setData({
        history: refresh ? items : [...this.data.history, ...items],
        page: page + 1,
        hasMore: !!payload.hasNext,
        loading: false
      });
    } catch (error) {
      wx.showToast({ title: error?.message || '加载失败', icon: 'none' });
      this.setData({ loading: false });
    } finally {
      wx.stopPullDownRefresh();
    }
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