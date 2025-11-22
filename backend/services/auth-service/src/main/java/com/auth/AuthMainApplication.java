/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableFeignClients(basePackages = {
        "com.common.client.feign",
        "com.auth.client.feign"
})
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan({"com.common.mapper", "com.auth.mapper"})
@ComponentScan(basePackages = {"com.common", "com.auth"})
public class AuthMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthMainApplication.class, args);
    }
}