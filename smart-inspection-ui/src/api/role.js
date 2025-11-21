import request from '@/utils/request'

export function listRole(query) {
    return request({
        url: '/admin/role/list',
        method: 'get',
        params: query
    })
}

export function getRole(roleId) {
    return request({
        url: '/admin/role/' + roleId,
        method: 'get'
    })
}

export function addRole(data) {
    return request({
        url: '/admin/role',
        method: 'post',
        data: data
    })
}

export function updateRole(data) {
    return request({
        url: '/admin/role',
        method: 'put',
        data: data
    })
}

export function delRole(roleId) {
    return request({
        url: '/admin/role/' + roleId,
        method: 'delete'
    })
}
