<template>
  <Layout>
    <div class="statistics">
      <div class="page-header">
        <h1>数据统计</h1>
        <p>查看您的销售数据和业务分析</p>
      </div>
      
      <!-- 时间筛选 -->
      <el-card class="filter-card">
        <el-form :model="filters" inline>
          <el-form-item label="统计时间">
            <el-date-picker
              v-model="filters.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
              @change="fetchStatistics"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button-group>
              <el-button @click="setDateRange('today')" :type="filters.period === 'today' ? 'primary' : 'default'">
                今日
              </el-button>
              <el-button @click="setDateRange('week')" :type="filters.period === 'week' ? 'primary' : 'default'">
                本周
              </el-button>
              <el-button @click="setDateRange('month')" :type="filters.period === 'month' ? 'primary' : 'default'">
                本月
              </el-button>
              <el-button @click="setDateRange('year')" :type="filters.period === 'year' ? 'primary' : 'default'">
                本年
              </el-button>
            </el-button-group>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 核心指标 -->
      <div class="metrics-grid">
        <el-card class="metric-card" v-loading="loading">
          <div class="metric-content">
            <div class="metric-icon sales">
              <el-icon><Money /></el-icon>
            </div>
            <div class="metric-info">
              <h3>¥{{ statistics.totalSales.toFixed(2) }}</h3>
              <p>总销售额</p>
              <div class="metric-trend" :class="{ positive: statistics.salesGrowth >= 0 }">
                <el-icon><ArrowUp v-if="statistics.salesGrowth >= 0" /><ArrowDown v-else /></el-icon>
                {{ Math.abs(statistics.salesGrowth).toFixed(1) }}%
              </div>
            </div>
          </div>
        </el-card>
        
        <el-card class="metric-card" v-loading="loading">
          <div class="metric-content">
            <div class="metric-icon orders">
              <el-icon><Document /></el-icon>
            </div>
            <div class="metric-info">
              <h3>{{ statistics.totalOrders }}</h3>
              <p>订单数量</p>
              <div class="metric-trend" :class="{ positive: statistics.ordersGrowth >= 0 }">
                <el-icon><ArrowUp v-if="statistics.ordersGrowth >= 0" /><ArrowDown v-else /></el-icon>
                {{ Math.abs(statistics.ordersGrowth).toFixed(1) }}%
              </div>
            </div>
          </div>
        </el-card>
        
        <el-card class="metric-card" v-loading="loading">
          <div class="metric-content">
            <div class="metric-icon customers">
              <el-icon><User /></el-icon>
            </div>
            <div class="metric-info">
              <h3>{{ statistics.totalCustomers }}</h3>
              <p>客户数量</p>
              <div class="metric-trend" :class="{ positive: statistics.customersGrowth >= 0 }">
                <el-icon><ArrowUp v-if="statistics.customersGrowth >= 0" /><ArrowDown v-else /></el-icon>
                {{ Math.abs(statistics.customersGrowth).toFixed(1) }}%
              </div>
            </div>
          </div>
        </el-card>
        
        <el-card class="metric-card" v-loading="loading">
          <div class="metric-content">
            <div class="metric-icon avg-order">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="metric-info">
              <h3>¥{{ statistics.avgOrderValue.toFixed(2) }}</h3>
              <p>平均订单价值</p>
              <div class="metric-trend" :class="{ positive: statistics.avgOrderGrowth >= 0 }">
                <el-icon><ArrowUp v-if="statistics.avgOrderGrowth >= 0" /><ArrowDown v-else /></el-icon>
                {{ Math.abs(statistics.avgOrderGrowth).toFixed(1) }}%
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-grid">
        <!-- 销售趋势图 -->
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
            </div>
          </template>
          <div class="chart-container" v-loading="loading">
            <div class="chart-placeholder">
              <el-icon><TrendCharts /></el-icon>
              <p>销售趋势图表</p>
              <small>此处将显示销售额随时间的变化趋势</small>
            </div>
          </div>
        </el-card>
        
        <!-- 商品销量排行 -->
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>热销商品排行</span>
            </div>
          </template>
          <div class="chart-container" v-loading="loading">
            <div class="product-ranking">
              <div v-for="(product, index) in topProducts" :key="product.id" class="ranking-item">
                <div class="ranking-number" :class="`rank-${index + 1}`">
                  {{ index + 1 }}
                </div>
                <div class="product-info">
                  <div class="product-name">{{ product.name }}</div>
                  <div class="product-sales">销量: {{ product.sales }}件</div>
                </div>
                <div class="product-revenue">
                  ¥{{ product.revenue.toFixed(2) }}
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 订单状态分布 -->
      <div class="charts-grid">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>订单状态分布</span>
            </div>
          </template>
          <div class="chart-container" v-loading="loading">
            <div class="status-distribution">
              <div v-for="status in orderStatusData" :key="status.name" class="status-item">
                <div class="status-info">
                  <div class="status-name">{{ status.name }}</div>
                  <div class="status-count">{{ status.count }}单</div>
                </div>
                <div class="status-bar">
                  <div 
                    class="status-progress" 
                    :style="{ width: status.percentage + '%', backgroundColor: status.color }"
                  ></div>
                </div>
                <div class="status-percentage">{{ status.percentage.toFixed(1) }}%</div>
              </div>
            </div>
          </div>
        </el-card>
        
        <!-- 客户地区分布 -->
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户地区分布</span>
            </div>
          </template>
          <div class="chart-container" v-loading="loading">
            <div class="region-distribution">
              <div v-for="region in regionData" :key="region.name" class="region-item">
                <div class="region-name">{{ region.name }}</div>
                <div class="region-bar">
                  <div 
                    class="region-progress" 
                    :style="{ width: region.percentage + '%' }"
                  ></div>
                </div>
                <div class="region-count">{{ region.count }}人 ({{ region.percentage.toFixed(1) }}%)</div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import Layout from '@/components/Layout.vue'
import { 
  Money, 
  Document, 
  User, 
  TrendCharts, 
  ArrowUp, 
  ArrowDown 
} from '@element-plus/icons-vue'

const loading = ref(false)

// 筛选条件
const filters = reactive({
  dateRange: null,
  period: 'month'
})

// 统计数据
const statistics = ref({
  totalSales: 0,
  salesGrowth: 0,
  totalOrders: 0,
  ordersGrowth: 0,
  totalCustomers: 0,
  customersGrowth: 0,
  avgOrderValue: 0,
  avgOrderGrowth: 0
})

// 热销商品
const topProducts = ref([])

// 订单状态数据
const orderStatusData = ref([])

// 地区数据
const regionData = ref([])

// 设置日期范围
const setDateRange = (period: string) => {
  filters.period = period
  const now = new Date()
  let startDate: Date
  
  switch (period) {
    case 'today':
      startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate())
      break
    case 'week':
      startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
      break
    case 'month':
      startDate = new Date(now.getFullYear(), now.getMonth(), 1)
      break
    case 'year':
      startDate = new Date(now.getFullYear(), 0, 1)
      break
    default:
      startDate = new Date(now.getFullYear(), now.getMonth(), 1)
  }
  
  filters.dateRange = [
    startDate.toISOString().split('T')[0],
    now.toISOString().split('T')[0]
  ]
  
  fetchStatistics()
}

// 获取统计数据
const fetchStatistics = async () => {
  loading.value = true
  try {
    // TODO: 调用API获取统计数据
    // 模拟数据
    statistics.value = {
      totalSales: 125680.50,
      salesGrowth: 12.5,
      totalOrders: 456,
      ordersGrowth: 8.3,
      totalCustomers: 234,
      customersGrowth: -2.1,
      avgOrderValue: 275.62,
      avgOrderGrowth: 5.7
    }
    
    topProducts.value = [
      { id: 1, name: 'iPhone 15 Pro', sales: 45, revenue: 359955.00 },
      { id: 2, name: '无线蓝牙耳机', sales: 123, revenue: 36777.00 },
      { id: 3, name: '笔记本电脑', sales: 28, revenue: 251720.00 },
      { id: 4, name: '智能手表', sales: 67, revenue: 133400.00 },
      { id: 5, name: '运动鞋', sales: 89, revenue: 44500.00 }
    ]
    
    const totalOrders = statistics.value.totalOrders
    orderStatusData.value = [
      { name: '已完成', count: 320, percentage: (320 / totalOrders) * 100, color: '#67c23a' },
      { name: '已发货', count: 78, percentage: (78 / totalOrders) * 100, color: '#409eff' },
      { name: '处理中', count: 45, percentage: (45 / totalOrders) * 100, color: '#e6a23c' },
      { name: '待处理', count: 13, percentage: (13 / totalOrders) * 100, color: '#f56c6c' }
    ]
    
    const totalCustomers = statistics.value.totalCustomers
    regionData.value = [
      { name: '广东省', count: 89, percentage: (89 / totalCustomers) * 100 },
      { name: '北京市', count: 67, percentage: (67 / totalCustomers) * 100 },
      { name: '上海市', count: 45, percentage: (45 / totalCustomers) * 100 },
      { name: '浙江省', count: 33, percentage: (33 / totalCustomers) * 100 }
    ]
  } catch (error) {
    console.error('Failed to fetch statistics:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  setDateRange('month')
})
</script>

<style scoped>
.statistics {
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

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.metric-card {
  border-radius: 8px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.metric-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.metric-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.metric-icon {
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

.metric-icon.sales {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.metric-icon.orders {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.metric-icon.customers {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.metric-icon.avg-order {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.metric-info {
  flex: 1;
}

.metric-info h3 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.metric-info p {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #909399;
}

.metric-trend {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #f56c6c;
}

.metric-trend.positive {
  color: #67c23a;
}

.metric-trend .el-icon {
  margin-right: 4px;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  border-radius: 8px;
}

.card-header {
  font-weight: 600;
  color: #303133;
}

.chart-container {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  text-align: center;
  color: #909399;
}

.chart-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.chart-placeholder p {
  margin: 0 0 8px 0;
  font-size: 16px;
}

.chart-placeholder small {
  font-size: 12px;
}

.product-ranking {
  width: 100%;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.ranking-item:last-child {
  border-bottom: none;
}

.ranking-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: white;
  margin-right: 16px;
}

.rank-1 {
  background: #ffd700;
}

.rank-2 {
  background: #c0c0c0;
}

.rank-3 {
  background: #cd7f32;
}

.ranking-number:not(.rank-1):not(.rank-2):not(.rank-3) {
  background: #909399;
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.product-sales {
  font-size: 12px;
  color: #909399;
}

.product-revenue {
  font-weight: 600;
  color: #e6a23c;
}

.status-distribution,
.region-distribution {
  width: 100%;
}

.status-item,
.region-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.status-item:last-child,
.region-item:last-child {
  border-bottom: none;
}

.status-info {
  width: 80px;
  margin-right: 16px;
}

.status-name,
.region-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.status-count {
  font-size: 12px;
  color: #909399;
}

.status-bar,
.region-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  margin-right: 16px;
  overflow: hidden;
}

.status-progress,
.region-progress {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s;
}

.region-progress {
  background: #409eff;
}

.status-percentage,
.region-count {
  width: 80px;
  text-align: right;
  font-size: 12px;
  color: #909399;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>