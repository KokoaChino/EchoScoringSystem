package com.echo.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character { // 角色
    private String id;
    private String name;
    private String type;
    private String pinyin;
    private Integer star;
    private int[] stats;
    private int[] weight;
    private Weapon weapon;
    private String scaleRatio;
    private String squadFigureUrl;
    private String avatarUrl;
}