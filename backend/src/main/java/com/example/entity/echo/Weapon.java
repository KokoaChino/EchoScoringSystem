package com.example.entity.echo;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Weapon { // 武器
    String name;
    String type;
    int LV;
    int ATH;
}
