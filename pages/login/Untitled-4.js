// 引入工具类
const justhuitan = require('../../utils/justhuitan.js');

Page({
  data: {
    products: [],
    inventory: {}
  },

  // 获取商品列表
  async loadProducts() {
    const products = await justhuitan.getProducts();
    this.setData({ products });
  },

  // 检查库存
  async checkInventory(skuId) {
    const inventory = await justhuitan.getInventory(skuId);
    console.log('库存信息:', inventory);
  },

  // 上传订单
  async uploadOrder(orderData) {
    const result = await justhuitan.uploadOrder(orderData);
    if (result.success) {
      wx.showToast({ title: '订单同步成功', icon: 'success' });
    }
  }
});