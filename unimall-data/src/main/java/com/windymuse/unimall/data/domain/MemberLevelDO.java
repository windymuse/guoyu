package com.windymuse.unimall.data.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * Created by zengwei on 2021/10/8.
 */
@TableName("unimall_member_level")
@Data
public class MemberLevelDO extends SuperDO {

    /**
     * 会员等级描述
     */
    private String name;

    /**
     * 会员等级
     */
    private Integer degree;

    /**
     * 商品通用折扣 100为原价
     */
    private Integer percent;

    /**
     * 消费额度，到此额度后自动升级
     */
    private Integer money;

    /**
     * 会员等级图片
     */
    private String img;

    /**
     * 会员等级描述
     */
    private String description;

}
