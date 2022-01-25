//package com.windymuse.unimall.admin.api.delivery;
//
//import com.windymuse.unimall.core.annotation.HttpMethod;
//import com.windymuse.unimall.core.annotation.HttpOpenApi;
//import com.windymuse.unimall.core.annotation.HttpParam;
//import com.windymuse.unimall.core.annotation.HttpParamType;
//import com.windymuse.unimall.core.annotation.param.NotNull;
//import com.windymuse.unimall.core.exception.ServiceException;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA.
// * Description:
// * User: kbq
// * Date: 2019-07-12
// * Time: 下午10:47
// */
//
//@HttpOpenApi(group = "admin.delivery", description = "快递")
//public interface AdminDeliveryService {
//
//    @HttpMethod(description = "销售确认单", permission = "operation:delivery:create", permissionParentName = "创建", permissionName = "下订单")
//    public Map<String, Object> createOrder(
//            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
//            @NotNull @HttpParam(name = "orderId", type = HttpParamType.COMMON, description = "订单id") Long orderId) throws ServiceException, IOException;
//
//    @HttpMethod(description = "跟踪订单", permission = "operation:delivery:get", permissionParentName = "查询", permissionName = "跟踪订单")
//    public Map<String, Object> getRoute(
//            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
//            @NotNull @HttpParam(name = "orderId", type = HttpParamType.COMMON, description = "订单id") Long orderId) throws ServiceException, IOException;
//
//}
