const app = getApp()
Page({
  data: {
    // ---- 數據部分 ----
    banners: [],
    productList: [],
    categoryNavs: [
      { image: '/images/home_banners/man_banner.png', mainCategory: 'man' },
      { image: '/images/home_banners/woman_banner.png', mainCategory: 'woman' },
      { image: '/images/home_banners/kids_banner.png', mainCategory: 'kids' }
    ],
    sneakerCustom: { image: '/images/home_banners/custom_banner.png', mainCategory: 'man', subCategory: 'custom' },
    seasonalFeature: { image: '/images/home_banners/seasonal_banner.png', mainCategory: 'man', subCategory: 'seasonal' },
    equipmentCategories: [
      { image: '/images/home_banners/equipment_shoes.png', mainCategory: 'man', subCategory: 'shoes' },
      { image: '/images/home_banners/equipment_apparel.png', mainCategory: 'man', subCategory: 'apparel' },
      { image: '/images/home_banners/equipment_accessories.png', mainCategory: 'man', subCategory: 'accessories' }
    ],
    featuredBrands: [
      { logo: '/images/home_banners/brand_logo_1.png', mainCategory: 'man', subCategory: 'brands' },
      { logo: '/images/home_banners/brand_logo_2.png', mainCategory: 'man', subCategory: 'brands' },
      { logo: '/images/home_banners/brand_logo_3.png', mainCategory: 'man', subCategory: 'brands' },
      { logo: '/images/home_banners/brand_logo_4.png', mainCategory: 'man', subCategory: 'brands' },
      { logo: '/images/home_banners/brand_logo_5.png', mainCategory: 'man', subCategory: 'brands' },
      { logo: '/images/home_banners/brand_logo_6.png', mainCategory: 'man', subCategory: 'brands' }
    ]
  },

  onLoad: function (options) {
    // ---- 關鍵修正：加入等待機制 ----
    if (app.globalData.httpClient) {
      this.loadPageData();
    } else {
      // 如果 httpClient 還沒準備好，就設定回呼
      app.userInfoReadyCallback = () => {
        this.loadPageData();
      }
    }
  },

  // 封裝頁面資料載入
  loadPageData: function() {
    this.getBannerList();
    this.getProductList();
  },

  handleNavigate: function(e) {
    const { maincategory, subcategory } = e.currentTarget.dataset;
    app.globalData.navigateToCategoryState = {
      mainCategory: maincategory,
      subCategory: subcategory
    };
    wx.switchTab({
      url: '/pages/category/category'
    });
  },

  navigateToSearch: function() {
    wx.navigateTo({ url: '/pages/search/search' });
  },
  
  // ---- 網路請求函數保持不變 ----
  getBannerList() {
    app.globalData.httpClient.get(`/banner/list`).then(res => {
      if (res.data.code == 0) { this.setData({ banners: res.data.data }); }
    })
  },
  getProductList() {
    app.globalData.httpClient.get(`/product/list`).then(res => {
      if (res.data.code == 0) { this.setData({ productList: res.data.data }); }
    })
  },
  onShareAppMessage: function () {
    return {
      title: '商城首頁',
      path: '/pages/index/index',
    }
  },
})