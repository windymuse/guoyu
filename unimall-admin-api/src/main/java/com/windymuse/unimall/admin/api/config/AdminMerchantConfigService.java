package com.windymuse.unimall.admin.api.config;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.annotation.param.Range;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.CategoryDO;
import com.windymuse.unimall.data.domain.ConfigDO;
import com.windymuse.unimall.data.dto.CategoryTreeNodeDTO;
import com.windymuse.unimall.data.dto.ConfigDTO;
import com.windymuse.unimall.data.model.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-20
 * Time: 上午10:18
 */

@HttpOpenApi(group = "admin.merchant", description = "商铺信息配置")
public interface AdminMerchantConfigService {

    @HttpMethod(description = "创建", permission = "promote:merchant:create", permissionParentName = "推广管理", permissionName = "商铺信息管理")
    public boolean addMerchant(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "title", type = HttpParamType.COMMON, description = "商铺标题") String title,
            @HttpParam(name = "logoUrl", type = HttpParamType.COMMON, description = "商铺logo") String logoUrl,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "商铺描述") String description,
            @HttpParam(name = "address", type = HttpParamType.COMMON, description = "商铺地址") String address,
            @HttpParam(name = "longitude", type = HttpParamType.COMMON, description = "经度") String longitude,
            @HttpParam(name = "latitude", type = HttpParamType.COMMON, description = "维度") String latitude,
            @HttpParam(name = "slogan", type = HttpParamType.COMMON, description = "标语") String slogan,
            @NotNull @HttpParam(name = "showType", type = HttpParamType.COMMON, description = "展示方式") Integer showType,
            @HttpParam(name = "startTime", type = HttpParamType.COMMON, description = "开始营业时间") String startTime,
            @HttpParam(name = "endTime", type = HttpParamType.COMMON, description = "结束营业时间") String endTime
    ) throws ServiceException;

    @HttpMethod(description = "修改", permission = "promote:merchant:update", permissionParentName = "推广管理", permissionName = "商铺信息管理")
    public boolean updateMerchant(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "title", type = HttpParamType.COMMON, description = "商铺标题") String title,
            @HttpParam(name = "logoUrl", type = HttpParamType.COMMON, description = "商铺logo") String logoUrl,
            @HttpParam(name = "description", type = HttpParamType.COMMON, description = "商铺描述") String description,
            @HttpParam(name = "address", type = HttpParamType.COMMON, description = "商铺地址") String address,
            @HttpParam(name = "longitude", type = HttpParamType.COMMON, description = "经度") String longitude,
            @HttpParam(name = "latitude", type = HttpParamType.COMMON, description = "维度") String latitude,
            @HttpParam(name = "slogan", type = HttpParamType.COMMON, description = "标语") String slogan,
            @NotNull @HttpParam(name = "showType", type = HttpParamType.COMMON, description = "展示方式") Integer showType,
            @HttpParam(name = "startTime", type = HttpParamType.COMMON, description = "开始营业时间") String startTime,
            @HttpParam(name = "endTime", type = HttpParamType.COMMON, description = "结束营业时间") String endTime
    ) throws ServiceException;

    @HttpMethod(description = "查询", permission = "promote:merchant:query", permissionParentName = "推广管理", permissionName = "商铺信息管理")
    public ConfigDTO getMerchant(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;


}
