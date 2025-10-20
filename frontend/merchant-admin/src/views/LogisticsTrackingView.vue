<template>
  <Layout>
    <div class="logistics-tracking">
      <div class="page-header">
        <h1>物流跟踪查询</h1>
        <p>输入运单号查询物流信息</p>
      </div>

      <!-- 查询表单 -->
      <el-card class="search-card">
        <el-form
          :model="searchForm"
          inline
          @submit.prevent="searchLogistics"
        >
          <el-form-item label="运单号">
            <el-input
              v-model="searchForm.trackingNumber"
              placeholder="请输入运单号"
              style="width: 300px"
              clearable
            />
          </el-form-item>
          <el-form-item label="快递公司">
            <el-select
              v-model="searchForm.expressCompany"
              placeholder="请选择快递公司"
              style="width: 200px"
              clearable
            >
              <el-option label="全部" value="" />
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
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              @click="searchLogistics"
              :icon="Search"
            >
              查询
            </el-button>
            <el-button @click="resetSearch" :icon="Refresh">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 物流信息展示 -->
      <el-card v-if="logisticsInfo" class="logistics-info-card">
        <template #header>
          <div class="card-header">
            <span>物流信息</span>
            <el-tag
              :type="getLogisticsStatusType(logisticsInfo.status)"
              size="large"
            >
              {{ logisticsInfo.status }}
            </el-tag>
          </div>
        </template>

        <div class="logistics-details">
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="detail-item">
                <label>运单号：</label>
                <span>{{ logisticsInfo.trackingNumber }}</span>
              </div>
              <div class="detail-item">
                <label>快递公司：</label>
                <span>{{ logisticsInfo.expressCompany }}</span>
              </div>
              <div class="detail-item">
                <label>当前位置：</label>
                <span>{{ logisticsInfo.currentLocation || '暂无信息' }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>收件人：</label>
                <span>{{ logisticsInfo.receiverName }}</span>
              </div>
              <div class="detail-item">
                <label>联系电话：</label>
                <span>{{ logisticsInfo.receiverPhone }}</span>
              </div>
              <div class="detail-item">
                <label>收货地址：</label>
                <span>{{ logisticsInfo.receiverAddress }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="24" style="margin-top: 20px">
            <el-col :span="12">
              <div class="detail-item">
                <label>发货时间：</label>
                <span>{{ formatDate(logisticsInfo.shippedAt) }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="detail-item">
                <label>预计送达：</label>
                <span>{{ formatDate(logisticsInfo.estimatedDeliveryTime) }}</span>
              </div>
            </el-col>
          </el-row>

          <div v-if="logisticsInfo.deliveredAt" class="detail-item" style="margin-top: 20px">
            <label>签收时间：</label>
            <span>{{ formatDate(logisticsInfo.deliveredAt) }}</span>
          </div>
        </div>
      </el-card>

      <!-- 物流轨迹 -->
      <el-card v-if="logisticsTracks.length > 0" class="logistics-tracks-card">
        <template #header>
          <span>物流轨迹</span>
        </template>

        <el-timeline>
          <el-timeline-item
            v-for="(track, index) in logisticsTracks"
            :key="track.id"
            :timestamp="formatDate(track.trackTime)"
            :type="index === 0 ? 'primary' : 'info'"
          >
            <div class="track-content">
              <div class="track-status">
                <el-tag
                  :type="getLogisticsStatusType(track.status)"
                  size="small"
                >
                  {{ track.status }}
                </el-tag>
              </div>
              <div class="track-description">{{ track.description }}</div>
              <div v-if="track.location" class="track-location">
                <el-icon><Location /></el-icon>
                {{ track.location }}
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>

      <!-- 空状态 -->
      <el-empty
        v-if="!logisticsInfo && !loading && searched"
        description="未找到相关物流信息"
        :image-size="200"
      />
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import Layout from '@/components/Layout.vue'
import { Search, Refresh, Location } from '@element-plus/icons-vue'

const loading = ref(false)
const searched = ref(false)

// 搜索表单
const searchForm = reactive({
  trackingNumber: '',
  expressCompany: ''
})

// 物流信息
const logisticsInfo = ref<any>(null)
const logisticsTracks = ref<any[]>([])

// 搜索物流信息
const searchLogistics = async () => {
  if (!searchForm.trackingNumber.trim()) {
    ElMessage.warning('请输入运单号')
    return
  }

  loading.value = true
  searched.value = true

  try {
    // TODO: 调用API查询物流信息
    // const response = await api.get('/api/logistics/track', {
    //   params: {
    //     trackingNumber: searchForm.trackingNumber,
    //     expressCompany: searchForm.expressCompany
    //   }
    // })
    // logisticsInfo.value = response.data.logistics
    // logisticsTracks.value = response.data.tracks

    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    logisticsInfo.value = {
      id: '1',
      trackingNumber: searchForm.trackingNumber,
      expressCompany: searchForm.expressCompany || '顺丰速运',
      status: '运输中',
      currentLocation: '北京分拣中心',
      receiverName: '张三',
      receiverPhone: '13800138000',
      receiverAddress: '北京市朝阳区xxx街道xxx号',
      shippedAt: '2024-01-10 10:30:00',
      estimatedDeliveryTime: '2024-01-15 18:00:00',
      deliveredAt: null
    }

    logisticsTracks.value = [
      {
        id: '1',
        status: '运输中',
        description: '快件在北京分拣中心进行分拣',
        location: '北京分拣中心',
        trackTime: '2024-01-12 14:30:00'
      },
      {
        id: '2',
        status: '运输中',
        description: '快件已从上海转运中心发出',
        location: '上海转运中心',
        trackTime: '2024-01-11 20:15:00'
      },
      {
        id: '3',
        status: '已发货',
        description: '商家已发货，快件已交给快递公司',
        location: '上海',
        trackTime: '2024-01-10 10:30:00'
      }
    ]

    ElMessage.success('查询成功')
  } catch (error) {
    console.error('查询物流信息失败:', error)
    ElMessage.error('查询失败，请稍后重试')
    logisticsInfo.value = null
    logisticsTracks.value = []
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.trackingNumber = ''
  searchForm.expressCompany = ''
  logisticsInfo.value = null
  logisticsTracks.value = []
  searched.value = false
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

// 格式化日期
const formatDate = (dateString: string | null) => {
  if (!dateString) return '暂无信息'
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.logistics-tracking {
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

.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.logistics-info-card,
.logistics-tracks-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logistics-details {
  padding: 20px 0;
}

.detail-item {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.detail-item label {
  font-weight: 600;
  color: #606266;
  min-width: 80px;
  margin-right: 8px;
}

.detail-item span {
  color: #303133;
}

.track-content {
  padding: 8px 0;
}

.track-status {
  margin-bottom: 8px;
}

.track-description {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.track-location {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-timeline-item__timestamp) {
  font-size: 12px;
  color: #909399;
}

:deep(.el-empty) {
  padding: 60px 0;
}
</style>