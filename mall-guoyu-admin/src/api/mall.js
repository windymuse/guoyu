import request from '@/utils/request'
import Qs from 'qs'

export function listMall(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.mall',
            _mt: 'getMall',
            ...query
        }
    })
}

export function createMall(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.mall',
            _mt: 'addMall',
            memberLevel: JSON.stringify(data)
        })
    })
}

export function updateMall(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.mall',
            _mt: 'updateMall',
            memberLevel: JSON.stringify(data)
        })
    })
}

export function deleteMall(id) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.mall',
            _mt: 'deleteMall',
            memberLevelId: id
        }
    })
}