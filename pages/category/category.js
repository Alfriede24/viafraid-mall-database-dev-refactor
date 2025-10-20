const app = getApp()
Page({
  data: {
    mainCategories: [
      { id: 'man', name: '男子' },
      { id: 'woman', name: '女子' },
      { id: 'kids', name: '儿童' }
    ],
    currentMainCategory: 'man',

    // **↓↓↓ 關鍵修改：調整了“品牌”的位置 ↓↓↓**
    subCategories: [
      { id: 'hot', name: '热销产品' },
      { id: 'brands', name: '品牌' }, // 已移到第二行
      { id: 'custom', name: '球鞋定制', link: '/pages/customization/customization' },
      { id: 'seasonal', name: '当季主推' },
      { id: 'new', name: '新品' },
      { id: 'shoes', name: '鞋类' },
      { id: 'apparel', name: '服饰' },
      { id: 'accessories', name: '配件' },
      { id: 'sale', name: '折扣专区' }
    ],
    currentSubCategory: 'hot', // 預設選中的依然是'热销产品'

    // 熱銷產品下的系列
    hotSeriesList: [
      { id: 'air-force', name: 'Air Force 系列', image: '/images/placeholder.png' },
      { id: 'air-jordan', name: 'Air Jordan 系列', image: '/images/placeholder.png' },
      { id: 'dunk', name: 'DUNK 系列', image: '/images/placeholder.png' },
      { id: 'pegasus', name: '飞马 系列', image: '/images/placeholder.png' },
      { id: 'zoom-gt', name: 'ZOOM G.T 系列', image: '/images/placeholder.png' },
      { id: 'kobe', name: 'KOBE 系列', image: '/images/placeholder.png' },
      { id: 'originals', name: 'ORIGINALS 系列', image: '/images/placeholder.png' },
      { id: 'legacy-312', name: 'LEGACY 312 系列', image: '/images/placeholder.png' },
      { id: 'spizike', name: 'SPIZIKE 系列', image: '/images/placeholder.png' },
      { id: 'court-borough', name: 'COURT BOROUGH 系列', image: '/images/placeholder.png' },
      { id: 'm2k', name: 'M2K 系列', image: '/images/placeholder.png' },
      { id: 'air-max', name: 'AIR Max 系列', image: '/images/placeholder.png' }
    ],
    currentSeries: null,

    // 品牌列表
    brandsList: [
        { id: 'nike', name: 'NIKE/耐克', image: '/images/placeholder.png'},
        { id: 'jordan', name: 'AIR JORDAN/乔丹', image: '/images/placeholder.png'},
        { id: 'adidas', name: 'ADIDAS/阿迪达斯', image: '/images/placeholder.png'},
        { id: 'salomon', name: 'SALOMON/萨洛蒙', image: '/images/placeholder.png'},
        { id: 'li-ning', name: 'LINING/李宁', image: '/images/placeholder.png'},
        { id: 'new-balance', name: 'NEW BALANCE/新百伦', image: '/images/placeholder.png'},
        { id: 'asics', name: 'ASICS/亚瑟士', image: '/images/placeholder.png'},
        { id: 'anta', name: 'ANTA/安踏', image: '/images/placeholder.png'},
        { id: 'puma', name: 'PUMA/彪马', image: '/images/placeholder.png'}
    ],
    currentBrand: null,

    productList: [],
  },

  onLoad: function (options) {
    let newState = {};
    if (options.mainCategory) {
      newState.currentMainCategory = options.mainCategory;
    }
    if (options.subCategory) {
      newState.currentSubCategory = options.subCategory;
    } else {
      newState.currentSubCategory = 'hot';
    }
    this.setData(newState);
  },

  onShow: function() {
    this.checkGlobalStateAndFetch();
  },
  
  checkGlobalStateAndFetch: function() {
    const navState = app.globalData.navigateToCategoryState;
    let newState = {};
    if (navState) {
      newState.currentMainCategory = navState.mainCategory || this.data.currentMainCategory;
      newState.currentSubCategory = navState.subCategory || 'hot';
      app.globalData.navigateToCategoryState = null;
    }
    this.setData(newState, () => {
      if (app.globalData.httpClient) {
        this.fetchProducts();
      } else {
        app.userInfoReadyCallback = () => {
          this.fetchProducts();
        }
      }
    });
  },

  // --- 交互方法 ---

  onMainCategoryTap: function (e) {
    const mainCategoryId = e.currentTarget.dataset.id;
    if (this.data.currentMainCategory === mainCategoryId) return;
    this.setData({
      currentMainCategory: mainCategoryId,
      currentSubCategory: 'hot',
      currentSeries: null,
      currentBrand: null,
      productList: []
    });
    this.fetchProducts();
  },

  onSubCategoryTap: function (e) {
    const subCategoryId = e.currentTarget.dataset.id;
    if (this.data.currentSubCategory === subCategoryId && subCategoryId !== 'custom') return;
    this.setData({
      currentSubCategory: subCategoryId,
      currentSeries: null,
      currentBrand: null,
      productList: []
    });
    this.fetchProducts();
  },

  onSeriesTap: function(e) {
    this.setData({
      currentSeries: e.currentTarget.dataset.series
    });
    this.fetchProducts();
  },
  
  onBrandTap: function(e) {
    this.setData({
      currentBrand: e.currentTarget.dataset.brand
    });
    this.fetchProducts();
  },

  backToList: function() {
    this.setData({
      currentSeries: null,
      currentBrand: null,
      productList: []
    });
  },

  // --- 網路請求 ---

  fetchProducts: function () {
    const { currentSubCategory, currentSeries, currentBrand } = this.data;

    if (currentSubCategory === 'custom') {
      wx.navigateTo({ url: '/pages/customization/customization' });
      this.setData({ productList: [] });
      return;
    }

    if ((currentSubCategory === 'hot' && !currentSeries) || (currentSubCategory === 'brands' && !currentBrand)) {
      this.setData({ productList: [] });
      return;
    }

    let seriesId = currentSeries ? currentSeries.id : '';
    let brandId = currentBrand ? currentBrand.id : '';
    this.getProductList(currentSubCategory, seriesId, brandId);
  },

  getProductList(categoryId, seriesId = '', brandId = '') {
    if (!app.globalData.httpClient) {
      console.error("httpClient 尚未初始化！");
      return;
    }
    
    let apiUrl = `/product/list?categoryId=${categoryId || ''}`;
    if (categoryId === 'hot' && seriesId) {
      apiUrl += `&series=${seriesId}`;
    }
    if (categoryId === 'brands' && brandId) {
      apiUrl += `&brand=${brandId}`;
    }
    
    console.log(`正在請求 API: ${apiUrl}`);

    app.globalData.httpClient.get(apiUrl).then(res => {
      if (res.data.code == 0) {
        this.setData({ productList: res.data.data });
      } else {
        this.setData({ productList: [] });
      }
    }).catch(err => {
      this.setData({ productList: [] });
      console.error("獲取商品列表失敗", err);
    });
  },
})