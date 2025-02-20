package com.windymuse.unimall.biz.config.notify;

import com.windymuse.unimall.biz.service.notify.AdminNotifyBizService;
import com.windymuse.unimall.biz.service.notify.UniNotifyAdminNotifyBizServiceImpl;
import com.windymuse.unimall.biz.service.notify.MockAdminNotifyBizServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: rize
 * Date: 2019/12/27
 * Time: 20:15
 */
@Configuration
public class AdminNotifyConfig {

    @Value("${com.windymuse.admin.notify.enable}")
    private String enable;

    @Bean
    public AdminNotifyBizService adminNotifyBizService() {
        if ("mock".equalsIgnoreCase(enable)) {
            return new MockAdminNotifyBizServiceImpl();
        } else if ("uninotify".equalsIgnoreCase(enable)) {
            return new UniNotifyAdminNotifyBizServiceImpl();
        } else {
            return null;
        }
    }

}
