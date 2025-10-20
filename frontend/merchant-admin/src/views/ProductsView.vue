<template>
  <Layout>
    <div class="products">
      <div class="page-header">
        <h1>商品管理</h1>
        <p>管理您的商品库存和信息</p>
      </div>
      
      <!-- 操作栏 -->
      <el-card class="action-card">
        <div class="action-bar">
          <div class="action-left">
            <el-button type="primary" @click="showAddDialog">
              <el-icon><Plus /></el-icon>
              添加商品
            </el-button>
            <el-button type="danger" :disabled="selectedProducts.length === 0" @click="batchDelete">
              <el-icon><Delete /></el-icon>
              批量删除
            </el-button>
          </div>
          
          <div class="action-right">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索商品名称或描述"
              style="width: 300px; margin-right: 10px"
              clearable
              @keyup.enter="searchProducts"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="searchProducts">搜索</el-button>
          </div>
        </div>
      </el-card>
      
      <!-- 商品列表 -->
      <el-card class="products-table">
        <el-table
          :data="products"
          v-loading="loading"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column label="商品图片" width="100">
            <template #default="{ row }">
              <el-image
                :src="row.mainImageUrl || '/placeholder.jpg'"
                :preview-src-list="[row.mainImageUrl || '/placeholder.jpg']"
                class="product-image"
                fit="cover"
              />
            </template>
          </el-table-column>
          
          <el-table-column prop="name" label="商品名称" width="200" />
          
          <el-table-column prop="description" label="商品描述" width="250" show-overflow-tooltip />
          
          <el-table-column prop="price" label="价格" width="100">
            <template #default="{ row }">
              <span class="price">¥{{ row.price.toFixed(2) }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="stock" label="库存" width="80">
            <template #default="{ row }">
              <el-tag :type="row.stock > 10 ? 'success' : row.stock > 0 ? 'warning' : 'danger'">
                {{ row.stock }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="categoryName" label="分类" width="120" />
          
          <el-table-column prop="isActive" label="状态" width="80">
            <template #default="{ row }">
              <el-switch
                v-model="row.isActive"
                @change="toggleProductStatus(row)"
              />
            </template>
          </el-table-column>
          
          <el-table-column prop="createdAt" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="editProduct(row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="deleteProduct(row)">
                删除
              </el-button>
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
      
      <!-- 添加/编辑商品对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑商品' : '添加商品'"
        width="600px"
        @close="resetForm"
      >
        <el-form
          ref="productFormRef"
          :model="productForm"
          :rules="productRules"
          label-width="100px"
        >
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="productForm.name" placeholder="请输入商品名称" />
          </el-form-item>
          
          <el-form-item label="商品描述" prop="description">
            <el-input
              v-model="productForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入商品描述"
            />
          </el-form-item>
          
          <el-form-item label="商品价格" prop="price">
            <el-input-number
              v-model="productForm.price"
              :min="0"
              :precision="2"
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="库存数量" prop="stock">
            <el-input-number
              v-model="productForm.stock"
              :min="0"
              style="width: 200px"
            />
          </el-form-item>
          
          <el-form-item label="商品分类" prop="categoryId">
            <el-select v-model="productForm.categoryId" placeholder="请选择分类" style="width: 200px">
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="商品图片" prop="mainImageUrl">
            <el-input v-model="productForm.mainImageUrl" placeholder="请输入图片URL" />
          </el-form-item>
          
          <el-form-item label="是否上架" prop="isActive">
            <el-switch v-model="productForm.isActive" />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveProduct" :loading="saving">
              {{ saving ? '保存中...' : '保存' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElForm } from 'element-plus'
import Layout from '@/components/Layout.vue'
import { Plus, Delete, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const selectedProducts = ref([])
const searchKeyword = ref('')
const productFormRef = ref<InstanceType<typeof ElForm>>()

// 分页
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 商品列表
const products = ref([])

// 分类列表
const categories = ref([
  { id: 1, name: '电子产品' },
  { id: 2, name: '服装鞋帽' },
  { id: 3, name: '家居用品' },
  { id: 4, name: '食品饮料' },
  { id: 5, name: '图书文具' }
])

// 商品表单
const productForm = reactive({
  id: null,
  name: '',
  description: '',
  price: 0,
  stock: 0,
  categoryId: null,
  mainImageUrl: '',
  isActive: true
})

// 表单验证规则
const productRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 100, message: '商品名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' },
    { max: 500, message: '商品描述不能超过 500 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格不能小于 0', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存不能小于 0', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ]
}

// 获取商品列表
const fetchProducts = async () => {
  loading.value = true
  try {
    // TODO: 调用API获取商品数据
    // 模拟数据
    products.value = [
      {
        id: 1,
        name: 'iPhone 15 Pro',
        description: '苹果最新款智能手机，配备A17 Pro芯片',
        price: 7999.00,
        stock: 50,
        categoryId: 1,
        categoryName: '电子产品',
        mainImageUrl: 'https://via.placeholder.com/100x100',
        isActive: true,
        createdAt: new Date().toISOString()
      },
      {
        id: 2,
        name: '无线蓝牙耳机',
        description: '高品质音质，长续航时间',
        price: 299.00,
        stock: 0,
        categoryId: 1,
        categoryName: '电子产品',
        mainImageUrl: 'https://via.placeholder.com/100x100',
        isActive: true,
        createdAt: new Date(Date.now() - 86400000).toISOString()
      },
      {
        id: 3,
        name: '运动T恤',
        description: '透气舒适，适合运动穿着',
        price: 89.00,
        stock: 5,
        categoryId: 2,
        categoryName: '服装鞋帽',
        mainImageUrl: 'https://via.placeholder.com/100x100',
        isActive: false,
        createdAt: new Date(Date.now() - 172800000).toISOString()
      }
    ]
    pagination.total = 100 // 模拟总数
  } catch (error) {
    console.error('Failed to fetch products:', error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索商品
const searchProducts = () => {
  pagination.page = 1
  fetchProducts()
}

// 处理选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedProducts.value = selection
}

// 处理页面大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchProducts()
}

// 处理当前页变化
const handleCurrentChange = (page: number) => {
  pagination.page = page
  fetchProducts()
}

// 显示添加对话框
const showAddDialog = () => {
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑商品
const editProduct = (product: any) => {
  isEdit.value = true
  Object.assign(productForm, product)
  dialogVisible.value = true
}

// 保存商品
const saveProduct = async () => {
  if (!productFormRef.value) return
  
  try {
    const valid = await productFormRef.value.validate()
    if (!valid) return
    
    saving.value = true
    
    // TODO: 调用API保存商品
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用
    
    ElMessage.success(isEdit.value ? '商品更新成功' : '商品添加成功')
    dialogVisible.value = false
    fetchProducts()
  } catch (error) {
    console.error('Save product error:', error)
  } finally {
    saving.value = false
  }
}

// 删除商品
const deleteProduct = async (product: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品 "${product.name}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API删除商品
    ElMessage.success('商品删除成功')
    fetchProducts()
  } catch {
    // 用户取消
  }
}

// 批量删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedProducts.value.length} 个商品吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API批量删除商品
    ElMessage.success('商品批量删除成功')
    fetchProducts()
  } catch {
    // 用户取消
  }
}

// 切换商品状态
const toggleProductStatus = async (product: any) => {
  try {
    // TODO: 调用API更新商品状态
    ElMessage.success(`商品已${product.isActive ? '上架' : '下架'}`)
  } catch (error) {
    console.error('Toggle product status error:', error)
    // 恢复状态
    product.isActive = !product.isActive
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(productForm, {
    id: null,
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: null,
    mainImageUrl: '',
    isActive: true
  })
  productFormRef.value?.clearValidate()
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
  fetchProducts()
})
</script>

<style scoped>
.products {
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

.action-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-left {
  display: flex;
  gap: 10px;
}

.action-right {
  display: flex;
  align-items: center;
}

.products-table {
  border-radius: 8px;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
}

.price {
  font-weight: 600;
  color: #e6a23c;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 4px;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>