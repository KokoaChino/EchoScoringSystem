package com.echo.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weapon { // 武器
    private String id;
    private String name;
    private String type;
    private Integer star;
    private Integer maxAtk;
    private String imageUrl;
}