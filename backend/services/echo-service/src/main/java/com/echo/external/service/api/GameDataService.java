package com.echo.external.service.api;

import com.echo.entity.Weapon;
import com.echo.entity.Character;
import java.util.Map;


public interface GameDataService {
    Map<String, Weapon> fetchAllWeapons(int originalSize);
    Map<String, Character> fetchAllCharacters(int originalSize);
}