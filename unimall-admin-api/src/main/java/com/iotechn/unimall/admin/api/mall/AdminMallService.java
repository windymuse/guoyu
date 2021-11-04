package com.iotechn.unimall.admin.api.mall;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.annotation.param.Range;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.MallDO;
import com.iotechn.unimall.data.model.Page;

/**
 * Description:
 * User: Zengwei
 * Date: 2021-10-07
 * Time: 下午7:30
 */

@HttpOpenApi(group = "admin.mall", description = "店铺信息管理")
public interface AdminMallService {

    @HttpMethod(description = "创建", permission = "system:mall:create", permissionParentName = "店铺信息管理", permissionName = "会员等级管理")
    public Boolean addMall(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "memberLevel", type = HttpParamType.COMMON, description = "用户信息") MallDO Mall) throws ServiceException;

    @HttpMethod(description = "删除", permission = "system:mall:delete", permissionParentName = "店铺信息管理", permissionName = "会员等级管理")
    public Boolean deleteMall(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "用户Id") Long id,
            @NotNull @HttpParam(name = "name", type = HttpParamType.COMMON, description = "用户等级称号") String name) throws ServiceException;

    @HttpMethod(description = "修改", permission = "system:mall:update", permissionParentName = "店铺信息管理", permissionName = "会员等级管理")
    public Boolean updateMall(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "memberLevel", type = HttpParamType.COMMON, description = "用户信息") MallDO Mall) throws ServiceException;

    @HttpMethod(description = "查询", permission = "system:mall:query", permissionParentName = "店铺信息管理", permissionName = "会员等级管理")
    public Page<MallDO> getMall(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @HttpParam(name = "id", type = HttpParamType.COMMON, description = "用户ID") Long id,
            @HttpParam(name = "title", type = HttpParamType.COMMON, description = "店铺名称") String title,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "店铺描述") String description,
            @HttpParam(name = "showType", type = HttpParamType.COMMON, description = "展示类型") Integer showType,
            @Range(min = 1) @HttpParam(name = "pageNo", type = HttpParamType.COMMON, description = "当前页码") Integer pageNo,
            @Range(min = 1) @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度") Integer limit) throws ServiceException;
}
