import request from '@/utils/request'

export function listDept(query) {
    return request({
        url: '/admin/dept/list',
        method: 'get',
        params: query
    })
}

export function getDept(deptId) {
    return request({
        url: '/admin/dept/' + deptId,
        method: 'get'
    })
}

export function addDept(data) {
    return request({
        url: '/admin/dept',
        method: 'post',
        data: data
    })
}

export function updateDept(data) {
    return request({
        url: '/admin/dept',
        method: 'put',
        data: data
    })
}

export function delDept(deptId) {
    return request({
        url: '/admin/dept/' + deptId,
        method: 'delete'
    })
}
