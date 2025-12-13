package com.echo.external.service.impl;

import com.echo.event.ConfigReloadedEvent;
import com.echo.external.service.api.CharacterExtraConfigService;
import com.echo.mapper.CharacterExtraConfigMapper;
import com.echo.mapper.persistence.CharacterExtraConfigDO;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.annotation.Resource;


@Service
@Slf4j
public class CharacterExtraConfigServiceImpl implements CharacterExtraConfigService {

    @Resource
    private CharacterExtraConfigMapper configMapper;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    private final LoadingCache<String, CharacterExtraConfigDO> cache = Caffeine.newBuilder()
            .maximumSize(200)
            .build(name -> configMapper.selectByName(name));

    private final AtomicReference<Map<String, CharacterExtraConfigDO>> allConfigsRef =
            new AtomicReference<>(Collections.emptyMap());

    @PostConstruct
    public void init() {
        reload();
    }

    @Override
    public void reload() {
        try {
            List<CharacterExtraConfigDO> configs = configMapper.selectAllEnabled();
            Map<String, CharacterExtraConfigDO> newMap = configs.stream()
                    .collect(Collectors.toMap(CharacterExtraConfigDO::getName, Function.identity()));
            allConfigsRef.set(newMap);
            cache.invalidateAll();
            eventPublisher.publishEvent(new ConfigReloadedEvent(this));
            log.info("角色配置缓存已刷新：{}", getAllConfigs());
        } catch (Exception e) {
            log.error("刷新配置失败", e);
        }
    }

    @Override
    public CharacterExtraConfigDO getConfig(String characterName) {
        return cache.get(characterName);
    }

    @Override
    public Map<String, CharacterExtraConfigDO> getAllConfigs() {
        return allConfigsRef.get();
    }
}