<template>
  <div class="customer-service-statistics">
    <div class="page-header">
      <h1>客服工作统计</h1>
      <p>客服绩效数据和工作负载统计</p>
    </div>

    <!-- 概览卡片 -->
    <div class="overview-cards">
      <div class="card">
        <div class="card-icon online">
          <i class="el-icon-user"></i>
        </div>
        <div class="card-content">
          <h3>{{ dashboardData.onlineCustomerServices }}</h3>
          <p>在线客服</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon waiting">
          <i class="el-icon-clock"></i>
        </div>
        <div class="card-content">
          <h3>{{ dashboardData.waitingSessions }}</h3>
          <p>等待分配</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon active">
          <i class="el-icon-chat-dot-round"></i>
        </div>
        <div class="card-content">
          <h3>{{ dashboardData.activeSessions }}</h3>
          <p>活跃会话</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon today">
          <i class="el-icon-data-line"></i>
        </div>
        <div class="card-content">
          <h3>{{ dashboardData.todayTotalSessions }}</h3>
          <p>今日会话</p>
        </div>
      </div>
    </div>

    <!-- 客服绩效表格 -->
    <div class="performance-section">
      <div class="section-header">
        <h2>客服绩效排行</h2>
        <el-button @click="refreshData" :loading="loading">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>
      
      <el-table :data="dashboardData.performances" v-loading="loading" class="performance-table">
        <el-table-column prop="name" label="客服姓名" width="120">
          <template #default="{ row }">
            <div class="service-name">
              <span class="status-dot" :class="{ online: row.isOnline }"></span>
              {{ row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalSessions" label="本月会话" width="100" sortable />
        <el-table-column prop="totalMessages" label="本月消息" width="100" sortable />
        <el-table-column prop="activeSessions" label="活跃会话" width="100" />
        <el-table-column prop="averageResponseTime" label="平均响应时间" width="120">
          <template #default="{ row }">
            {{ formatTime(row.averageResponseTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="satisfactionRate" label="满意度" width="100">
          <template #default="{ row }">
            <el-tag :type="getSatisfactionType(row.satisfactionRate)">
              {{ row.satisfactionRate }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastActiveAt" label="最后活跃" width="150">
          <template #default="{ row }">
            {{ formatDateTime(row.lastActiveAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetails(row.customerServiceId)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 会话分配统计 -->
    <div class="distribution-section">
      <div class="section-header">
        <h2>会话分配统计</h2>
      </div>
      
      <el-table :data="dashboardData.sessionDistribution" class="distribution-table">
        <el-table-column prop="customerServiceName" label="客服姓名" width="120" />
        <el-table-column prop="totalSessions" label="总会话数" width="100" sortable />
        <el-table-column prop="todaySessions" label="今日会话" width="100" />
        <el-table-column prop="closedSessions" label="已结束" width="100" />
        <el-table-column prop="averageSessionDuration" label="平均时长" width="120">
          <template #default="{ row }">
            {{ formatTime(row.averageSessionDuration) }}
          </template>
        </el-table-column>
        <el-table-column label="完成率" width="100">
          <template #default="{ row }">
            <el-progress 
              :percentage="getCompletionRate(row)" 
              :color="getProgressColor(getCompletionRate(row))"
              :show-text="false"
              :stroke-width="8"
            />
            <span class="completion-text">{{ getCompletionRate(row) }}%</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 客服详情对话框 -->
    <el-dialog 
      v-model="detailDialogVisible" 
      title="客服详细统计" 
      width="600px"
      :before-close="closeDetailDialog"
    >
      <div v-if="selectedServiceStats" class="service-details">
        <div class="detail-cards">
          <div class="detail-card">
            <h4>今日数据</h4>
            <p>会话: {{ selectedServiceStats.todaySessions }}</p>
            <p>消息: {{ selectedServiceStats.todayMessages }}</p>
            <p>结束: {{ selectedServiceStats.todayClosedSessions }}</p>
          </div>
          <div class="detail-card">
            <h4>本周数据</h4>
            <p>会话: {{ selectedServiceStats.weekSessions }}</p>
            <p>消息: {{ selectedServiceStats.weekMessages }}</p>
          </div>
          <div class="detail-card">
            <h4>本月数据</h4>
            <p>会话: {{ selectedServiceStats.monthSessions }}</p>
            <p>消息: {{ selectedServiceStats.monthMessages }}</p>
          </div>
        </div>
        <div class="detail-metrics">
          <div class="metric">
            <label>平均响应时间:</label>
            <span>{{ formatTime(selectedServiceStats.averageResponseTime) }}</span>
          </div>
          <div class="metric">
            <label>客户满意度:</label>
            <span>{{ selectedServiceStats.satisfactionRate }}%</span>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="closeDetailDialog">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CustomerServiceStatistics',
  data() {
    return {
      loading: false,
      dashboardData: {
        onlineCustomerServices: 0,
        waitingSessions: 0,
        activeSessions: 0,
        todayTotalSessions: 0,
        todayTotalMessages: 0,
        averageWaitTime: 0,
        performances: [],
        sessionDistribution: []
      },
      detailDialogVisible: false,
      selectedServiceStats: null
    }
  },
  mounted() {
    this.loadDashboardData()
  },
  methods: {
    async loadDashboardData() {
      this.loading = true
      try {
        const response = await axios.get('/api/customer-service/statistics/dashboard')
        if (response.data.success) {
          this.dashboardData = response.data.data
        } else {
          this.$message.error('获取统计数据失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
        this.$message.error('获取统计数据失败')
      } finally {
        this.loading = false
      }
    },
    
    async refreshData() {
      await this.loadDashboardData()
      this.$message.success('数据已刷新')
    },
    
    async viewDetails(customerServiceId) {
      try {
        const response = await axios.get(`/api/customer-service/statistics/${customerServiceId}`)
        if (response.data.success) {
          this.selectedServiceStats = response.data.data
          this.detailDialogVisible = true
        } else {
          this.$message.error('获取详细统计失败: ' + response.data.message)
        }
      } catch (error) {
        console.error('获取详细统计失败:', error)
        this.$message.error('获取详细统计失败')
      }
    },
    
    closeDetailDialog() {
      this.detailDialogVisible = false
      this.selectedServiceStats = null
    },
    
    formatTime(minutes) {
      if (!minutes || minutes === 0) return '0分钟'
      if (minutes < 1) return '< 1分钟'
      return Math.round(minutes) + '分钟'
    },
    
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    },
    
    getSatisfactionType(rate) {
      if (rate >= 95) return 'success'
      if (rate >= 85) return 'warning'
      return 'danger'
    },
    
    getCompletionRate(row) {
      if (row.totalSessions === 0) return 0
      return Math.round((row.closedSessions / row.totalSessions) * 100)
    },
    
    getProgressColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 70) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.customer-service-statistics {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.card-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.card-icon.online {
  background: #67c23a;
}

.card-icon.waiting {
  background: #e6a23c;
}

.card-icon.active {
  background: #409eff;
}

.card-icon.today {
  background: #909399;
}

.card-content h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.card-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.performance-section,
.distribution-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.service-name {
  display: flex;
  align-items: center;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #c0c4cc;
  margin-right: 8px;
}

.status-dot.online {
  background: #67c23a;
}

.completion-text {
  margin-left: 8px;
  font-size: 12px;
  color: #909399;
}

.service-details {
  padding: 10px 0;
}

.detail-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-bottom: 20px;
}

.detail-card {
  background: #f5f7fa;
  border-radius: 6px;
  padding: 15px;
  text-align: center;
}

.detail-card h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #606266;
}

.detail-card p {
  margin: 5px 0;
  font-size: 13px;
  color: #909399;
}

.detail-metrics {
  border-top: 1px solid #ebeef5;
  padding-top: 15px;
}

.metric {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.metric label {
  color: #606266;
  font-size: 14px;
}

.metric span {
  color: #303133;
  font-weight: 500;
}
</style>