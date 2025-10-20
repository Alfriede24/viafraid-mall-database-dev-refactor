<template>
  <div class="customer-service-container">
    <div class="service-header">
      <h2>客服工作台</h2>
      <div class="service-status">
        <el-switch
          v-model="isOnline"
          @change="handleStatusChange"
          active-text="在线"
          inactive-text="离线"
          active-color="#13ce66"
          inactive-color="#ff4949"
        />
      </div>
    </div>

    <div class="service-content">
      <!-- 会话列表 -->
      <div class="session-list">
        <div class="session-header">
          <h3>会话列表</h3>
          <el-select v-model="sessionFilter" @change="loadSessions" size="small">
            <el-option label="全部" value="all" />
            <el-option label="等待中" value="waiting" />
            <el-option label="进行中" value="active" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </div>
        
        <div class="session-items">
          <div
            v-for="session in sessions"
            :key="session.id"
            :class="['session-item', { active: currentSession?.id === session.id }]"
            @click="selectSession(session)"
          >
            <div class="session-avatar">
              <el-avatar :src="session.user?.avatar" :size="40">
                {{ session.user?.nickname?.charAt(0) || 'U' }}
              </el-avatar>
              <span v-if="session.unreadCount > 0" class="unread-badge">{{ session.unreadCount }}</span>
            </div>
            <div class="session-info">
              <div class="session-title">
                <span class="user-name">{{ session.user?.nickname || '匿名用户' }}</span>
                <span class="session-time">{{ formatTime(session.updatedAt) }}</span>
              </div>
              <div class="session-preview">
                <span class="last-message">{{ session.lastMessage?.content || '暂无消息' }}</span>
                <el-tag :type="getStatusType(session.status)" size="small">{{ getStatusText(session.status) }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 聊天窗口 -->
      <div class="chat-window">
        <div v-if="!currentSession" class="no-session">
          <el-empty description="请选择一个会话开始聊天" />
        </div>
        
        <div v-else class="chat-content">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <div class="user-info">
              <el-avatar :src="currentSession.user?.avatar" :size="32">
                {{ currentSession.user?.nickname?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="user-details">
                <span class="user-name">{{ currentSession.user?.nickname || '匿名用户' }}</span>
                <span class="user-phone">{{ currentSession.user?.phone || '未知' }}</span>
              </div>
            </div>
            <div class="chat-actions">
              <el-button v-if="currentSession.status === 'waiting'" type="primary" size="small" @click="acceptSession">
                接受会话
              </el-button>
              <el-button v-if="currentSession.status === 'active'" type="warning" size="small" @click="closeSession">
                结束会话
              </el-button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div ref="messageContainer" class="message-list">
            <div
              v-for="message in messages"
              :key="message.id"
              :class="['message-item', message.senderType === 'customer_service' ? 'sent' : 'received']"
            >
              <div class="message-avatar">
                <el-avatar :size="28" :src="message.senderType === 'customer_service' ? null : currentSession.user?.avatar">
                  {{ message.senderType === 'customer_service' ? 'CS' : (currentSession.user?.nickname?.charAt(0) || 'U') }}
                </el-avatar>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <div v-if="message.messageType === 'text'" class="text-message">
                    {{ message.content }}
                  </div>
                  <div v-else-if="message.messageType === 'image'" class="image-message">
                    <el-image :src="message.mediaUrl" fit="cover" style="max-width: 200px; max-height: 200px;" />
                  </div>
                  <div v-else-if="message.messageType === 'file'" class="file-message">
                    <div class="file-info">
                      <el-icon class="file-icon"><Document /></el-icon>
                      <div class="file-details">
                        <div class="file-name">{{ message.fileName }}</div>
                        <div class="file-size">{{ formatFileSize(message.fileSize) }}</div>
                      </div>
                      <el-button type="primary" size="small" @click="downloadFile(message.mediaUrl, message.fileName)">
                        下载
                      </el-button>
                    </div>
                  </div>
                </div>
                <div class="message-time">{{ formatTime(message.createdAt) }}</div>
              </div>
            </div>
          </div>

          <!-- 输入框 -->
          <div class="message-input">
            <div class="input-toolbar">
              <el-button type="text" @click="showEmojiPicker = !showEmojiPicker">
                <el-icon><Smile /></el-icon>
              </el-button>
              <el-upload
                :show-file-list="false"
                :before-upload="handleImageUpload"
                accept="image/*"
              >
                <el-button type="text">
                  <el-icon><Picture /></el-icon>
                </el-button>
              </el-upload>
              <el-upload
                :show-file-list="false"
                :before-upload="handleFileUpload"
                accept=".pdf,.doc,.docx,.txt,.zip,.rar"
              >
                <el-button type="text">
                  <el-icon><Document /></el-icon>
                </el-button>
              </el-upload>
            </div>
            <div class="input-area">
              <el-input
                v-model="messageText"
                type="textarea"
                :rows="3"
                placeholder="输入消息..."
                @keydown.enter.exact="sendMessage"
                @keydown.enter.shift.exact.prevent
              />
              <el-button type="primary" @click="sendMessage" :disabled="!messageText.trim()">
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 用户信息面板 -->
      <div class="user-panel">
        <div v-if="currentSession" class="user-details-panel">
          <h3>用户信息</h3>
          <div class="user-profile">
            <el-avatar :src="currentSession.user?.avatar" :size="60">
              {{ currentSession.user?.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="profile-info">
              <p><strong>昵称：</strong>{{ currentSession.user?.nickname || '匿名用户' }}</p>
              <p><strong>手机：</strong>{{ currentSession.user?.phone || '未知' }}</p>
              <p><strong>注册时间：</strong>{{ formatTime(currentSession.user?.createdAt) }}</p>
            </div>
          </div>
          
          <div class="session-details">
            <h4>会话信息</h4>
            <p><strong>会话状态：</strong><el-tag :type="getStatusType(currentSession.status)">{{ getStatusText(currentSession.status) }}</el-tag></p>
            <p><strong>创建时间：</strong>{{ formatTime(currentSession.createdAt) }}</p>
            <p><strong>最后更新：</strong>{{ formatTime(currentSession.updatedAt) }}</p>
            <p><strong>来源渠道：</strong>{{ currentSession.source || '小程序' }}</p>
          </div>
        </div>
        
        <div v-else class="no-user-info">
          <el-empty description="选择会话查看用户信息" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Smile, Picture } from '@element-plus/icons-vue'
import * as signalR from '@microsoft/signalr'
import axios from 'axios'

// 响应式数据
const isOnline = ref(false)
const sessionFilter = ref('all')
const sessions = ref([])
const currentSession = ref(null)
const messages = ref([])
const messageText = ref('')
const showEmojiPicker = ref(false)
const messageContainer = ref(null)

// SignalR连接
let connection = null

// 生命周期
onMounted(() => {
  initSignalR()
  loadSessions()
})

// 监听当前会话变化
watch(currentSession, (newSession) => {
  if (newSession) {
    loadMessages(newSession.id)
    markMessagesAsRead(newSession.id)
  }
})

// 初始化SignalR连接
const initSignalR = async () => {
  connection = new signalR.HubConnectionBuilder()
    .withUrl('http://localhost:5000/customerServiceHub')
    .build()

  try {
    await connection.start()
    console.log('SignalR连接成功')
    
    // 监听新消息
    connection.on('ReceiveMessage', (message) => {
      if (currentSession.value && message.sessionId === currentSession.value.id) {
        messages.value.push(message)
        scrollToBottom()
      } else {
        // 如果不是当前会话的消息，增加未读数量
        const session = sessions.value.find(s => s.id === message.sessionId)
        if (session) {
          session.unreadCount = (session.unreadCount || 0) + 1
        }
      }
      // 更新会话列表中的最后消息
      updateSessionLastMessage(message.sessionId, message)
    })
    
    // 监听消息已读状态
    connection.on('MessagesRead', (data) => {
      const { MessageIds, ReadAt } = data
      // 更新消息的已读状态
      messages.value.forEach(msg => {
        if (MessageIds.includes(msg.id)) {
          msg.isRead = true
          msg.readAt = ReadAt
        }
      })
    })
    
    // 监听会话状态变化
    connection.on('SessionStatusChanged', (sessionId, status) => {
      const session = sessions.value.find(s => s.id === sessionId)
      if (session) {
        session.status = status
      }
      if (currentSession.value && currentSession.value.id === sessionId) {
        currentSession.value.status = status
      }
    })
    
  } catch (err) {
    console.error('SignalR连接失败:', err)
    ElMessage.error('实时通信连接失败')
  }
}

// 加载会话列表
const loadSessions = async () => {
  try {
    const params = sessionFilter.value !== 'all' ? { status: sessionFilter.value } : {}
    const response = await axios.get('/api/CustomerServiceSession', { params })
    sessions.value = response.data.data || []
    
    // 加载每个会话的未读消息数量
    await loadUnreadCounts()
  } catch (error) {
    console.error('加载会话列表失败:', error)
    ElMessage.error('加载会话列表失败')
  }
}

// 加载未读消息数量
const loadUnreadCounts = async () => {
  try {
    const currentUserId = 1 // TODO: 从用户状态获取当前客服ID
    for (const session of sessions.value) {
      const response = await axios.get(`/api/CustomerServiceSession/${session.id}/unread-count?userId=${currentUserId}`)
      session.unreadCount = response.data || 0
    }
  } catch (error) {
    console.error('加载未读消息数量失败:', error)
  }
}

// 选择会话
const selectSession = async (session) => {
  currentSession.value = session
  if (connection) {
    await connection.invoke('JoinSession', session.id.toString())
  }
}

// 加载消息列表
const loadMessages = async (sessionId) => {
  try {
    const response = await axios.get(`/api/CustomerServiceMessage?sessionId=${sessionId}`)
    messages.value = response.data.data || []
    nextTick(() => scrollToBottom())
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  }
}

// 发送消息
const sendMessage = async () => {
  if (!messageText.value.trim() || !currentSession.value) return
  
  try {
    const messageData = {
      sessionId: currentSession.value.id,
      messageType: 'text',
      content: messageText.value.trim()
    }
    
    await axios.post('/api/CustomerServiceMessage/send', messageData)
    
    if (connection) {
      await connection.invoke('SendMessage', currentSession.value.id.toString(), messageData)
    }
    
    messageText.value = ''
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败')
  }
}

// 发送媒体消息
const sendMediaMessage = async (messageType, content, mediaUrl, fileName, fileSize) => {
  if (!currentSession.value) return
  
  try {
    await connection.invoke('SendMessage', {
      sessionId: currentSession.value.id,
      messageType: messageType,
      content: content,
      mediaUrl: mediaUrl,
      fileName: fileName,
      fileSize: fileSize
    })
  } catch (error) {
    console.error('发送媒体消息失败:', error)
    ElMessage.error('发送消息失败')
  }
}

// 下载文件
const downloadFile = (url, fileName) => {
  const link = document.createElement('a')
  link.href = url
  link.download = fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 接受会话
const acceptSession = async () => {
  try {
    await axios.put(`/api/CustomerServiceSession/${currentSession.value.id}/assign`, {
      status: 'active'
    })
    currentSession.value.status = 'active'
    ElMessage.success('已接受会话')
  } catch (error) {
    console.error('接受会话失败:', error)
    ElMessage.error('接受会话失败')
  }
}

// 关闭会话
const closeSession = async () => {
  try {
    await ElMessageBox.confirm('确定要结束这个会话吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await axios.put(`/api/CustomerServiceSession/${currentSession.value.id}`, {
      status: 'closed',
      closeReason: '客服主动关闭'
    })
    
    currentSession.value.status = 'closed'
    ElMessage.success('会话已结束')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('关闭会话失败:', error)
      ElMessage.error('关闭会话失败')
    }
  }
}

// 标记消息已读
const markMessagesAsRead = async (sessionId) => {
  try {
    const currentUserId = 1 // TODO: 从用户状态获取当前客服ID
    await axios.put(`/api/CustomerServiceSession/${sessionId}/mark-read?userId=${currentUserId}`)
    
    // 更新会话的未读数量
    const session = sessions.value.find(s => s.id === sessionId)
    if (session) {
      session.unreadCount = 0
    }
    
    // 通过SignalR通知已读状态
    if (connection) {
      await connection.invoke('MarkSessionMessagesAsRead', sessionId)
    }
  } catch (error) {
    console.error('标记消息已读失败:', error)
  }
}

// 处理图片上传
const handleImageUpload = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await fetch('/api/Upload/image', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: formData
    })
    
    const result = await response.json()
    
    if (result.success) {
      // 发送图片消息
      await sendMediaMessage('image', '', result.data.url, result.data.fileName, result.data.size)
      ElMessage.success('图片发送成功')
    } else {
      ElMessage.error(result.message || '图片上传失败')
    }
  } catch (error) {
    console.error('图片上传失败:', error)
    ElMessage.error('图片上传失败')
  }
  return false
}

// 处理文件上传
const handleFileUpload = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await fetch('/api/Upload/file', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: formData
    })
    
    const result = await response.json()
    
    if (result.success) {
      // 发送文件消息
      await sendMediaMessage('file', '', result.data.url, result.data.fileName, result.data.size)
      ElMessage.success('文件发送成功')
    } else {
      ElMessage.error(result.message || '文件上传失败')
    }
  } catch (error) {
    console.error('文件上传失败:', error)
    ElMessage.error('文件上传失败')
  }
  return false
}

// 更新会话最后消息
const updateSessionLastMessage = (sessionId, message) => {
  const session = sessions.value.find(s => s.id === sessionId)
  if (session) {
    session.lastMessage = message
    session.updatedAt = message.createdAt
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    }
  })
}

// 处理在线状态变化
const handleStatusChange = async (status) => {
  try {
    // TODO: 调用API更新客服在线状态
    ElMessage.success(status ? '已上线' : '已下线')
  } catch (error) {
    console.error('更新状态失败:', error)
    ElMessage.error('更新状态失败')
    isOnline.value = !status // 回滚状态
  }
}

// 工具函数
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return date.toLocaleDateString()
}

const getStatusType = (status) => {
  const types = {
    waiting: 'warning',
    active: 'success',
    closed: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    waiting: '等待中',
    active: '进行中',
    closed: '已关闭'
  }
  return texts[status] || '未知'
}
</script>

<style scoped>
.customer-service-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.service-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
}

.service-header h2 {
  margin: 0;
  color: #303133;
}

.service-content {
  flex: 1;
  display: flex;
  min-height: 0;
}

.session-list {
  width: 300px;
  background: white;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.session-header {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.session-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.session-items {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  display: flex;
  padding: 12px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;
}

.session-item:hover {
  background: #f5f7fa;
}

.session-item.active {
  background: #ecf5ff;
  border-right: 3px solid #409eff;
}

.session-avatar {
  position: relative;
  margin-right: 12px;
}

.unread-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #f56c6c;
  color: white;
  border-radius: 10px;
  padding: 2px 6px;
  font-size: 12px;
  min-width: 16px;
  text-align: center;
}

.session-info {
  flex: 1;
  min-width: 0;
}

.session-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.user-name {
  font-weight: 500;
  color: #303133;
}

.session-time {
  font-size: 12px;
  color: #909399;
}

.session-preview {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.last-message {
  flex: 1;
  font-size: 13px;
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-right: 8px;
}

.chat-window {
  flex: 1;
  background: white;
  display: flex;
  flex-direction: column;
}

.no-session {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 16px 24px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-details {
  margin-left: 12px;
}

.user-name {
  display: block;
  font-weight: 500;
  color: #303133;
}

.user-phone {
  font-size: 12px;
  color: #909399;
}

.message-list {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #f8f9fa;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
}

.message-item.sent {
  flex-direction: row-reverse;
}

.message-avatar {
  margin: 0 8px;
}

.message-content {
  max-width: 60%;
}

.message-item.sent .message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.message-bubble {
  padding: 8px 12px;
  border-radius: 8px;
  word-wrap: break-word;
}

.message-item.received .message-bubble {
  background: white;
  border: 1px solid #e4e7ed;
}

.message-item.sent .message-bubble {
  background: #409eff;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.message-input {
  border-top: 1px solid #e4e7ed;
  padding: 16px;
}

.input-toolbar {
  margin-bottom: 8px;
}

.input-area {
  display: flex;
  gap: 8px;
}

.input-area .el-textarea {
  flex: 1;
}

.user-panel {
  width: 280px;
  background: white;
  border-left: 1px solid #e4e7ed;
  padding: 16px;
}

.user-details-panel h3 {
  margin: 0 0 16px 0;
  color: #303133;
}

.user-profile {
  text-align: center;
  margin-bottom: 24px;
}

.profile-info {
  margin-top: 12px;
}

.profile-info p {
  margin: 8px 0;
  font-size: 14px;
  color: #606266;
}

.session-details h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 14px;
}

.session-details p {
  margin: 8px 0;
  font-size: 13px;
  color: #606266;
}

.image-message {
  max-width: 200px;
}

.file-message {
  .file-info {
    display: flex;
    align-items: center;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 8px;
    border: 1px solid #e4e7ed;
    max-width: 300px;
    
    .file-icon {
      font-size: 24px;
      color: #409eff;
      margin-right: 12px;
    }
    
    .file-details {
      flex: 1;
      margin-right: 12px;
      
      .file-name {
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
        word-break: break-all;
      }
      
      .file-size {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.no-user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
}
</style>