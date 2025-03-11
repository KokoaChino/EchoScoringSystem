package com.example.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class Order {
    String id;
    String username;
    LocalDateTime createTime;
    String qrUrl;
}