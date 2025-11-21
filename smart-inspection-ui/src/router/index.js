import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/',
            name: 'dashboard',
            component: () => import('../views/Dashboard.vue'),
            children: [
                {
                    path: '',
                    name: 'dashboard-home',
                    component: () => import('../views/DashboardHome.vue')
                },
                {
                    path: 'config',
                    name: 'inspection-config',
                    component: () => import('../views/InspectionConfig.vue')
                },
                {
                    path: 'users',
                    name: 'user-management',
                    component: () => import('../views/UserManagement.vue')
                }
            ]
        }
    ]
})

export default router
