package com.echo.mapper.persistence;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterConfigDO {
    private String name;
    private String weapon;
    private String weight;
}