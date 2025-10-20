// pages/customization/customization.js
Page({
  data: {
    backgroundImage: '/images/custom/bg.jpg',
    baseShoe: { name: '基础款运动鞋', price: 599.00, baseImage: '/images/custom/base.png' },
    // 核心修改：每个颜色选项现在都关联一个图片路径
    parts: [
      { name: '鞋面', key: 'upper', 
        options: {
          colors: [
            { name: '活力红', color: '#ff4d4f', image: '/images/custom/upper-red.png' }, 
            { name: '天空蓝', color: '#40a9ff', image: '/images/custom/upper-blue.png' }, 
            { name: '经典白', color: '#ffffff', image: '/images/custom/upper-white.png' },
          ],
          materials: [ { name: '帆布', price: 0 }, { name: '皮革', price: 50 } ]
        }
      },
      { name: '鞋底', key: 'sole',
        options: {
          colors: [ 
            { name: '深空灰', color: '#555555', image: '/images/custom/sole-grey.png' }, 
            { name: '经典白', color: '#ffffff', image: '/images/custom/sole-white.png' } 
          ]
        }
      },
      { name: '标志', key: 'logo',
        options: {
          colors: [ 
            { name: '金属银', color: '#C0C0C0', image: '/images/custom/logo-silver.png' }, 
            { name: '经典黑', color: '#000000', image: '/images/custom/logo-black.png' } 
          ],
        }
      },
      { name: '个性签名', key: 'pid', options: {} }
    ],
    activePartKey: 'upper',
    activePartOptions: {},
    currentSelections: {},
    personalId: '',
    totalPrice: 0,
  },

  onLoad(options) {
    this.initCustomization();
  },
  
  initCustomization() {
    const { parts } = this.data;
    const defaultSelections = {};
    parts.forEach(part => {
      if (part.key !== 'pid') {
        defaultSelections[part.key] = {
          image: part.options.colors ? part.options.colors[0].image : null,
          colorName: part.options.colors ? part.options.colors[0].name : null,
          material: part.options.materials ? part.options.materials[0].name : null,
        };
      }
    });
    this.setData({ currentSelections: defaultSelections });
    this.updateActiveOptions();
    this.calculatePrice();
  },
  
  updateActiveOptions() {
    const { parts, activePartKey } = this.data;
    const activePart = parts.find(p => p.key === activePartKey);
    this.setData({ activePartOptions: activePart ? activePart.options : {} });
  },

  switchPart(e) {
    this.setData({ activePartKey: e.currentTarget.dataset.key });
    this.updateActiveOptions();
  },
  
  selectOption(e) {
    const { type, option } = e.currentTarget.dataset;
    const { activePartKey } = this.data;
    
    if (type === 'color') {
      this.setData({
        [`currentSelections.${activePartKey}.image`]: option.image,
        [`currentSelections.${activePartKey}.colorName`]: option.name,
      });
    } else if (type === 'material') {
      this.setData({
        [`currentSelections.${activePartKey}.material`]: option.name,
      });
    }
    
    this.calculatePrice();
  },

  onIdInput(e) {
    this.setData({ personalId: e.detail.value });
  },

  calculatePrice() {
    let price = this.data.baseShoe.price;
    // ... 价格计算逻辑 ...
    this.setData({ totalPrice: price.toFixed(2) });
  },

  addToCart() {
    const { baseShoe, currentSelections, personalId } = this.data;
    const cartItems = wx.getStorageSync('cartItems') || [];
    
    let customDesc = [];
    for(const key in currentSelections) {
      const part = this.data.parts.find(p => p.key === key);
      const selection = currentSelections[key];
      if (selection.material) customDesc.push(`${part.name}材质:${selection.material}`);
    }
    if (personalId) customDesc.push(`签名:${personalId}`);

    const newItem = {
      cartId: `custom-${Date.now()}`, id: 999, name: `定制-${baseShoe.name}`,
      price: parseFloat(this.data.totalPrice), image: baseShoe.baseImage, quantity: 1,
      selected: true, color: '定制', size: customDesc.join('; ') || '标准定制',
    };

    cartItems.push(newItem);
    wx.setStorageSync('cartItems', cartItems);

    wx.showToast({ title: '已添加至购物车', icon: 'success' });
    setTimeout(() => { wx.switchTab({ url: '/pages/cart/cart' }); }, 1500);
  }
});