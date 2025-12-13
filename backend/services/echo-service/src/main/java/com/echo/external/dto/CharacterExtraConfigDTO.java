package com.echo.external.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterExtraConfigDTO { // 额外配置 DTO

    public Long id;
    public String name;
    public String pinyin;
    public int[] weight;
    public String scaleRatio;
    public String avatarUrl;

    public static final CharacterExtraConfigDTO DEFAULT_CONFIG;
    static {
        DEFAULT_CONFIG = new CharacterExtraConfigDTO();
        DEFAULT_CONFIG.setId(0L);
        DEFAULT_CONFIG.setName("");
        DEFAULT_CONFIG.setPinyin("");
        DEFAULT_CONFIG.setWeight(new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100});
        DEFAULT_CONFIG.setScaleRatio("80%");
        DEFAULT_CONFIG.setAvatarUrl("");
    }
}