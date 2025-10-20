<template>
  <Layout>
    <div class="dashboard">
      <div class="dashboard-header">
        <h1>仪表板</h1>
        <p>欢迎回来，{{ authStore.merchant?.name }}！</p>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-grid">
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <div class="stat-icon orders">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.totalOrders }}</h3>
              <p>总订单数</p>
            </div>
          </div>
        </el-card>
        
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <div class="stat-icon revenue">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <h3>¥{{ stats.totalRevenue.toFixed(2) }}</h3>
              <p>总收入</p>
            </div>
          </div>
        </el-card>
        
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <div class="stat-icon products">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.totalProducts }}</h3>
              <p>商品总数</p>
            </div>
          </div>
        </el-card>
        
        <el-card class="stat-card" v-loading="loading">
          <div class="stat-content">
            <div class="stat-icon pending">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.pendingOrders }}</h3>
              <p>待处理订单</p>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 最近订单 -->
      <el-card class="recent-orders" v-loading="loading">
        <template #header>
          <div class="card-header">
            <span>最近订单</span>
            <el-button type="text" @click="$router.push('/orders')">查看全部</el-button>
          </div>
        </template>
        
        <el-table :data="recentOrders" style="width: 100%">
          <el-table-column prop="id" label="订单号" width="120" />
          <el-table-column prop="customerName" label="客户" width="120" />
          <el-table-column prop="totalAmount" label="金额" width="100">
            <template #default="{ row }">
              ¥{{ row.totalAmount.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import Layout from '@/components/Layout.vue'
import { Document, Money, Goods, Clock } from '@element-plus/icons-vue'

const authStore = useAuthStore()
const loading = ref(false)

// 统计数据
const stats = ref({
  totalOrders: 0,
  totalRevenue: 0,
  totalProducts: 0,
  pendingOrders: 0
})

// 最近订单
const recentOrders = ref([])

// 获取统计数据
const fetchStats = async () => {
  loading.value = true
  try {
    // TODO: 调用API获取统计数据
    // 模拟数据
    stats.value = {
      totalOrders: 156,
      totalRevenue: 25680.50,
      totalProducts: 45,
      pendingOrders: 8
    }
    
    // 模拟最近订单数据
    recentOrders.value = [
      {
        id: 'ORD001',
        customerName: '张三',
        totalAmount: 299.00,
        status: 'pending',
        createdAt: new Date().toISOString()
      },
      {
        id: 'ORD002',
        customerName: '李四',
        totalAmount: 156.80,
        status: 'shipped',
        createdAt: new Date(Date.now() - 86400000).toISOString()
      },
      {
        id: 'ORD003',
        customerName: '王五',
        totalAmount: 89.90,
        status: 'completed',
        createdAt: new Date(Date.now() - 172800000).toISOString()
      }
    ]
  } catch (error) {
    console.error('Failed to fetch stats:', error)
  } finally {
    loading.value = false
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
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
  margin: 0;
}

.dashboard-header {
  margin-bottom: 24px;
}

.dashboard-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.dashboard-header p {
  margin: 0;
  color: #606266;
  font-size: 16px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 8px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 24px;
  color: white;
}

.stat-icon.orders {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.revenue {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.products {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.pending {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info h3 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.stat-info p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.recent-orders {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

:deep(.el-table) {
  border-radius: 4px;
}
</style>