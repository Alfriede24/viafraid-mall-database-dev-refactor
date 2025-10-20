// pages/search/search.js
const app = getApp()

Page({
  data: {
    keyword: '',
    autoFocus: true,
    hasSearched: false,
    loading: false,
    products: [],
    totalCount: 0,
    currentPage: 1,
    pageSize: 20,
    hasMore: true,
    searchHistory: [],
    hotKeywords: ['手机', '耳机', '充电器', '数据线', '手机壳', '蓝牙音箱']
  },

  onLoad(options) {
    // 从其他页面跳转过来的搜索关键词
    if (options.keyword) {
      this.setData({
        keyword: options.keyword,
        autoFocus: false
      })
      this.performSearch()
    }
    
    // 加载搜索历史
    this.loadSearchHistory()
  },

  onShow() {
    // 页面显示时重新加载搜索历史
    this.loadSearchHistory()
  },

  // 输入关键词
  onKeywordInput(e) {
    this.setData({
      keyword: e.detail.value
    })
  },

  // 执行搜索
  onSearch() {
    const keyword = this.data.keyword.trim()
    if (!keyword) {
      wx.showToast({
        title: '请输入搜索关键词',
        icon: 'none'
      })
      return
    }

    // 保存搜索历史
    this.saveSearchHistory(keyword)
    
    // 重置搜索状态
    this.setData({
      hasSearched: true,
      currentPage: 1,
      products: [],
      hasMore: true
    })

    this.performSearch()
  },

  // 执行搜索请求
  async performSearch() {
    if (this.data.loading) return

    this.setData({ loading: true })

    try {
      const res = await wx.request({
        url: `${app.globalData.apiBaseUrl}/api/products`,
        method: 'GET',
        data: {
          keyword: this.data.keyword,
          page: this.data.currentPage,
          pageSize: this.data.pageSize
        },
        header: {
          'Authorization': `Bearer ${app.globalData.token}`
        }
      })

      if (res.data.success) {
        const { items, total } = res.data.data
        const newProducts = this.data.currentPage === 1 ? items : [...this.data.products, ...items]
        
        this.setData({
          products: newProducts,
          totalCount: total,
          hasMore: newProducts.length < total
        })
      } else {
        wx.showToast({
          title: res.data.message || '搜索失败',
          icon: 'none'
        })
      }
    } catch (error) {
      console.error('搜索商品失败:', error)
      wx.showToast({
        title: '搜索失败，请重试',
        icon: 'none'
      })
    } finally {
      this.setData({ loading: false })
    }
  },

  // 加载更多
  onLoadMore() {
    if (!this.data.hasMore || this.data.loading) return
    
    this.setData({
      currentPage: this.data.currentPage + 1
    })
    this.performSearch()
  },

  // 点击商品
  onProductTap(e) {
    const productId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${productId}`
    })
  },

  // 点击搜索历史
  onHistoryTap(e) {
    const keyword = e.currentTarget.dataset.keyword
    this.setData({
      keyword,
      hasSearched: true,
      currentPage: 1,
      products: [],
      hasMore: true
    })
    this.performSearch()
  },

  // 点击热门关键词
  onHotKeywordTap(e) {
    const keyword = e.currentTarget.dataset.keyword
    this.setData({
      keyword,
      hasSearched: true,
      currentPage: 1,
      products: [],
      hasMore: true
    })
    this.saveSearchHistory(keyword)
    this.performSearch()
  },

  // 清空搜索历史
  onClearHistory() {
    wx.showModal({
      title: '提示',
      content: '确定要清空搜索历史吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('searchHistory')
          this.setData({
            searchHistory: []
          })
        }
      }
    })
  },

  // 加载搜索历史
  loadSearchHistory() {
    try {
      const history = wx.getStorageSync('searchHistory') || []
      this.setData({
        searchHistory: history
      })
    } catch (error) {
      console.error('加载搜索历史失败:', error)
    }
  },

  // 保存搜索历史
  saveSearchHistory(keyword) {
    try {
      let history = wx.getStorageSync('searchHistory') || []
      
      // 移除重复的关键词
      history = history.filter(item => item !== keyword)
      
      // 添加到开头
      history.unshift(keyword)
      
      // 最多保存10条历史记录
      if (history.length > 10) {
        history = history.slice(0, 10)
      }
      
      wx.setStorageSync('searchHistory', history)
      this.setData({
        searchHistory: history
      })
    } catch (error) {
      console.error('保存搜索历史失败:', error)
    }
  }
})