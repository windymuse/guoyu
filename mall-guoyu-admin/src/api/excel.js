import request from '@/utils/request'

export function createSaleConfirmation(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.excel',
            _mt: 'createSaleConfirmation',
            ...query
        }
    })
}

export function createDaySales(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.excel',
            _mt: 'createDaySales',
            ...query
        }
    })
}

export function createSomeDaySales(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.excel',
            _mt: 'createSomeDaySales',
            ...query
        }
    })
}

export function createNewSomeDaySales(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.excel',
            _mt: 'createNewSomeDaySales',
            ...query
        }
    })
}

export function createNewGoodsSales(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.excel',
            _mt: 'createNewGoodsSales',
            ...query
        }
    })
}