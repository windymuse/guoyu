package com.windymuse.unimall.app.config.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.windymuse.unimall.wx")
public class WxProperties {

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String keyPath;

}
