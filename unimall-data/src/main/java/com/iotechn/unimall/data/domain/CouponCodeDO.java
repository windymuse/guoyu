package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 优惠券领取代码，适用于领取类型为代码领取的优惠券
 * Created by zengwei on 2021/10/12.
 */
@Data
@TableName("unimall_coupon_code")
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeDO extends SuperDO {

    @TableField("coupon_id")
    private Long couponId;

    @TableField("user_id")
    private Long userId;

    /**
     * 优惠代码
     */
    private String code;

    /**
     * 是否已被使用
     */
    private Integer status;

    /**
     * 对该优惠券的说明
     */
    private String description;


    @TableField("gmt_start")
    private Date gmtStart;

    @TableField("gmt_end")
    private Date gmtEnd;

}
