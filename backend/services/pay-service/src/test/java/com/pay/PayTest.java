package com.pay;

import com.pay.client.feign.MessageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootTest
public class PayTest {

    @Autowired
    MessageClient messageClient;

    @Test
    public void test() {
        Map<String, String> msg = new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", UUID.randomUUID() + "-SUCCESS"),
                        Map.entry("outTradeNo", "outTradeNo"),
                        Map.entry("username", "星开祈灵")
                )
        );
        messageClient.insertMessageIdLog(msg.get("id"), 0);
        messageClient.sendMqMessage("e1", "pay_success", msg);
    }
}
