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
                },
                {
                    path: 'roles',
                    name: 'role-management',
                    component: () => import('../views/RoleManagement.vue')
                },
                {
                    path: 'depts',
                    name: 'dept-management',
                    component: () => import('../views/DeptManagement.vue')
                },
                {
                    path: 'inspection/level',
                    name: 'inspection-level',
                    component: () => import('../views/inspection/InspectionLevel.vue')
                },
                {
                    path: 'inspection/category',
                    name: 'inspection-category',
                    component: () => import('../views/inspection/InspectionCategory.vue')
                },
                {
                    path: 'inspection/item',
                    name: 'inspection-item',
                    component: () => import('../views/inspection/InspectionItem.vue')
                },
                {
                    path: 'inspection/template',
                    name: 'inspection-template',
                    component: () => import('../views/inspection/InspectionTemplate.vue')
                },
                {
                    path: 'task/list',
                    name: 'task-management',
                    component: () => import('../views/TaskManagement.vue')
                },
                {
                    path: 'task/detail/:taskId',
                    name: 'task-detail',
                    component: () => import('../views/TaskDetail.vue'),
                    hidden: true
                },
                {
                    path: 'workflow/design',
                    name: 'workflow-design',
                    component: () => import('../views/workflow/WorkflowDesign.vue')
                },
                {
                    path: 'workflow/task',
                    name: 'my-task',
                    component: () => import('../views/workflow/MyTask.vue')
                },
                {
                    path: 'punish/list',
                    name: 'punish-list',
                    component: () => import('../views/punish/PunishmentList.vue')
                }
            ]
        }
    ]
})

export default router
