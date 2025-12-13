package com.echo.mapper.persistence;

import lombok.Data;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
public class EchoDO {
    String username;
    String name;
    String echo;
}