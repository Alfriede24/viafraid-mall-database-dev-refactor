// pages/coupon/get/get.js
const app = getApp()

Page({
  data: {
    coupons: [], // 可领取的优惠券列表
    loading: false, // 加载状态
    refreshing: false, // 刷新状态
    loadingMore: false, // 加载更多状态
    hasMore: true, // 是否还有更多数据
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    receivedCount: 0, // 已领取数量
    showSuccessModal: false, // 显示成功提示弹窗
    successCoupon: null // 成功领取的优惠券
  },

  onLoad(options) {
    this.loadCoupons()
    this.loadReceivedCount()
  },

  onShow() {
    // 页面显示时刷新数据
    this.refreshCoupons()
  },

  onPullDownRefresh() {
    this.onRefresh()
  },

  onReachBottom() {
    this.loadMore()
  },

  // 加载可领取的优惠券列表
  async loadCoupons(isLoadMore = false) {
    if (this.data.loading || this.data.loadingMore) return
    
    const loadingKey = isLoadMore ? 'loadingMore' : 'loading'
    this.setData({ [loadingKey]: true })
    
    try {
      const response = await wx.request({
        url: `${app.globalData.apiBaseUrl}/coupons/available`,
        method: 'GET',
        header: {
          'Content-Type': 'application/json'
        },
        data: {
          page: this.data.page,
          pageSize: this.data.pageSize
        }
      })

      if (response.statusCode === 200) {
        const { items, hasMore } = response.data
        
        // 格式化优惠券数据
        const formattedCoupons = items.map(item => ({
          ...item,
          startTime: this.formatDateTime(item.startTime),
          endTime: this.formatDateTime(item.endTime),
          receiving: false, // 添加领取状态
          isReceived: false // 添加是否已领取状态
        }))
        
        // 检查用户是否已领取这些优惠券
        await this.checkReceivedStatus(formattedCoupons)
        
        this.setData({
          coupons: isLoadMore ? [...this.data.coupons, ...formattedCoupons] : formattedCoupons,
          hasMore: hasMore,
          page: isLoadMore ? this.data.page + 1 : this.data.page + 1
        })
      } else {
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    } catch (error) {
      console.error('加载优惠券失败:', error)
      wx.showToast({
        title: '网络错误',
        icon: 'none'
      })
    } finally {
      this.setData({ 
        [loadingKey]: false,
        refreshing: false
      })
      wx.stopPullDownRefresh()
    }
  },

  // 检查用户已领取状态
  async checkReceivedStatus(coupons) {
    const token = wx.getStorageSync('token')
    if (!token) return
    
    try {
      const couponIds = coupons.map(c => c.id)
      const response = await wx.request({
        url: `${app.globalData.apiBaseUrl}/user-coupons/check-received`,
        method: 'POST',
        header: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        data: {
          couponIds: couponIds
        }
      })
      
      if (response.statusCode === 200) {
        const receivedIds = response.data.receivedCouponIds || []
        coupons.forEach(coupon => {
          coupon.isReceived = receivedIds.includes(coupon.id)
        })
      }
    } catch (error) {
      console.error('检查领取状态失败:', error)
    }
  },

  // 加载已领取数量
  async loadReceivedCount() {
    const token = wx.getStorageSync('token')
    if (!token) return
    
    try {
      const response = await wx.request({
        url: `${app.globalData.apiBaseUrl}/user-coupons/count`,
        method: 'GET',
        header: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })
      
      if (response.statusCode === 200) {
        this.setData({
          receivedCount: response.data.count || 0
        })
      }
    } catch (error) {
      console.error('加载已领取数量失败:', error)
    }
  },

  // 刷新数据
  onRefresh() {
    this.setData({
      refreshing: true,
      page: 1,
      hasMore: true
    })
    this.loadCoupons()
    this.loadReceivedCount()
  },

  // 刷新优惠券列表
  refreshCoupons() {
    this.setData({
      page: 1,
      hasMore: true
    })
    this.loadCoupons()
    this.loadReceivedCount()
  },

  // 加载更多
  loadMore() {
    if (this.data.hasMore && !this.data.loadingMore) {
      this.loadCoupons(true)
    }
  },

  // 领取优惠券
  async receiveCoupon(e) {
    const coupon = e.currentTarget.dataset.coupon
    
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/login/login'
        })
      }, 1500)
      return
    }
    
    // 检查是否已领取
    if (coupon.isReceived) {
      wx.showToast({
        title: '您已领取过该优惠券',
        icon: 'none'
      })
      return
    }
    
    // 检查库存
    if (coupon.remainingQuantity <= 0) {
      wx.showToast({
        title: '优惠券已被抢完',
        icon: 'none'
      })
      return
    }
    
    // 设置领取状态
    const couponIndex = this.data.coupons.findIndex(c => c.id === coupon.id)
    if (couponIndex === -1) return
    
    this.setData({
      [`coupons[${couponIndex}].receiving`]: true
    })
    
    try {
      const response = await wx.request({
        url: `${app.globalData.apiBaseUrl}/user-coupons/receive`,
        method: 'POST',
        header: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        data: {
          couponId: coupon.id
        }
      })
      
      if (response.statusCode === 200) {
        // 领取成功
        this.setData({
          [`coupons[${couponIndex}].isReceived`]: true,
          [`coupons[${couponIndex}].remainingQuantity`]: coupon.remainingQuantity - 1,
          receivedCount: this.data.receivedCount + 1,
          showSuccessModal: true,
          successCoupon: coupon
        })
        
        // 震动反馈
        wx.vibrateShort()
      } else {
        const errorMsg = response.data?.message || '领取失败'
        wx.showToast({
          title: errorMsg,
          icon: 'none'
        })
      }
    } catch (error) {
      console.error('领取优惠券失败:', error)
      wx.showToast({
        title: '网络错误',
        icon: 'none'
      })
    } finally {
      this.setData({
        [`coupons[${couponIndex}].receiving`]: false
      })
    }
  },

  // 关闭成功提示弹窗
  closeSuccessModal() {
    this.setData({
      showSuccessModal: false,
      successCoupon: null
    })
  },

  // 去我的优惠券页面
  goToMyCoupons() {
    this.closeSuccessModal()
    wx.navigateTo({
      url: '/pages/coupon/coupon?tab=0'
    })
  },

  // 格式化日期时间
  formatDateTime(dateTimeString) {
    if (!dateTimeString) return ''
    
    const date = new Date(dateTimeString)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    
    return `${year}-${month}-${day}`
  },

  // 分享功能
  onShareAppMessage() {
    return {
      title: '超多优惠券免费领取，快来抢！',
      path: '/pages/coupon/get/get',
      imageUrl: '/images/share-coupon.jpg'
    }
  },

  onShareTimeline() {
    return {
      title: '超多优惠券免费领取，快来抢！',
      query: '',
      imageUrl: '/images/share-coupon.jpg'
    }
  }
})