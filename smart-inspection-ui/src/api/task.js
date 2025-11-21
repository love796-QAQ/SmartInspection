import request from '@/utils/request'

export function listTask(query) {
    return request({
        url: '/admin/task/list',
        method: 'get',
        params: query
    })
}

export function getTask(taskId) {
    return request({
        url: '/admin/task/' + taskId,
        method: 'get'
    })
}


export function assignTask(data) {
    return request({
        url: '/admin/task/assign',
        method: 'post',
        data: data
    })
}

export function getTaskItems(taskId) {
    return request({
        url: '/admin/task/' + taskId + '/items',
        method: 'get'
    })
}
