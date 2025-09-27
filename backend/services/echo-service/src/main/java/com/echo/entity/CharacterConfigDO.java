package com.echo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterConfigDO {
    private String name;
    private String weapon;
    private String weight;
}