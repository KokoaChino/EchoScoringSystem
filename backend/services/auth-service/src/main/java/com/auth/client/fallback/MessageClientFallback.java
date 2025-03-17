package com.auth.client.fallback;

import com.auth.client.feign.MessageClient;
import org.springframework.stereotype.Component;


@Component
public class MessageClientFallback implements MessageClient {

    @Override
    public String sendCodeEmail(String email, String code) {
        return null;
    }
}