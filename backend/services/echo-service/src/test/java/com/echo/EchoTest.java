package com.echo;

import com.echo.entity.Character;
import com.echo.entity.Weapon;
import com.echo.external.service.api.GameDataCacheService;
import com.echo.external.service.api.GameDataService;
import com.echo.external.service.api.KujiequApiService;
import com.echo.util.EchoUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Map;


@SpringBootTest
public class EchoTest {

    @Resource
    GameDataService gameDataService;

    @Resource
    KujiequApiService kujiequApiService;

    @Resource
    GameDataCacheService gameDataCacheService;

    @Resource
    EchoUtil echoUtil;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void test() {
        Map<String, Weapon> weapons = echoUtil.getWeapons();
        System.out.println(weapons);
        Map<String, Character> characters = echoUtil.getCharacters();
        System.out.println(characters);
    }
}
