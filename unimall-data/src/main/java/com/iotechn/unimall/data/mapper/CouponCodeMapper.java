package com.iotechn.unimall.data.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iotechn.unimall.data.domain.CouponCodeDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by zengwei on 2021/10/12.
 */
public interface CouponCodeMapper extends BaseMapper<CouponCodeDO> {
    public List<CouponCodeDO> getCouponCodeList(@Param("couponId")Long couponId, @Param("userId") Long userId, @Param("status")Integer status, @Param("offset") Integer offset,@Param("limit") Integer limit);

}
