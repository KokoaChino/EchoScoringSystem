package com.echo.external.service.api;

import com.echo.entity.Weapon;
import com.echo.entity.Character;
import java.util.Map;


public interface GameDataCacheService {
    void refreshCache();
    Map<String, Weapon> getWeapons();
    Map<String, Character> getCharacters();
}