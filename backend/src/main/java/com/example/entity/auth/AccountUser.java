package com.example.entity.auth;

import lombok.Data;


@Data
public class AccountUser { // 用户
    int id;
    String username;
    String email;
}