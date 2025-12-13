package com.echo.mapper.persistence;

import lombok.Data;
import com.alibaba.fastjson.JSON;
import com.echo.external.dto.CharacterExtraConfigDTO;
import org.jsoup.internal.StringUtil;
import java.time.LocalDateTime;


@Data
public class CharacterExtraConfigDO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 权重列表
     */
    private String weightJson;

    /**
     * 立绘缩放比例，单位 %
     */
    private Short scaleRatio;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public CharacterExtraConfigDTO toCharacterExtraConfigDTO() {
        CharacterExtraConfigDTO res = new CharacterExtraConfigDTO();
        CharacterExtraConfigDTO defaultConfig = CharacterExtraConfigDTO.DEFAULT_CONFIG;
        res.setId(this.getId() != null ? this.getId() : defaultConfig.id);
        res.setName(StringUtil.isBlank(this.getName()) ? defaultConfig.name : this.getName());
        res.setPinyin(StringUtil.isBlank(this.getPinyin()) ? defaultConfig.pinyin : this.getPinyin());
        res.setWeight(StringUtil.isBlank(this.getWeightJson()) ?
                defaultConfig.weight : JSON.parseObject(this.getWeightJson(), int[].class));
        res.setScaleRatio(this.getScaleRatio() != null ?
                this.getScaleRatio() + "%" : defaultConfig.scaleRatio);
        res.setAvatarUrl(StringUtil.isBlank(this.getAvatarUrl()) ? defaultConfig.avatarUrl : this.getAvatarUrl());
        return res;
    }
}