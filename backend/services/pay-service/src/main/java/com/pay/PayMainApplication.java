/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.common.client.feign",
        "com.pay.client"
})
@SpringBootApplication
@MapperScan({"com.common.mapper", "com.pay.mapper"})
@ComponentScan(basePackages = {"com.common", "com.pay"})
public class PayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayMainApplication.class, args);
    }
}