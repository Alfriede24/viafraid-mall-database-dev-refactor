// pages/favorite/favorite.js
Page({
  data: {
    favorites: [],
    loading: true
  },

  onShow() {
    this.loadFavorites();
  },

  loadFavorites() {
    this.setData({ loading: true });
    
    // 模拟API调用
    setTimeout(() => {
      this.setData({
        favorites: [
          { id: 1, name: '商品1', price: '99.00', image: '/images/product1.png' },
          { id: 2, name: '商品2', price: '199.00', image: '/images/product2.png' },
          { id: 3, name: '商品3', price: '299.00', image: '/images/product3.png' },
        ],
        loading: false
      });
      wx.stopPullDownRefresh(); // 停止下拉刷新动画
    }, 500);
  },

  onProductTap(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${id}`
    });
  },

  onCancelFavorite(e) {
    const { id } = e.currentTarget.dataset;
    wx.showModal({
      title: '提示',
      content: '确定要取消收藏吗？',
      success: (res) => {
        if (res.confirm) {
          // 模拟取消收藏
          const newList = this.data.favorites.filter(item => item.id !== id);
          this.setData({
            favorites: newList
          });
          wx.showToast({ title: '已取消收藏' });
        }
      }
    });
  },
  
  onPullDownRefresh() {
    this.loadFavorites();
  }
});