package com.windymuse.unimall.app.api.address;

/*
@author kbq
@date  2019/7/4 - 21:36
*/

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.annotation.HttpParam;
import com.windymuse.unimall.core.annotation.HttpParamType;
import com.windymuse.unimall.core.annotation.param.NotNull;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.domain.AddressDO;

import java.rmi.ServerException;
import java.util.List;

@HttpOpenApi(group = "address", description = "收货地址")
public interface AddressService {

    @HttpMethod(description = "增加收货地址")
    public Boolean addAddress(
            @NotNull @HttpParam(name = "province", type = HttpParamType.COMMON, description = "省份") String province,
            @NotNull @HttpParam(name = "city", type = HttpParamType.COMMON, description = "城市") String city,
            @NotNull @HttpParam(name = "county", type = HttpParamType.COMMON, description = "区县") String county,
            @NotNull @HttpParam(name = "address", type = HttpParamType.COMMON, description = "详细地址") String address,
            @NotNull @HttpParam(name = "longitude", type = HttpParamType.COMMON, description = "经度") Double longitude,
            @NotNull @HttpParam(name = "latitude", type = HttpParamType.COMMON, description = "维度") Double latitude,
            @NotNull @HttpParam(name = "defaultAddress", type = HttpParamType.COMMON, description = "默认地址") Integer defaultAddress,
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户ID") Long userId,
            @NotNull @HttpParam(name = "phone", type = HttpParamType.COMMON, description = "电话") String phone,
            @NotNull @HttpParam(name = "consignee", type = HttpParamType.COMMON, description = "收件人") String consignee) throws ServiceException;


    @HttpMethod(description = "删除收货地址")
    public Boolean deleteAddress(
            @NotNull @HttpParam(name = "addressId", type = HttpParamType.COMMON, description = "收货地址ID") Long addressId,
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户ID") Long userId) throws ServiceException;


    @HttpMethod(description = "修改收货地址")
    public Boolean updateAddress(
            @NotNull @HttpParam(name = "addressId", type = HttpParamType.COMMON, description = "地址Id") Long addressId,
            @NotNull @HttpParam(name = "province", type = HttpParamType.COMMON, description = "省份") String province,
            @NotNull @HttpParam(name = "city", type = HttpParamType.COMMON, description = "城市") String city,
            @NotNull @HttpParam(name = "county", type = HttpParamType.COMMON, description = "区县") String county,
            @NotNull @HttpParam(name = "address", type = HttpParamType.COMMON, description = "详细地址") String address,
            @NotNull @HttpParam(name = "longitude", type = HttpParamType.COMMON, description = "经度") Double longitude,
            @NotNull @HttpParam(name = "latitude", type = HttpParamType.COMMON, description = "维度") Double latitude,
            @NotNull @HttpParam(name = "defaultAddress", type = HttpParamType.COMMON, description = "默认地址") Integer defaultAddress,
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户ID") Long userId,
            @NotNull @HttpParam(name = "phone", type = HttpParamType.COMMON, description = "电话") String phone,
            @NotNull @HttpParam(name = "consignee", type = HttpParamType.COMMON, description = "收件人") String consignee) throws ServiceException;


    @HttpMethod(description = "查询用户所有收货地址")
    public List<AddressDO> getAllAddress(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId) throws ServiceException;


    @HttpMethod(description = "根据地址ID，查询收货地址")
    public AddressDO getAddressById(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户Id") Long userId,
            @NotNull @HttpParam(name = "addressId", type = HttpParamType.COMMON, description = "地址Id") Long addressId) throws ServiceException;


    @HttpMethod(description = "获取用户默认地址")
    public AddressDO getDefAddress(
            @NotNull @HttpParam(name = "userId", type = HttpParamType.USER_ID, description = "用户id") Long userId) throws ServiceException;
}
