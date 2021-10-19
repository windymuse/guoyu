import request from '@/utils/request'
import Qs from 'qs'

export function listMemberLevel(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.member_level',
            _mt: 'getMemberLevel',
            ...query
        }
    })
}

export function createMemberLevel(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.member_level',
            _mt: 'addMemberLevel',
            memberLevel: JSON.stringify(data)
        })
    })
}

export function updateMemberLevel(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.member_level',
            _mt: 'updateMemberLevel',
            memberLevel: JSON.stringify(data)
        })
    })
}

export function deleteMemberLevel(id) {
    return request({
        method: 'post',
        params: {
            _gp: 'admin.member_level',
            _mt: 'deleteMemberLevel',
            memberLevelId: id
        }
    })
}

export function getPermission(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.member_level',
            _mt: 'permissionList',
            ...query
        }
    })
}

export function updatePermission(data) {
    return request({
        method: 'post',
        data: Qs.stringify({
            _gp: 'admin.member_level',
            _mt: 'permissionSet',
            memberLevelSetPermissionDTO: JSON.stringify(data)
        })
    })
}

export function memberLevelOptions(query) {
    return request({
        method: 'get',
        params: {
            _gp: 'admin.member_level',
            _mt: 'options'
        }
    })
}