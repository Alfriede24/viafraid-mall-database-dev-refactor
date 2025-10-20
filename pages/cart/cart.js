// pages/cart/cart.js
Page({
  data: {
    cartItems: [],
    totalPrice: 0,
    selectedAll: false,
    hasSelectedItems: false,
    loading: true,
    recommendedProducts: []
  },

  onShow() {
    // 每次进入页面都从缓存加载购物车数据
    this.loadCartData();
  },

  loadCartData() {
    this.setData({ loading: true });
    
    // 核心修改：从全局缓存读取购物车数据
    const cartItems = wx.getStorageSync('cartItems') || [];
    
    // 模拟加载热销商品
    setTimeout(() => {
      this.setData({
        cartItems: cartItems, // 使用从缓存读取的数据
        recommendedProducts: [
            { id: 3, name: '商品3', price: 299.00, image: '/images/product3.png' },
            { id: 4, name: '商品4', price: 399.00, image: '/images/product4.png' },
            { id: 5, name: '商品5', price: 499.00, image: '/images/product5.png' }
        ],
        loading: false
      });
      this.calculateTotal();
    }, 200);
  },

  calculateTotal() {
    const { cartItems } = this.data;
    let totalPrice = 0;
    let selectedCount = 0;
    
    cartItems.forEach(item => {
      if (item.selected) {
        totalPrice += item.price * item.quantity;
        selectedCount++;
      }
    });
    
    this.setData({
      totalPrice: totalPrice.toFixed(2),
      selectedAll: cartItems.length > 0 && selectedCount === cartItems.length,
      hasSelectedItems: selectedCount > 0
    });
  },

  onSelectItem(e) {
    const { index } = e.currentTarget.dataset;
    const cartItems = this.data.cartItems;
    cartItems[index].selected = !cartItems[index].selected;
    
    this.setData({ cartItems });
    wx.setStorageSync('cartItems', cartItems); // 更新缓存
    this.calculateTotal();
  },

  onSelectAll() {
    const { selectedAll, cartItems } = this.data;
    const newSelectedState = !selectedAll;
    
    cartItems.forEach(item => {
      item.selected = newSelectedState;
    });
    
    this.setData({ cartItems });
    wx.setStorageSync('cartItems', cartItems); // 更新缓存
    this.calculateTotal();
  },
  
  onDeleteItem(e) {
    const { index } = e.currentTarget.dataset;
    
    wx.showModal({
      title: '提示',
      content: '确定要从购物车移除该商品吗？',
      success: (res) => {
        if (res.confirm) {
          const cartItems = this.data.cartItems;
          cartItems.splice(index, 1);
          this.setData({ cartItems });
          wx.setStorageSync('cartItems', cartItems); // 更新缓存
          this.calculateTotal();
        }
      }
    });
  },

  onProductTap(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${id}`
    });
  },

  onCouponTap() {
    wx.navigateTo({
      url: '/pages/coupon/coupon'
    });
  },
  
  onCheckout() {
    const selectedItems = this.data.cartItems.filter(item => item.selected);
    if (selectedItems.length === 0) {
      wx.showToast({ title: '请选择要结算的商品', icon: 'none' });
      return;
    }
    wx.setStorageSync('selectedCartItems', selectedItems);
    wx.navigateTo({ url: '/pages/order/checkout/checkout' });
  }
});