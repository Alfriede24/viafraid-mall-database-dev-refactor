// pages/favorite/favorite.js
const app = getApp();

Page({
  data: {
    favorites: [],
    loading: true
  },

  onShow() {
    this.loadFavorites();
  },

  async loadFavorites() {
    this.setData({ loading: true });

    try {
      const res = await app.request({
        url: '/miniapp/member/favorites',
        method: 'GET',
        silent: true
      });

      const list = (res?.data || []).map(item => ({
        id: item.id,
        productId: item.productId,
        name: item.name,
        price: Number(item.price || 0).toFixed(2),
        image: item.image,
        available: item.available
      }));

      this.setData({ favorites: list, loading: false });
    } catch (error) {
      this.setData({ favorites: [], loading: false });
      wx.showToast({ title: error?.message || '加载收藏失败', icon: 'none' });
    } finally {
      wx.stopPullDownRefresh();
    }
  },

  onProductTap(e) {
    const { productId } = e.currentTarget.dataset;
    if (!productId) {
      return;
    }
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${productId}`
    });
  },

  onCancelFavorite(e) {
    const { favoriteId } = e.currentTarget.dataset;
    if (!favoriteId) {
      return;
    }

    wx.showModal({
      title: '提示',
      content: '确定要取消收藏吗？',
      success: async (res) => {
        if (!res.confirm) {
          return;
        }

        try {
          await app.request({
            url: `/miniapp/member/favorites/${favoriteId}`,
            method: 'DELETE'
          });
          wx.showToast({ title: '已取消收藏' });
          this.loadFavorites();
        } catch (error) {
          wx.showToast({ title: error?.message || '操作失败', icon: 'none' });
        }
      }
    });
  },

  onPullDownRefresh() {
    this.loadFavorites();
  }
});