<template>
  <div class="coupons-container">
    <div class="page-header">
      <h2>优惠券管理</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建优惠券
      </el-button>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="优惠券名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入优惠券名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="全部" value="" />
            <el-option label="未开始" value="NotStarted" />
            <el-option label="进行中" value="Active" />
            <el-option label="已结束" value="Expired" />
            <el-option label="已停用" value="Disabled" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable style="width: 120px">
            <el-option label="全部" value="" />
            <el-option label="满减券" value="FullReduction" />
            <el-option label="折扣券" value="Discount" />
            <el-option label="免邮券" value="FreeShipping" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadCoupons">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 优惠券列表 -->
    <el-card class="table-card">
      <el-table
        :data="coupons"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="name" label="优惠券名称" min-width="150" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getCouponTypeTagType(row.type)">{{ getCouponTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优惠内容" min-width="120">
          <template #default="{ row }">
            <span v-if="row.type === 'FullReduction'">
              满{{ row.minAmount }}减{{ row.discountAmount }}
            </span>
            <span v-else-if="row.type === 'Discount'">
              {{ row.discountPercentage }}折
            </span>
            <span v-else-if="row.type === 'FreeShipping'">
              免邮券
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="totalQuantity" label="发放数量" width="100" />
        <el-table-column prop="usedQuantity" label="已使用" width="100" />
        <el-table-column label="有效期" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.startTime) }} 至 {{ formatDate(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getCouponStatusTagType(row.status)">{{ getCouponStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="editCoupon(row)">编辑</el-button>
            <el-button size="small" type="success" @click="distributeCoupon(row)">发放</el-button>
            <el-button
              size="small"
              :type="row.status === 'Disabled' ? 'success' : 'warning'"
              @click="toggleCouponStatus(row)"
            >
              {{ row.status === 'Disabled' ? '启用' : '停用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadCoupons"
          @current-change="loadCoupons"
        />
      </div>
    </el-card>

    <!-- 创建/编辑优惠券对话框 -->
    <el-dialog
      :title="editingCoupon ? '编辑优惠券' : '创建优惠券'"
      v-model="showCreateDialog"
      width="600px"
      @close="resetForm"
    >
      <el-form
        :model="couponForm"
        :rules="couponRules"
        ref="couponFormRef"
        label-width="100px"
      >
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="couponForm.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="couponForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入优惠券描述"
          />
        </el-form-item>
        <el-form-item label="优惠券类型" prop="type">
          <el-select v-model="couponForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="满减券" value="FullReduction" />
            <el-option label="折扣券" value="Discount" />
            <el-option label="免邮券" value="FreeShipping" />
          </el-select>
        </el-form-item>
        
        <!-- 满减券配置 -->
        <template v-if="couponForm.type === 'FullReduction'">
          <el-form-item label="最低消费" prop="minAmount">
            <el-input-number
              v-model="couponForm.minAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="减免金额" prop="discountAmount">
            <el-input-number
              v-model="couponForm.discountAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </template>
        
        <!-- 折扣券配置 -->
        <template v-if="couponForm.type === 'Discount'">
          <el-form-item label="最低消费" prop="minAmount">
            <el-input-number
              v-model="couponForm.minAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="折扣" prop="discountPercentage">
            <el-input-number
              v-model="couponForm.discountPercentage"
              :min="0.1"
              :max="9.9"
              :step="0.1"
              :precision="1"
              style="width: 100%"
            />
            <span style="margin-left: 8px; color: #999;">折</span>
          </el-form-item>
        </template>
        
        <!-- 免邮券配置 -->
        <template v-if="couponForm.type === 'FreeShipping'">
          <el-form-item label="最低消费" prop="minAmount">
            <el-input-number
              v-model="couponForm.minAmount"
              :min="0"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </template>
        
        <el-form-item label="发放数量" prop="totalQuantity">
          <el-input-number
            v-model="couponForm.totalQuantity"
            :min="1"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="每人限领" prop="limitPerUser">
          <el-input-number
            v-model="couponForm.limitPerUser"
            :min="1"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="有效期" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="saveCoupon" :loading="saving">
            {{ editingCoupon ? '更新' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 发放优惠券对话框 -->
    <el-dialog
      title="发放优惠券"
      v-model="showDistributeDialog"
      width="500px"
    >
      <el-form :model="distributeForm" label-width="100px">
        <el-form-item label="发放方式">
          <el-radio-group v-model="distributeForm.type">
            <el-radio label="all">全部用户</el-radio>
            <el-radio label="specific">指定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="distributeForm.type === 'specific'" label="用户手机号">
          <el-input
            v-model="distributeForm.phoneNumbers"
            type="textarea"
            :rows="4"
            placeholder="请输入用户手机号，多个手机号用换行分隔"
          />
        </el-form-item>
        <el-form-item label="发放数量">
          <el-input-number
            v-model="distributeForm.quantity"
            :min="1"
            :max="selectedCoupon?.totalQuantity - selectedCoupon?.usedQuantity"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDistributeDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmDistribute" :loading="distributing">
            发放
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const distributing = ref(false)
const showCreateDialog = ref(false)
const showDistributeDialog = ref(false)
const editingCoupon = ref(null)
const selectedCoupon = ref(null)
const couponFormRef = ref()
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  name: '',
  status: '',
  type: ''
})

// 优惠券列表
const coupons = ref([])

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 优惠券表单
const couponForm = reactive({
  name: '',
  description: '',
  type: '',
  minAmount: 0,
  discountAmount: 0,
  discountPercentage: 8.0,
  totalQuantity: 100,
  limitPerUser: 1,
  startTime: '',
  endTime: ''
})

// 发放表单
const distributeForm = reactive({
  type: 'all',
  phoneNumbers: '',
  quantity: 1
})

// 表单验证规则
const couponRules = {
  name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入优惠券描述', trigger: 'blur' }],
  type: [{ required: true, message: '请选择优惠券类型', trigger: 'change' }],
  totalQuantity: [{ required: true, message: '请输入发放数量', trigger: 'blur' }],
  limitPerUser: [{ required: true, message: '请输入每人限领数量', trigger: 'blur' }]
}

// 加载优惠券列表
const loadCoupons = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    const response = await api.get('/coupons', { params })
    coupons.value = response.data.items
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('加载优惠券列表失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    name: '',
    status: '',
    type: ''
  })
  pagination.page = 1
  loadCoupons()
}

// 编辑优惠券
const editCoupon = (coupon) => {
  editingCoupon.value = coupon
  Object.assign(couponForm, {
    ...coupon,
    discountPercentage: coupon.discountPercentage || 8.0
  })
  dateRange.value = [coupon.startTime, coupon.endTime]
  showCreateDialog.value = true
}

// 保存优惠券
const saveCoupon = async () => {
  if (!couponFormRef.value) return
  
  try {
    await couponFormRef.value.validate()
    
    if (!dateRange.value || dateRange.value.length !== 2) {
      ElMessage.error('请选择有效期')
      return
    }
    
    saving.value = true
    
    const formData = {
      ...couponForm,
      startTime: dateRange.value[0],
      endTime: dateRange.value[1]
    }
    
    if (editingCoupon.value) {
      await api.put(`/coupons/${editingCoupon.value.id}`, formData)
      ElMessage.success('更新优惠券成功')
    } else {
      await api.post('/coupons', formData)
      ElMessage.success('创建优惠券成功')
    }
    
    showCreateDialog.value = false
    loadCoupons()
  } catch (error) {
    ElMessage.error(editingCoupon.value ? '更新优惠券失败' : '创建优惠券失败')
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetForm = () => {
  editingCoupon.value = null
  Object.assign(couponForm, {
    name: '',
    description: '',
    type: '',
    minAmount: 0,
    discountAmount: 0,
    discountPercentage: 8.0,
    totalQuantity: 100,
    limitPerUser: 1,
    startTime: '',
    endTime: ''
  })
  dateRange.value = []
  couponFormRef.value?.resetFields()
}

// 发放优惠券
const distributeCoupon = (coupon) => {
  selectedCoupon.value = coupon
  distributeForm.quantity = 1
  distributeForm.type = 'all'
  distributeForm.phoneNumbers = ''
  showDistributeDialog.value = true
}

// 确认发放
const confirmDistribute = async () => {
  try {
    distributing.value = true
    
    const data = {
      couponId: selectedCoupon.value.id,
      quantity: distributeForm.quantity,
      distributionType: distributeForm.type,
      phoneNumbers: distributeForm.type === 'specific' ? 
        distributeForm.phoneNumbers.split('\n').filter(p => p.trim()) : []
    }
    
    await api.post('/coupons/distribute', data)
    ElMessage.success('发放优惠券成功')
    showDistributeDialog.value = false
    loadCoupons()
  } catch (error) {
    ElMessage.error('发放优惠券失败')
  } finally {
    distributing.value = false
  }
}

// 切换优惠券状态
const toggleCouponStatus = async (coupon) => {
  try {
    const action = coupon.status === 'Disabled' ? '启用' : '停用'
    await ElMessageBox.confirm(`确定要${action}这个优惠券吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const newStatus = coupon.status === 'Disabled' ? 'Active' : 'Disabled'
    await api.patch(`/coupons/${coupon.id}/status`, { status: newStatus })
    
    ElMessage.success(`${action}优惠券成功`)
    loadCoupons()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 工具函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

const getCouponTypeText = (type) => {
  const typeMap = {
    'FullReduction': '满减券',
    'Discount': '折扣券',
    'FreeShipping': '免邮券'
  }
  return typeMap[type] || type
}

const getCouponTypeTagType = (type) => {
  const typeMap = {
    'FullReduction': 'success',
    'Discount': 'warning',
    'FreeShipping': 'info'
  }
  return typeMap[type] || ''
}

const getCouponStatusText = (status) => {
  const statusMap = {
    'NotStarted': '未开始',
    'Active': '进行中',
    'Expired': '已结束',
    'Disabled': '已停用'
  }
  return statusMap[status] || status
}

const getCouponStatusTagType = (status) => {
  const statusMap = {
    'NotStarted': 'info',
    'Active': 'success',
    'Expired': 'warning',
    'Disabled': 'danger'
  }
  return statusMap[status] || ''
}

// 生命周期
onMounted(() => {
  loadCoupons()
})
</script>

<style scoped>
.coupons-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  min-height: 400px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>