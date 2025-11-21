import request from '@/utils/request'

// Level APIs
export function listLevel() {
    return request({
        url: '/admin/config/base/level/list',
        method: 'get'
    })
}

export function addLevel(data) {
    return request({
        url: '/admin/config/base/level',
        method: 'post',
        data: data
    })
}

export function updateLevel(data) {
    return request({
        url: '/admin/config/base/level',
        method: 'put',
        data: data
    })
}

export function delLevel(ids) {
    return request({
        url: '/admin/config/base/level/' + ids,
        method: 'delete'
    })
}

// Category APIs
export function listCategory() {
    return request({
        url: '/admin/config/base/category/list',
        method: 'get'
    })
}

export function addCategory(data) {
    return request({
        url: '/admin/config/base/category',
        method: 'post',
        data: data
    })
}

export function updateCategory(data) {
    return request({
        url: '/admin/config/base/category',
        method: 'put',
        data: data
    })
}

export function delCategory(ids) {
    return request({
        url: '/admin/config/base/category/' + ids,
        method: 'delete'
    })
}
