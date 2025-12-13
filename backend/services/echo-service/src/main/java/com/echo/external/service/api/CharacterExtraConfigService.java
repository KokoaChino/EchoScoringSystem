package com.echo.external.service.api;

import java.util.Map;
import com.echo.mapper.persistence.CharacterExtraConfigDO;


public interface CharacterExtraConfigService {
    void reload();
    CharacterExtraConfigDO getConfig(String characterName);
    Map<String, CharacterExtraConfigDO> getAllConfigs();
}