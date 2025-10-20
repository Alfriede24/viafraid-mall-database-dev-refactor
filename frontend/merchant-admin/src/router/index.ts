import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/',
      name: 'dashboard',
      component: () => import('../views/DashboardView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/orders',
      name: 'orders',
      component: () => import('../views/OrdersView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/products',
      name: 'products',
      component: () => import('../views/ProductsView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: () => import('../views/StatisticsView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfileView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/logistics-tracking',
      name: 'logistics-tracking',
      component: () => import('../views/LogisticsTrackingView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/customer-service',
      name: 'customer-service',
      component: () => import('../views/CustomerServiceView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/customer-service-statistics',
      name: 'customer-service-statistics',
      component: () => import('../views/CustomerServiceStatistics.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/coupons',
      name: 'coupons',
      component: () => import('../views/CouponsView.vue'),
      meta: { requiresAuth: true }
    }
  ],
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // 初始化认证状态
  if (authStore.token && !authStore.merchant) {
    await authStore.init()
  }
  
  // 需要登录的页面
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')
    return
  }
  
  // 已登录用户访问登录页面，重定向到首页
  if (to.meta.requiresGuest && authStore.isLoggedIn) {
    next('/')
    return
  }
  
  next()
})

export default router
