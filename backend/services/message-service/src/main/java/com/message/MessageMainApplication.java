/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MessageMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageMainApplication.class, args);
    }
}