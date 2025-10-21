// pages/coupon/get/get.js
const app = getApp()

const extractData = (payload) => {
  if (!payload || typeof payload !== 'object') {
    return payload
  }

  if (payload.data !== undefined) {
    return payload.data
  }

  return payload
}

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
      const payload = await app.request({
        url: '/coupons/available',
        method: 'GET',
        data: {
          page: this.data.page,
          pageSize: this.data.pageSize
        },
        silent: true
      })

      const rawData = extractData(payload) || {}
      const sourceItems = Array.isArray(rawData.items)
        ? rawData.items
        : Array.isArray(rawData.list)
        ? rawData.list
        : Array.isArray(rawData.records)
        ? rawData.records
        : Array.isArray(rawData)
        ? rawData
        : []

      const formattedCoupons = sourceItems.map((item) => ({
        ...item,
        startTime: this.formatDateTime(item.startTime),
        endTime: this.formatDateTime(item.endTime),
        receiving: false,
        isReceived: false
      }))

      await this.checkReceivedStatus(formattedCoupons)

      const hasMore =
        rawData.hasMore !== undefined
          ? rawData.hasMore
          : rawData.hasNext !== undefined
          ? rawData.hasNext
          : formattedCoupons.length === this.data.pageSize

      this.setData({
        coupons: isLoadMore ? [...this.data.coupons, ...formattedCoupons] : formattedCoupons,
        hasMore,
        page: this.data.page + 1
      })
    } catch (error) {
      console.error('加载优惠券失败:', error)
      wx.showToast({
        title: error?.message || '加载失败',
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
    if (!token || !Array.isArray(coupons) || coupons.length === 0) return

    try {
      const couponIds = coupons.map((c) => c.id)
      const payload = await app.request({
        url: '/user-coupons/check-received',
        method: 'POST',
        data: {
          couponIds
        },
        silent: true
      })

      const data = extractData(payload) || {}
      const receivedIdsSource = Array.isArray(data)
        ? data
        : data.receivedCouponIds || data.ids || []
      const receivedIds = Array.isArray(receivedIdsSource) ? receivedIdsSource : []

      coupons.forEach((coupon) => {
        coupon.isReceived = receivedIds.includes(coupon.id)
      })
    } catch (error) {
      console.error('检查领取状态失败:', error)
    }
  },

  // 加载已领取数量
  async loadReceivedCount() {
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ receivedCount: 0 })
      return
    }

    try {
      const payload = await app.request({
        url: '/user-coupons/count',
        method: 'GET',
        silent: true
      })

      const data = extractData(payload)
      const count =
        typeof data === 'number'
          ? data
          : data && typeof data === 'object'
          ? data.count ?? data.total ?? 0
          : 0

      this.setData({
        receivedCount: count
      })
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
    const couponIndex = this.data.coupons.findIndex((c) => c.id === coupon.id)
    if (couponIndex === -1) return
    
    this.setData({
      [`coupons[${couponIndex}].receiving`]: true
    })

    try {
      await app.request({
        url: '/user-coupons/receive',
        method: 'POST',
        data: {
          couponId: coupon.id
        },
        silent: true
      })

      const updatedRemaining = Math.max(0, (coupon.remainingQuantity || 0) - 1)
      const updatedCoupon = {
        ...coupon,
        isReceived: true,
        remainingQuantity: updatedRemaining
      }

      this.setData({
        [`coupons[${couponIndex}].isReceived`]: true,
        [`coupons[${couponIndex}].remainingQuantity`]: updatedRemaining,
        receivedCount: this.data.receivedCount + 1,
        showSuccessModal: true,
        successCoupon: updatedCoupon
      })

      wx.vibrateShort()
    } catch (error) {
      console.error('领取优惠券失败:', error)
      wx.showToast({
        title: error?.message || '领取失败',
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
