package com.windymuse.unimall.app.api.config;

import com.windymuse.unimall.core.annotation.HttpMethod;
import com.windymuse.unimall.core.annotation.HttpOpenApi;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.ConfigDTO;

/**
 * Created by rize on 2019/7/21.
 */
@HttpOpenApi(group = "config", description = "商户配置服务")
public interface ConfigService {

    @HttpMethod(description = "获取商户配置")
    public ConfigDTO getMerchantConfig() throws ServiceException;

}
