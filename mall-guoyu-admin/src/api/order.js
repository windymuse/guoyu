import request from '@/utils/request'

export function listOrder(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.order',
            _mt: 'list',
            ...query
        }
    })
}

export function detailOrder(orderId) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.order',
            _mt: 'detail',
            orderId
        }
    })
}

export function shipOrder(shipForm) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.order',
            _mt: 'ship',
            ...shipForm
        }
    })
}

export function takeOrder(takeForm) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.order',
            _mt: 'take',
            ...takeForm
        }
    })
}

export function refundOrder(refundForm) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.order',
            _mt: 'refund',
            ...refundForm
        }
    })
}

export function getExcelInfo(data) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.order',
            _mt: 'queryToExcel',
            ...data
        }
    })
}