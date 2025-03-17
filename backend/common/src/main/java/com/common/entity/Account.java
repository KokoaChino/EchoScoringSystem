package com.common.entity;

import lombok.Data;


@Data
public class Account { // 用户
    int id;
    String email;
    String username;
    String password;
    Boolean vip;
}