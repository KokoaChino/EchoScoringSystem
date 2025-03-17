package com.echo.entity;

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
