package com.echo.external.service.impl;

import com.echo.entity.Character;
import com.echo.entity.Weapon;
import com.echo.external.service.api.GameDataService;
import com.echo.external.service.api.KujiequApiService;
import com.echo.external.dto.CharacterDetailDTO;
import com.echo.external.dto.WeaponDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.util.concurrent.RateLimiter;


@Slf4j
@Service
public class GameDataServiceImpl implements GameDataService {

    private static final int TIME_OUT = 30;
    @SuppressWarnings("UnstableApiUsage")
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(3.0);

    @Resource
    private KujiequApiService kujiequApiService;

    @Override
    public Map<String, Weapon> fetchAllWeapons(int originalSize) {
        long start = System.currentTimeMillis();
        log.info("开始拉取全量武器数据...");
        List<WeaponDetailDTO> weaponList = kujiequApiService.getWeaponList();
        if (weaponList.size() <= originalSize) {
            log.info("没有新增的武器数据：共 {} 件", originalSize);
            return null;
        }
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            log.info("获取武器列表成功，共 {} 件", weaponList.size());
            List<Weapon> weapons = weaponList.stream()
                    .map(dto -> CompletableFuture.supplyAsync(() -> {
                        RATE_LIMITER.acquire();
                        return kujiequApiService.enrichWeaponDetail(dto).toWeapon();
                    }, executor))
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
            return weapons.stream().collect(Collectors.toMap(Weapon::getName, Function.identity()));
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            log.info("全量武器拉取完成，耗时 {} ms", System.currentTimeMillis() - start);
        }
    }

    @Override
    public Map<String, Character> fetchAllCharacters(int originalSize) {
        long start = System.currentTimeMillis();
        log.info("开始拉取全量角色数据...");
        List<CharacterDetailDTO> charList = kujiequApiService.getCharacterList();
        if (charList.size() <= originalSize) {
            log.info("没有新增的角色数据：共 {} 件", originalSize);
            return null;
        }
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            log.info("获取角色列表成功，共 {} 名", charList.size());
            List<CharacterDetailDTO> characterDetailDTOList = charList.stream()
                    .map(dto -> CompletableFuture.supplyAsync(() -> {
                        RATE_LIMITER.acquire();
                        return kujiequApiService.enrichCharacterDetail1(dto);
                    }, executor))
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
            characterDetailDTOList = kujiequApiService.enrichCharacterIds(characterDetailDTOList);
            List<Character> characters = characterDetailDTOList.stream()
                    .map(dto -> CompletableFuture.supplyAsync(() -> {
                        RATE_LIMITER.acquire();
                        return kujiequApiService.enrichCharacterDetail2(dto).toCharacter();
                    }, executor))
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
            return characters.stream().collect(Collectors.toMap(Character::getName, Function.identity()));
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            log.info("全量角色拉取完成，耗时 {} ms", System.currentTimeMillis() - start);
        }
    }
}