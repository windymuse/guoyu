package com.windymuse.unimall.data.dto.goods;

import com.windymuse.unimall.data.domain.SkuDO;
import com.windymuse.unimall.data.domain.SpuAttributeDO;
import com.windymuse.unimall.data.dto.CategoryDTO;
import com.windymuse.unimall.data.dto.SuperDTO;
import com.windymuse.unimall.data.dto.appraise.AppraiseResponseDTO;
import com.windymuse.unimall.data.dto.freight.FreightTemplateDTO;
import com.windymuse.unimall.data.model.Page;
import lombok.Data;

import java.util.List;

/**
 * Created by rize on 2019/7/2.
 */
@Data
public class SpuDTO extends SuperDTO {

    private List<SkuDO> skuList;

    private Integer originalPrice;

    private Integer price;

    private Integer vipPrice;

    private Integer stock;

    private Integer sales;

    private String title;

    /**
     * 主图
     */
    private String img;

    /**
     * 后面的图，仅在详情接口才出现
     */
    private List<String> imgList;

    private String detail;

    private String description;

    private Long categoryId;

    private List<Long> categoryIds;

    private List<CategoryDTO> categoryList;

    private List<SpuAttributeDO> attributeList;

    /**
     * 商品的第一页(前10条)评价
     */
    private Page<AppraiseResponseDTO> appraisePage;

    /**
     * 商品现在携带的团购信息
     */
    private GroupShopDTO groupShop;

    private String unit;

    private Long freightTemplateId;

    private FreightTemplateDTO freightTemplate;

    private Boolean collect;

    private Integer status;

    /**
     * 配送限制
     * 0 无限制
     * 1 不得出城，只在同城配送
     */
    private Integer deliverLimit;

}
