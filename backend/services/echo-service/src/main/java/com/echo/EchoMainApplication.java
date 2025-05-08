/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

package com.echo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class EchoMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchoMainApplication.class, args);
    }
}