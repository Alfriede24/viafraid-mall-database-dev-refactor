<template>
  <Layout>
    <div class="orders">
      <div class="page-header">
        <h1>订单管理</h1>
        <p>管理您的所有订单</p>
      </div>
      
      <!-- 搜索和筛选 -->
      <el-card class="filter-card">
        <el-form :model="filters" inline>
          <el-form-item label="订单号">
            <el-input
              v-model="filters.orderId"
              placeholder="请输入订单号"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="客户姓名">
            <el-input
              v-model="filters.customerName"
              placeholder="请输入客户姓名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="订单状态">
            <el-select
              v-model="filters.status"
              placeholder="请选择状态"
              clearable
              style="width: 150px"
            >
              <el-option label="待处理" value="pending" />
              <el-option label="处理中" value="processing" />
              <el-option label="已发货" value="shipped" />
              <el-option label="已完成" value="completed" />
              <el-option label="已取消" value="cancelled" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="filters.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="searchOrders">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetFilters">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 订单列表 -->
      <el-card class="orders-table">
        <el-table
          :data="orders"
          v-loading="loading"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column prop="id" label="订单号" width="120" fixed="left" />
          
          <el-table-column prop="customerName" label="客户" width="120" />
          
          <el-table-column prop="customerPhone" label="联系电话" width="130" />
          
          <el-table-column label="商品信息" width="300">
            <template #default="{ row }">
              <div class="product-info">
                <div v-for="item in row.items" :key="item.id" class="product-item">
                  <span>{{ item.productName }}</span>
                  <span class="quantity">x{{ item.quantity }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="totalAmount" label="订单金额" width="120">
            <template #default="{ row }">
              <span class="amount">¥{{ row.totalAmount.toFixed(2) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="createdAt" label="下单时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
          
          <el-table-column label="物流信息" width="150">
            <template #default="{ row }">
              <div v-if="row.logisticsInfo">
                <div class="logistics-info">
                  <el-tag size="small" :type="getLogisticsStatusType(row.logisticsInfo.status)">
                    {{ row.logisticsInfo.status }}
                  </el-tag>
                  <div class="logistics-detail">
                    <span>{{ row.logisticsInfo.expressCompany }}</span>
                    <span class="tracking-number">{{ row.logisticsInfo.trackingNumber }}</span>
                  </div>
                </div>
              </div>
              <el-tag v-else type="info" size="small">未发货</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="250" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="viewOrder(row)">
                查看
              </el-button>
              
              <el-dropdown @command="(command) => handleOrderAction(command, row)">
                <el-button type="primary" size="small" plain>
                  操作<el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="process" v-if="row.status === 'pending'">
                      处理订单
                    </el-dropdown-item>
                    <el-dropdown-item command="ship" v-if="row.status === 'processing'">
                      发货
                    </el-dropdown-item>
                    <el-dropdown-item command="logistics" v-if="['processing', 'shipped', 'completed'].includes(row.status)">
                      物流管理
                    </el-dropdown-item>
                    <el-dropdown-item command="complete" v-if="row.status === 'shipped'">
                      完成订单
                    </el-dropdown-item>
                    <el-dropdown-item command="cancel" v-if="['pending', 'processing'].includes(row.status)">
                      取消订单
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 物流管理对话框 -->
    <el-dialog
      v-model="logisticsDialogVisible"
      title="物流管理"
      width="600px"
      @close="closeLogisticsDialog"
    >
      <el-form
        :model="logisticsForm"
        label-width="120px"
        label-position="left"
      >
        <el-form-item label="快递公司">
          <el-select
            v-model="logisticsForm.expressCompany"
            placeholder="请选择快递公司"
            style="width: 100%"
          >
            <el-option label="顺丰速运" value="顺丰速运" />
            <el-option label="圆通速递" value="圆通速递" />
            <el-option label="中通快递" value="中通快递" />
            <el-option label="申通快递" value="申通快递" />
            <el-option label="韵达速递" value="韵达速递" />
            <el-option label="百世快递" value="百世快递" />
            <el-option label="京东物流" value="京东物流" />
            <el-option label="邮政EMS" value="邮政EMS" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="运单号">
          <el-input
            v-model="logisticsForm.trackingNumber"
            placeholder="请输入运单号"
          />
        </el-form-item>
        
        <el-form-item label="物流状态">
          <el-select
            v-model="logisticsForm.status"
            placeholder="请选择物流状态"
            style="width: 100%"
          >
            <el-option label="已发货" value="已发货" />
            <el-option label="运输中" value="运输中" />
            <el-option label="派送中" value="派送中" />
            <el-option label="已签收" value="已签收" />
            <el-option label="异常" value="异常" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="收件人">
          <el-input
            v-model="logisticsForm.receiverName"
            placeholder="请输入收件人姓名"
          />
        </el-form-item>
        
        <el-form-item label="联系电话">
          <el-input
            v-model="logisticsForm.receiverPhone"
            placeholder="请输入联系电话"
          />
        </el-form-item>
        
        <el-form-item label="收货地址">
          <el-input
            v-model="logisticsForm.receiverAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入收货地址"
          />
        </el-form-item>
        
        <el-form-item label="预计送达">
          <el-date-picker
            v-model="logisticsForm.estimatedDeliveryTime"
            type="datetime"
            placeholder="请选择预计送达时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeLogisticsDialog">取消</el-button>
          <el-button type="primary" @click="saveLogisticsInfo">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import Layout from '@/components/Layout.vue'
import { Search, Refresh, ArrowDown } from '@element-plus/icons-vue'

const loading = ref(false)
const selectedOrders = ref([])

// 筛选条件
const filters = reactive({
  orderId: '',
  customerName: '',
  status: '',
  dateRange: null
})

// 物流管理对话框
const logisticsDialogVisible = ref(false)
const currentOrder = ref(null)
const logisticsForm = reactive({
  expressCompany: '',
  trackingNumber: '',
  status: '已发货',
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  estimatedDeliveryTime: null
})
const logisticsInfo = ref(null)

// 分页
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 订单列表
const orders = ref([])

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    // TODO: 调用API获取订单数据
    // 模拟数据
    orders.value = [
      {
        id: 'ORD001',
        customerName: '张三',
        customerPhone: '13800138001',
        items: [
          { id: 1, productName: '苹果手机', quantity: 1 },
          { id: 2, productName: '手机壳', quantity: 2 }
        ],
        totalAmount: 5299.00,
        status: 'pending',
        createdAt: new Date().toISOString()
      },
      {
        id: 'ORD002',
        customerName: '李四',
        customerPhone: '13800138002',
        items: [
          { id: 3, productName: '笔记本电脑', quantity: 1 }
        ],
        totalAmount: 8999.00,
        status: 'processing',
        createdAt: new Date(Date.now() - 86400000).toISOString()
      },
      {
        id: 'ORD003',
        customerName: '王五',
        customerPhone: '13800138003',
        items: [
          { id: 4, productName: '无线耳机', quantity: 1 }
        ],
        totalAmount: 299.00,
        status: 'shipped',
        createdAt: new Date(Date.now() - 172800000).toISOString()
      }
    ]
    pagination.total = 50 // 模拟总数
  } catch (error) {
    console.error('Failed to fetch orders:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索订单
const searchOrders = () => {
  pagination.page = 1
  fetchOrders()
}

// 重置筛选条件
const resetFilters = () => {
  Object.assign(filters, {
    orderId: '',
    customerName: '',
    status: '',
    dateRange: null
  })
  searchOrders()
}

// 处理选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedOrders.value = selection
}

// 处理页面大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchOrders()
}

// 处理当前页变化
const handleCurrentChange = (page: number) => {
  pagination.page = page
  fetchOrders()
}

// 查看订单详情
const viewOrder = (order: any) => {
  // TODO: 实现订单详情查看
  ElMessage.info(`查看订单 ${order.id}`)
}

// 处理订单操作
const handleOrderAction = async (command: string, order: any) => {
  try {
    let confirmText = ''
    let successText = ''
    
    switch (command) {
      case 'process':
        confirmText = '确定要处理这个订单吗？'
        successText = '订单已开始处理'
        break
      case 'ship':
        confirmText = '确定要发货吗？'
        successText = '订单已发货'
        break
      case 'complete':
        confirmText = '确定要完成这个订单吗？'
        successText = '订单已完成'
        break
      case 'cancel':
        confirmText = '确定要取消这个订单吗？'
        successText = '订单已取消'
        break
      case 'logistics':
        // 打开物流管理对话框
        openLogisticsDialog(order)
        return
    }
    
    await ElMessageBox.confirm(confirmText, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // TODO: 调用API更新订单状态
    ElMessage.success(successText)
    fetchOrders()
  } catch {
    // 用户取消
  }
}

// 获取订单状态类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: 'warning',
    processing: 'info',
    shipped: 'primary',
    completed: 'success',
    cancelled: 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取物流状态类型
const getLogisticsStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '未发货': 'info',
    '已发货': 'warning',
    '运输中': 'primary',
    '派送中': 'warning',
    '已签收': 'success',
    '异常': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取订单状态文本
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: '待处理',
    processing: '处理中',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 打开物流管理对话框
const openLogisticsDialog = async (order: any) => {
  currentOrder.value = order
  logisticsDialogVisible.value = true
  
  // 获取物流信息
  await fetchLogisticsInfo(order.id)
}

// 获取物流信息
const fetchLogisticsInfo = async (orderId: string) => {
  try {
    // TODO: 调用API获取物流信息
    // const response = await api.get(`/api/logistics/order/${orderId}`)
    // logisticsInfo.value = response.data
    
    // 模拟数据
    logisticsInfo.value = {
      id: '1',
      orderId: orderId,
      expressCompany: '顺丰速运',
      trackingNumber: 'SF1234567890',
      status: '运输中',
      receiverName: '张三',
      receiverPhone: '13800138000',
      receiverAddress: '北京市朝阳区xxx街道xxx号',
      estimatedDeliveryTime: '2024-01-15 18:00:00'
    }
    
    // 填充表单
    if (logisticsInfo.value) {
      Object.assign(logisticsForm, logisticsInfo.value)
    }
  } catch (error) {
    console.error('获取物流信息失败:', error)
  }
}

// 保存物流信息
const saveLogisticsInfo = async () => {
  try {
    // TODO: 调用API保存物流信息
    // if (logisticsInfo.value) {
    //   await api.put(`/api/logistics/${logisticsInfo.value.id}`, logisticsForm)
    // } else {
    //   await api.post('/api/logistics', { ...logisticsForm, orderId: currentOrder.value.id })
    // }
    
    ElMessage.success('物流信息保存成功')
    logisticsDialogVisible.value = false
    fetchOrders()
  } catch (error) {
    console.error('保存物流信息失败:', error)
    ElMessage.error('保存物流信息失败')
  }
}

// 关闭物流对话框
const closeLogisticsDialog = () => {
  logisticsDialogVisible.value = false
  currentOrder.value = null
  logisticsInfo.value = null
  // 重置表单
  Object.assign(logisticsForm, {
    expressCompany: '',
    trackingNumber: '',
    status: '已发货',
    receiverName: '',
    receiverPhone: '',
    receiverAddress: '',
    estimatedDeliveryTime: null
  })
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.orders {
  width: 100%;
  margin: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 16px;
}

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.orders-table {
  border-radius: 8px;
}

.product-info {
  max-height: 80px;
  overflow-y: auto;
}

.product-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2px 0;
  border-bottom: 1px solid #f0f0f0;
}

.product-item:last-child {
  border-bottom: none;
}

.quantity {
  color: #909399;
  font-size: 12px;
}

.amount {
  font-weight: 600;
  color: #e6a23c;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-table) {
  border-radius: 4px;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>