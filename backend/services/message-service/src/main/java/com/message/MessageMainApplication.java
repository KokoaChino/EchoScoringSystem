/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableFeignClients(basePackages = {
        "com.common.client.feign"
})
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan({"com.common.mapper", "com.message.mapper"})
@ComponentScan(basePackages = {"com.common", "com.message"})
public class MessageMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageMainApplication.class, args);
    }
}