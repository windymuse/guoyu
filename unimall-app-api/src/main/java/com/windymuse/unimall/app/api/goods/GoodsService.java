package com.windymuse.unimall.app.api.goods;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.goods.SpuDTO;
import com.windymuse.unimall.data.model.Page;

/**
 * Created by rize on 2019/7/1.
 */
@HttpOpenApi(group = "goods", description = "商品服务")
public interface GoodsService {

    // 注解
    @HttpMethod(description = "搜索Goods列表")
    public Page<SpuDTO> getGoodsPage(
            @HttpParam(name = "pageNo", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer pageNo,
            @HttpParam(name = "pageSize", type = HttpParamType.COMMON, description = "页码长度", valueDef = "10") Integer pageSize,
            @HttpParam(name = "categoryId", type = HttpParamType.COMMON, description = "搜索分类") Long categoryId,
            @HttpParam(name = "orderBy", type = HttpParamType.COMMON, description = "排序 id 或 sales", valueDef = "id") String orderBy,
            @HttpParam(name = "isAsc", type = HttpParamType.COMMON, description = "是否升序", valueDef = "false") Boolean isAsc,
            @HttpParam(name = "title", type = HttpParamType.COMMON, description = "搜索标题") String title) throws ServiceException;

    @HttpMethod(description = "获取商品详情")
    public SpuDTO getGoods(
            @NotNull @HttpParam(name = "spuId", type = HttpParamType.COMMON, description = "商品Id") Long spuId,
            @HttpParam(name = "groupShopId", type = HttpParamType.COMMON, description = "团购Id") Long groupShopId,
            @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;

    @HttpMethod(description = "获取页面二维码")
    public Object getQRCode(
            @NotNull @HttpParam(name = "spuId", type = HttpParamType.COMMON, description = "商品Id") Long spuId,
            @HttpParam(name = "groupShopId", type = HttpParamType.COMMON, description = "团购Id") Long groupShopId,
            @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;

}
