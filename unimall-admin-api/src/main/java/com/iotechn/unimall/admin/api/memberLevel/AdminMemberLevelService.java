package com.iotechn.unimall.admin.api.memberLevel;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.annotation.param.Range;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.MemberLevelDO;
import com.iotechn.unimall.data.model.Page;

/**
 * Description:
 * User: Zengwei
 * Date: 2021-10-07
 * Time: 下午7:30
 */

@HttpOpenApi(group = "admin.member_level", description = "会员等级管理")
public interface AdminMemberLevelService {

    @HttpMethod(description = "创建", permission = "system:member_level:create", permissionParentName = "系统管理", permissionName = "会员等级管理")
    public Boolean addMemberLevel(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "memberLevel", type = HttpParamType.COMMON, description = "用户信息") MemberLevelDO MemberLevel) throws ServiceException;

    @HttpMethod(description = "删除", permission = "system:member_level:delete", permissionParentName = "系统管理", permissionName = "会员等级管理")
    public Boolean deleteMemberLevel(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "用户Id") Long id,
            @NotNull @HttpParam(name = "name", type = HttpParamType.COMMON, description = "用户等级称号") String name) throws ServiceException;

    @HttpMethod(description = "修改", permission = "system:member_level:update", permissionParentName = "系统管理", permissionName = "会员等级管理")
    public Boolean updateMemberLevel(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "memberLevel", type = HttpParamType.COMMON, description = "用户信息") MemberLevelDO MemberLevel) throws ServiceException;

    @HttpMethod(description = "查询", permission = "system:member_level:query", permissionParentName = "系统管理", permissionName = "会员等级管理")
    public Page<MemberLevelDO> getMemberLevel(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @HttpParam(name = "id", type = HttpParamType.COMMON, description = "用户ID") Long id,
            @HttpParam(name = "name", type = HttpParamType.COMMON, description = "会员等级称号") String name,
            @HttpParam(name = "degree", type = HttpParamType.COMMON, description = "会员等级") Integer degree,
            @HttpParam(name = "percent", type = HttpParamType.COMMON, description = "会员等级通用折扣") Integer percent,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "会员等级描述") String description,
            @Range(min = 1) @HttpParam(name = "pageNo", type = HttpParamType.COMMON, description = "当前页码") Integer pageNo,
            @Range(min = 1) @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度") Integer limit) throws ServiceException;
}
