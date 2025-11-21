import request from '@/utils/request'

export function listUser(query) {
    return request({
        url: '/admin/user/list',
        method: 'get',
        params: query
    })
}

export function getUser(userId) {
    return request({
        url: '/admin/user/' + userId,
        method: 'get'
    })
}

export function addUser(data) {
    return request({
        url: '/admin/user',
        method: 'post',
        data: data
    })
}

export function updateUser(data) {
    return request({
        url: '/admin/user',
        method: 'put',
        data: data
    })
}

export function delUser(userId) {
    return request({
        url: '/admin/user/' + userId,
        method: 'delete'
    })
}
