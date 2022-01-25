import request from '@/utils/request'

export function createOrder(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.delivery',
            _mt: 'createOrder',
            ...query
        }
    })
}

export function getRoute(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.delivery',
            _mt: 'getRoute',
            ...query
        }
    })
}