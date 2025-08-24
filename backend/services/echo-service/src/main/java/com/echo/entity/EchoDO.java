package com.echo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EchoDO {
    String username;
    String name;
    String echo;
}