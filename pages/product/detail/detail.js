// pages/product/detail/detail.js
Page({
  data: {
    product: null,
    loading: true,
    quantity: 1,
    selectedSpec: {},
    specs: []
  },

  onLoad(options) {
    const productId = options.id
    if (productId) {
      this.loadProductDetail(productId)
    }
  },

  loadProductDetail(productId) {
    this.setData({ loading: true })
    
    // 模拟数据加载，实际应从API获取
    setTimeout(() => {
      // 假设这是从API获取的商品数据
      const allProducts = {
        '1': { id: 1, name: '商品1', price: 99.00, images: ['/images/product1.png'], description: '这是一个优质的商品', specs: [{ name: '颜色', options: ['红', '蓝'] }, { name: '尺寸', options: ['S', 'M', 'L'] }], stock: 100, sales: 1234 },
        '2': { id: 2, name: '商品2', price: 199.00, images: ['/images/product2.png'], description: '具有出色的品质和性能', specs: [{ name: '颜色', options: ['黑', '白'] }, { name: '尺寸', options: ['M', 'L', 'XL'] }], stock: 50, sales: 567 },
        '3': { id: 3, name: '商品3', price: 299.00, images: ['/images/product3.png'], description: '适合各种场合使用', specs: [{ name: '颜色', options: ['绿', '黄'] }, { name: '尺寸', options: ['S', 'L'] }], stock: 80, sales: 890 },
        '4': { id: 4, name: '商品4', price: 399.00, images: ['/images/product4.png'], description: '是您的理想选择', specs: [{ name: '颜色', options: ['灰', '橙'] }, { name: '尺寸', options: ['M', 'XL'] }], stock: 20, sales: 432 },
        '5': { id: 5, name: '商品5', price: 499.00, images: ['/images/product5.png'], description: '最新款上架', specs: [{ name: '颜色', options: ['紫', '粉'] }, { name: '尺寸', options: ['S', 'M'] }], stock: 120, sales: 123 }
      };
      const product = allProducts[productId] || allProducts['1']; // 如果ID不存在，则显示商品1作为默认

      this.setData({
        product,
        specs: product.specs || [],
        loading: false
      });
    }, 500);
  },
  
  // --- 核心修改：实现真正的加入购物车逻辑 ---
  onAddToCart() {
    const { product, quantity, selectedSpec } = this.data;

    // 1. 检查规格是否已选
    if (product.specs && product.specs.length > 0) {
      const requiredSpecs = product.specs.map(s => s.name);
      const isAllSpecSelected = requiredSpecs.every(specName => selectedSpec[specName]);
      if (!isAllSpecSelected) {
        wx.showToast({ title: '请选择完整的商品规格', icon: 'none' });
        return;
      }
    }
    
    // 2. 从缓存中获取当前购物车
    const cartItems = wx.getStorageSync('cartItems') || [];

    // 3. 构造规格ID，用于判断是否是同一商品
    const specId = Object.values(selectedSpec).join('-');
    const cartId = `${product.id}-${specId}`;

    // 4. 查找购物车中是否已存在该商品
    const existingItemIndex = cartItems.findIndex(item => item.cartId === cartId);

    if (existingItemIndex > -1) {
      // 如果存在，则数量增加
      cartItems[existingItemIndex].quantity += quantity;
    } else {
      // 如果不存在，则新增到购物车
      cartItems.push({
        cartId: cartId, // 唯一标识符
        id: product.id,
        name: product.name,
        price: product.price,
        image: product.images[0],
        quantity: quantity,
        selected: true, // 默认选中
        color: selectedSpec['颜色'] || '标准', // 获取颜色规格
        size: selectedSpec['尺寸'] || '均码', // 获取尺寸规格
      });
    }

    // 5. 将更新后的购物车存回缓存
    wx.setStorageSync('cartItems', cartItems);

    wx.showToast({
      title: '已添加到购物车',
      icon: 'success'
    });
  },
  
  // ... 其他函数保持不变 ...
  onImageTap(e) {
    const { index } = e.currentTarget.dataset
    const { images } = this.data.product
    wx.previewImage({
      current: images[index],
      urls: images
    })
  },
  onSpecTap(e) {
    const { specName, option } = e.currentTarget.dataset
    const selectedSpec = { ...this.data.selectedSpec }
    selectedSpec[specName] = option
    this.setData({ selectedSpec })
  },
  onQuantityChange(e) {
    const { type } = e.currentTarget.dataset
    let { quantity } = this.data
    if (type === 'minus' && quantity > 1) {
      quantity--
    } else if (type === 'plus' && quantity < this.data.product.stock) {
      quantity++
    }
    this.setData({ quantity })
  },
  onBuyNow() {
    wx.navigateTo({
      url: `/pages/order/checkout/checkout?productId=${this.data.product.id}&quantity=${this.data.quantity}`
    })
  },
  onToggleFavorite() {
    wx.showToast({
      title: '已收藏',
      icon: 'success'
    })
  }
});