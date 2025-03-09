package com.example.config.autoconfig;

import com.alipay.easysdk.kernel.Config;
import com.example.config.properties.AliPayProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
public class AliPayConfiguration {

    @Bean
    public Config config(AliPayProperties payProperties) {
        Config config = new Config();
        config.protocol = payProperties.getProtocol();
        config.gatewayHost = payProperties.getGatewayHost();
        config.signType = payProperties.getSignType();
        config.appId = payProperties.getAppId();
        config.merchantPrivateKey = payProperties.getMerchantPrivateKey();
        config.alipayPublicKey = payProperties.getAlipayPublicKey();
        config.notifyUrl = payProperties.getNotifyUrl();
        config.encryptKey = "";
        return config;
    }
}