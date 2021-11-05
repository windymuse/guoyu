package com.windymuse.unimall.app.api.coupon;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.CouponDO;
import com.windymuse.unimall.data.dto.CouponDTO;
import com.windymuse.unimall.data.dto.UserCouponDTO;

import java.util.List;

/**
 * Created by rize on 2019/7/4.
 */
@HttpOpenApi(group = "coupon", description = "优惠券服务")
public interface CouponService {

    @HttpMethod(description = "领取优惠券")
    public String obtainCoupon(
            @NotNull @HttpParam(name = "couponId", type = HttpParamType.COMMON, description = "优惠券Id") Long couponId,
            @HttpParam(name = "code", type = HttpParamType.COMMON, description = "优惠券兑换码") String code,
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;

    @HttpMethod(description = "获取用户可领取优惠券")
    public List<CouponDTO> getObtainableCoupon(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;

    @HttpMethod(description = "获取用户优惠券")
    public List<UserCouponDTO> getUserCoupons(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;

}
