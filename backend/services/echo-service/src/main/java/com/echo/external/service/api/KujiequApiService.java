package com.echo.external.service.api;

import com.echo.external.dto.CharacterDetailDTO;
import com.echo.external.dto.WeaponDetailDTO;
import java.util.List;


public interface KujiequApiService { // 库街区 API 接口

    List<WeaponDetailDTO> getWeaponList(); // 获取武器列表
    WeaponDetailDTO enrichWeaponDetail(WeaponDetailDTO var); // 补充武器详情

    List<CharacterDetailDTO> getCharacterList(); // 获取角色列表
    CharacterDetailDTO enrichCharacterDetail1(CharacterDetailDTO var); // 补充角色属性，立绘和三维等
    List<CharacterDetailDTO> enrichCharacterIds(List<CharacterDetailDTO> var); // 补充每个角色的 ID
    CharacterDetailDTO enrichCharacterDetail2(CharacterDetailDTO var); // 补充角色头像和专武
}