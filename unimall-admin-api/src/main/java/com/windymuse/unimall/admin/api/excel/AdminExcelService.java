package com.windymuse.unimall.admin.api.excel;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.CouponDO;
import com.windymuse.unimall.data.dto.CouponAdminDTO;
import com.windymuse.unimall.data.model.Page;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-12
 * Time: 下午10:47
 */

@HttpOpenApi(group = "admin.excel", description = "excel工作表")
public interface AdminExcelService {

    @HttpMethod(description = "销售确认单", permission = "operation:excel:confirmation:create", permissionParentName = "创建", permissionName = "销售确认单")
    public Map<String, Object> createSaleConfirmation(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "orderId", type = HttpParamType.COMMON, description = "订单id") Long orderId) throws ServiceException, IOException;


    @HttpMethod(description = "日销售汇总", permission = "operation:excel:daySales:create", permissionParentName = "创建", permissionName = "日销售汇总表")
    public Map<String, Object> createDaySales(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException, IOException;


    @HttpMethod(description = "日销售表", permission = "operation:excel:daySales:create", permissionParentName = "创建", permissionName = "单日销售表")
    public Map<String, Object> createSomeDaySales(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "startDay", type = HttpParamType.COMMON, description = "起始日期") String startDay,
            @NotNull @HttpParam(name = "endDay", type = HttpParamType.COMMON, description = "结束日期") String endDay) throws ServiceException, IOException;


    @HttpMethod(description = "商品流水", permission = "operation:excel:daySales:create", permissionParentName = "创建", permissionName = "销售流水")
    public Map<String, Object> createNewSomeDaySales(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "startDay", type = HttpParamType.COMMON, description = "起始日期") String startDay,
            @NotNull @HttpParam(name = "endDay", type = HttpParamType.COMMON, description = "结束日期") String endDay) throws ServiceException, IOException;

    @HttpMethod(description = "商品日汇总表", permission = "operation:excel:daySales:create", permissionParentName = "创建", permissionName = "商品日汇总表")
    public Map<String, Object> createNewGoodsSales(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "startDay", type = HttpParamType.COMMON, description = "起始日期") String startDay,
            @NotNull @HttpParam(name = "endDay", type = HttpParamType.COMMON, description = "结束日期") String endDay) throws ServiceException, IOException;

}
