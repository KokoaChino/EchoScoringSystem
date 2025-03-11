package com.example.service.listener;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;


//@Component
//public class RabbitMQListener {
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "q1", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
//            exchange = @Exchange(name = "e1"),
//            key = {"pay_success"}
//    ))
//    public void listenSimpleQueueMessage(String msg) {
//        System.out.println("spring 消费者接收到消息：【" + msg + "】");
//    }
//}