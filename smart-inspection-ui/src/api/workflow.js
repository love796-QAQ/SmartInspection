import request from '@/utils/request'

export function deployProcess(data) {
    return request({
        url: '/admin/workflow/deploy',
        method: 'post',
        data: data
    })
}

export function startProcess(data) {
    return request({
        url: '/admin/workflow/start',
        method: 'post',
        data: data
    })
}

export function getMyTasks(assignee) {
    return request({
        url: '/admin/workflow/tasks',
        method: 'get',
        params: { assignee }
    })
}

export function completeTask(data) {
    return request({
        url: '/admin/workflow/complete',
        method: 'post',
        data: data
    })
}

export function getHistory(assignee) {
    return request({
        url: '/admin/workflow/history',
        method: 'get',
        params: { assignee }
    })
}
