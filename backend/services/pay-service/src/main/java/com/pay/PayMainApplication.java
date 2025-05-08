/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PayMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMainApplication.class, args);
    }
}