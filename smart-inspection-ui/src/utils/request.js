import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
    baseURL: '/api', // Proxy to backend
    timeout: 5000
})

// Request interceptor
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

import router from '@/router'

// Response interceptor
service.interceptors.response.use(
    response => {
        const res = response.data
        if (res.code === 401 || res.code === 403) {
            ElMessage.error('登录已过期，请重新登录')
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            localStorage.removeItem('dataScope')
            router.push('/login')
            return Promise.reject(new Error(res.msg || 'Unauthorized'))
        }
        if (res.code !== 200) {
            ElMessage.error(res.msg || 'Error')
            return Promise.reject(new Error(res.msg || 'Error'))
        } else {
            return res.data
        }
    },
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            ElMessage.error('未授权或登录已过期，请重新登录')
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            localStorage.removeItem('dataScope')
            router.push('/login')
        } else {
            ElMessage.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default service
