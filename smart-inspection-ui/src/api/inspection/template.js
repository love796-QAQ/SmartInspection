import request from '@/utils/request'

export function listTemplate(query) {
    return request({
        url: '/admin/config/template/list',
        method: 'get',
        params: query
    })
}

export function getTemplate(id) {
    return request({
        url: '/admin/config/template/' + id,
        method: 'get'
    })
}

export function addTemplate(data) {
    return request({
        url: '/admin/config/template',
        method: 'post',
        data: data
    })
}

export function updateTemplate(data) {
    return request({
        url: '/admin/config/template',
        method: 'put',
        data: data
    })
}

export function delTemplate(ids) {
    return request({
        url: '/admin/config/template/' + ids,
        method: 'delete'
    })
}
