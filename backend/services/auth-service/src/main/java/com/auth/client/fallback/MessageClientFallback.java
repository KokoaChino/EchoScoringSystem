package com.auth.client.fallback;

import com.auth.client.feign.MessageClient;
import org.springframework.stereotype.Component;
import java.util.Map;


@Component
public class MessageClientFallback implements MessageClient {

    @Override
    public String sendCodeEmail(String email, String code) {
        return null;
    }

    @Override
    public void sendMqMessage(String exchange, String key, Map<String, String> msg) {

    }
}