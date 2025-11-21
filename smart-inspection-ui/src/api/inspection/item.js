import request from '@/utils/request'

export function listItem(query) {
    return request({
        url: '/admin/config/item/list',
        method: 'get',
        params: query
    })
}

export function getItem(id) {
    return request({
        url: '/admin/config/item/' + id,
        method: 'get'
    })
}

export function addItem(data) {
    return request({
        url: '/admin/config/item',
        method: 'post',
        data: data
    })
}

export function updateItem(data) {
    return request({
        url: '/admin/config/item',
        method: 'put',
        data: data
    })
}

export function delItem(ids) {
    return request({
        url: '/admin/config/item/' + ids,
        method: 'delete'
    })
}
