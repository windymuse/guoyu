package com.windymuse.unimall.app.api.config;

import com.windymuse.unimall.biz.service.config.ConfigBizService;
import com.windymuse.unimall.core.exception.ServiceException;
import com.windymuse.unimall.data.dto.ConfigDTO;
import com.windymuse.unimall.data.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rize on 2019/7/21.
 */
@Service
public class ConfigServiceImpl implements ConfigService{

    @Autowired
    private ConfigBizService configBizService;

    @Override
    public ConfigDTO getMerchantConfig() throws ServiceException {
        return configBizService.getMerchantConfig();
    }
}
