import request from '@/utils/request'
import Qs from 'qs'

export function listCouponCode(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.couponCode',
            _mt: 'queryCouponCode',
            ...query
        }
    })
}

export function createCouponCode(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.couponCode',
            _mt: 'addCouponCode',
            ...data
        })
    })
}

export function updateCouponCode(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.couponCode',
            _mt: 'updateCouponCode',
            ...data
        })
    })
}

export function activeCouponCode(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.couponCode',
            _mt: 'updateCouponCodeStatus',
            ...data
        })
    })
}

export function deleteCouponCode(data) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.couponCode',
            _mt: 'deleteCouponCode',
            ...data
        }
    })
}