package com.windymuse.unimall.data.dto.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.windymuse.unimall.data.domain.OrderSkuDO;
import com.windymuse.unimall.data.dto.SuperDTO;
import com.windymuse.unimall.data.dto.freight.ShipTraceDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by rize on 2019/7/6.
 */
@Data
public class OrderGoodsSumDTO extends SuperDTO {

    @TableField("sku_id")
    private Long skuId;

    /**
     * 冗余SPUID方便评论
     */
    @TableField("spu_id")
    private Long spuId;

    @TableField("order_id")
    private Long orderId;

    /**
     * 冗余，方便技术查库
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * SPU 标题
     */
    @TableField("spu_title")
    private String spuTitle;

    /**
     * SKU 标题， 即小规格名称
     */
    private String title;

    @TableField("bar_code")
    private String barCode;

    private Integer num;

    @TableField("original_price")
    private Integer originalPrice;

    /**
     * 单价
     */
    private Integer price;

    /**
     * SKU 或 SPU主图 （优先使用SKU图）
     */
    private String img;

    //单位
    private String unit;

    @TableField("avg_price")
    private Integer avgPrice;

    @TableField("total_num")
    private Double totalNum;

    @TableField("total_price")
    private Integer totalPrice;


}
