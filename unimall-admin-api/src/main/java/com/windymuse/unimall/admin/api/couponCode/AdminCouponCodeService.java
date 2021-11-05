package com.windymuse.unimall.admin.api.couponCode;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.CouponCodeDO;
import com.windymuse.unimall.data.model.Page;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-12
 * Time: 下午10:47
 */

@HttpOpenApi(group = "admin.couponCode", description = "兑换卷兑换码")
public interface AdminCouponCodeService {

    @HttpMethod(description = "创建", permission = "promote:couponCode:create", permissionParentName = "兑换码管理", permissionName = "兑换管理")
    public CouponCodeDO addCouponCode(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "couponId", type = HttpParamType.COMMON, description = "兑换券id") Long couponId,
            @NotNull @HttpParam(name = "code", type = HttpParamType.COMMON, description = "兑换码") String code,
            @NotNull @HttpParam(name = "status", type = HttpParamType.COMMON, description = "兑换码状态") Integer status,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "兑换码描述") String description,
            @HttpParam(name = "gmtStart", type = HttpParamType.COMMON, description = "兑换开始时间") Long gmtStart,
            @HttpParam(name = "gmtEnd", type = HttpParamType.COMMON, description = "兑换结束时间") Long gmtEnd) throws ServiceException;

    @HttpMethod(description = "删除", permission = "promote:couponCode:delete", permissionParentName = "兑换码管理", permissionName = "兑换管理")
    public Boolean deleteCouponCode(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "兑换卷ID") Long id) throws ServiceException;

    @HttpMethod(description = "修改", permission = "promote:couponCode:update", permissionParentName = "兑换码管理", permissionName = "兑换管理")
    public Boolean updateCouponCode(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "兑换码ID") Long id,
            @NotNull @HttpParam(name = "couponId", type = HttpParamType.ADMIN_ID, description = "优惠券id") Long couponId,
            @HttpParam(name = "userId", type = HttpParamType.COMMON, description = "用户id") Long userId,
            @NotNull @HttpParam(name = "code", type = HttpParamType.COMMON, description = "兑换码") String code,
            @NotNull @HttpParam(name = "status", type = HttpParamType.COMMON, description = "兑换码状态") Integer status,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "兑换码描述") String description,
            @HttpParam(name = "gmtStart", type = HttpParamType.COMMON, description = "兑换开始时间") Date gmtStart,
            @HttpParam(name = "gmtEnd", type = HttpParamType.COMMON, description = "兑换结束时间") Date gmtEnd) throws ServiceException;


    @HttpMethod(description = "查询", permission = "promote:couponCode:query", permissionParentName = "兑换码管理", permissionName = "兑换管理")
    public Page<CouponCodeDO> queryCouponCode(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @HttpParam(name = "couponId", type = HttpParamType.COMMON, description = "优惠券id") Long couponId,
            @HttpParam(name = "userId", type = HttpParamType.COMMON, description = "用户id") Long userId,
            @HttpParam(name = "status", type = HttpParamType.COMMON, description = "兑换卷状态") Integer status,
            @HttpParam(name = "pageNo", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer pageNo,
            @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度", valueDef = "10") Integer limit) throws ServiceException;

}
