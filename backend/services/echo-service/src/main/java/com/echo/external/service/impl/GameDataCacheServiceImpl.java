package com.echo.external.service.impl;

import com.echo.entity.Character;
import com.echo.entity.Weapon;
import com.echo.event.GameDataRefreshEvent;
import com.echo.external.service.api.GameDataCacheService;
import com.echo.external.service.api.GameDataService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class GameDataCacheServiceImpl implements GameDataCacheService {

    @Data
    @AllArgsConstructor
    private static class GameDataSnapshot {
        private final Map<String, Weapon> weapons;
        private final Map<String, Character> characters;
    }

    private static final AtomicReference<GameDataSnapshot> SNAPSHOT =
            new AtomicReference<>(new GameDataSnapshot(Map.of(), Map.of()));

    @Resource
    private GameDataService gameDataService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        refreshCache();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledRefresh() {
        refreshCache();
    }

    @Override
    public void refreshCache() {
        log.info("开始刷新游戏数据缓存...");
        Map<String, Weapon> newWeapons = gameDataService.fetchAllWeapons(
                getWeapons().size()
        );
        log.info("抓取到的武器数据：{}", newWeapons);
        Map<String, Character> newCharacters = gameDataService.fetchAllCharacters(
                getCharacters().size()
        );
        log.info("抓取到的角色数据：{}", newCharacters);
        if (newWeapons == null && newCharacters == null) {
            return;
        }
        if (newWeapons == null || newWeapons.isEmpty()) {
            newWeapons = getWeapons();
        }
        if (newCharacters == null || newCharacters.isEmpty()) {
            newCharacters = getCharacters();
        }
        SNAPSHOT.set(new GameDataSnapshot(
                Map.copyOf(newWeapons), Map.copyOf(newCharacters)
        ));
        eventPublisher.publishEvent(new GameDataRefreshEvent(this));
        log.info("缓存刷新成功！武器：{} 件，角色：{} 名", newWeapons.size(), newCharacters.size());
    }

    @Override
    public Map<String, Weapon> getWeapons() {
        return SNAPSHOT.get().getWeapons();
    }

    @Override
    public Map<String, Character> getCharacters() {
        return SNAPSHOT.get().getCharacters();
    }
}