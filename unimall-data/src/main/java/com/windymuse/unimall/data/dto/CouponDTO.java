package com.windymuse.unimall.data.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.Date;

/**
 * Created by rize on 2019/7/4.
 */
@Data
public class CouponDTO extends SuperDTO {

    private String title;

    private Integer type;

    private String description;

    private Integer total;

    private Integer surplus;

    private Integer limit;

    private Integer discount;

    private Integer min;

    private Integer status;

    private Long categoryId;

    private String categoryTitle;

    private Integer days;

    /**
     * 用户现在有的数量
     */
    private Integer nowCount;

    private Date gmtStart;

    private Date gmtEnd;

    /**
     * 领取类型：1 直接领取 2 代码领取
     */
    private Integer gmtType;

}
