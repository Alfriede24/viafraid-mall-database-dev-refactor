// utils/justhuitan.js
// 聚水潭API相关工具函数

const app = getApp();

/**
 * 聚水潭API基础配置
 */
const JST_CONFIG = {
  baseUrl: app.globalData?.apiUrl || 'http://localhost:5277',
  endpoints: {
    products: '/api/justhuitan/products',
    inventory: '/api/justhuitan/inventory',
    shops: '/api/justhuitan/shops',
    uploadOrder: '/api/justhuitan/upload-order'
  }
};

/**
 * 获取聚水潭商品列表
 * @returns {Promise} 商品列表
 */
function getJstProducts() {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${JST_CONFIG.baseUrl}${JST_CONFIG.endpoints.products}`,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('获取聚水潭商品失败:', err);
        reject(err);
      }
    });
  });
}

/**
 * 获取聚水潭库存信息
 * @returns {Promise} 库存信息
 */
function getJstInventory() {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${JST_CONFIG.baseUrl}${JST_CONFIG.endpoints.inventory}`,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('获取聚水潭库存失败:', err);
        reject(err);
      }
    });
  });
}

/**
 * 获取聚水潭店铺列表
 * @returns {Promise} 店铺列表
 */
function getJstShops() {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${JST_CONFIG.baseUrl}${JST_CONFIG.endpoints.shops}`,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('获取聚水潭店铺失败:', err);
        reject(err);
      }
    });
  });
}

/**
 * 上传订单到聚水潭
 * @param {object} orderData 订单数据
 * @returns {Promise} 上传结果
 */
function uploadOrderToJst(orderData) {
  return new Promise((resolve, reject) => {
    // 转换订单数据格式为聚水潭格式
    const jstOrder = convertOrderToJstFormat(orderData);
    
    wx.request({
      url: `${JST_CONFIG.baseUrl}${JST_CONFIG.endpoints.uploadOrder}`,
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: jstOrder,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`上传失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('上传订单到聚水潭失败:', err);
        reject(err);
      }
    });
  });
}

/**
 * 将本地订单数据转换为聚水潭格式
 * @param {object} orderData 本地订单数据
 * @returns {object} 聚水潭格式订单数据
 */
function convertOrderToJstFormat(orderData) {
  return {
    so_id: orderData.id || orderData.orderId,
    shop_id: 1, // 默认店铺ID，需要根据实际配置
    order_date: orderData.createdAt || new Date().toISOString(),
    receiver_name: orderData.shippingAddress?.name || orderData.receiverName,
    receiver_mobile: orderData.shippingAddress?.phone || orderData.receiverPhone,
    receiver_address: orderData.shippingAddress?.fullAddress || orderData.receiverAddress,
    freight: orderData.shippingFee || 0,
    items: orderData.items?.map(item => ({
      sku_id: item.productId || item.skuId,
      qty: item.quantity,
      price: item.price,
      amount: item.quantity * item.price
    })) || [],
    pay: [{
      pay_date: orderData.paidAt || new Date().toISOString(),
      amount: orderData.totalAmount,
      payment: orderData.paymentMethod || '微信支付'
    }]
  };
}

/**
 * 同步商品库存到本地
 * @param {array} jstInventory 聚水潭库存数据
 * @returns {Promise} 同步结果
 */
function syncInventoryToLocal(jstInventory) {
  return new Promise((resolve, reject) => {
    // 这里可以调用本地API更新库存
    wx.request({
      url: `${JST_CONFIG.baseUrl}/api/products/sync-inventory`,
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: { inventory: jstInventory },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`同步失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('同步库存失败:', err);
        reject(err);
      }
    });
  });
}

/**
 * 检查商品库存状态
 * @param {string} skuId 商品SKU ID
 * @returns {Promise} 库存状态
 */
function checkProductStock(skuId) {
  return getJstInventory().then(inventory => {
    const item = inventory.find(inv => inv.sku_id === skuId);
    return {
      skuId: skuId,
      available: item ? item.sellable_qty > 0 : false,
      quantity: item ? item.sellable_qty : 0,
      reserved: item ? item.reserved_qty : 0
    };
  });
}

/**
 * 批量检查商品库存
 * @param {array} skuIds SKU ID数组
 * @returns {Promise} 库存状态数组
 */
function batchCheckStock(skuIds) {
  return getJstInventory().then(inventory => {
    return skuIds.map(skuId => {
      const item = inventory.find(inv => inv.sku_id === skuId);
      return {
        skuId: skuId,
        available: item ? item.sellable_qty > 0 : false,
        quantity: item ? item.sellable_qty : 0,
        reserved: item ? item.reserved_qty : 0
      };
    });
  });
}

/**
 * 显示聚水潭操作结果
 * @param {boolean} success 是否成功
 * @param {string} message 消息内容
 */
function showJstResult(success, message) {
  wx.showToast({
    title: message,
    icon: success ? 'success' : 'none',
    duration: 2000
  });
}

module.exports = {
  JST_CONFIG,
  getJstProducts,
  getJstInventory,
  getJstShops,
  uploadOrderToJst,
  convertOrderToJstFormat,
  syncInventoryToLocal,
  checkProductStock,
  batchCheckStock,
  showJstResult
};