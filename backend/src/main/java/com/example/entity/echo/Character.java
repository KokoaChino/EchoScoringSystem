package com.example.entity.echo;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Character { // 角色
    String name;
    String pinyin;
    String type;
    Weapon weapon;
    int LV;
    int[] stats;
    int[] weight;
}