package com.echo.util;

import com.common.context.SpringContextHolder;
import com.echo.dto.RoleDTO;
import com.echo.entity.Character;
import com.echo.entity.Echo;
import com.echo.entity.Weapon;
import com.echo.event.ConfigReloadedEvent;
import com.echo.event.GameDataRefreshEvent;
import com.echo.external.dto.CharacterExtraConfigDTO;
import com.echo.external.service.api.CharacterExtraConfigService;
import com.echo.external.service.api.GameDataCacheService;
import com.echo.mapper.persistence.CharacterExtraConfigDO;
import com.echo.service.api.EchoScoringSystemService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Component
@Slf4j
public class EchoUtil { // 声骸工具类

    public static final String[] ECHO_KEYS = new String[]{ // 副词条名称
            "固定攻击", "固定生命", "固定防御", "百分比攻击", "百分比生命", "百分比防御",
            "暴击率", "暴击伤害", "共鸣效率", "普攻伤害加成", "重击伤害加成", "共鸣技能伤害加成", "共鸣解放伤害加成"
    };
    public static final Map<String, double[]> ECHO_VALUES = new HashMap<>( // 副词条取值
            Map.ofEntries(
                    Map.entry("固定攻击", new double[]{60, 50, 40, 30}),
                    Map.entry("固定生命", new double[]{580, 540, 510, 470, 430, 390, 360, 320}),
                    Map.entry("固定防御", new double[]{70, 60, 50, 40}),
                    Map.entry("百分比攻击", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
                    Map.entry("百分比生命", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
                    Map.entry("百分比防御", new double[]{14.7, 13.8, 12.8, 11.8, 10.9, 10.0, 9.0, 8.1}),
                    Map.entry("暴击率", new double[]{10.5, 9.9, 9.3, 8.7, 8.1, 7.5, 6.9, 6.3}),
                    Map.entry("暴击伤害", new double[]{21.0, 19.8, 18.6, 17.4, 16.2, 15.0, 13.8, 12.6}),
                    Map.entry("共鸣效率", new double[]{12.4, 11.6, 10.8, 10.0, 9.2, 8.4, 7.6, 6.8}),
                    Map.entry("普攻伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
                    Map.entry("重击伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
                    Map.entry("共鸣技能伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
                    Map.entry("共鸣解放伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4})
            )
    );
    public static final Map<String, Double> ECHO_AVERAGE = new HashMap<>( // 副词条基准值
            Map.ofEntries(
                    Map.entry("固定攻击", 45.0),
                    Map.entry("固定生命", 450.0),
                    Map.entry("固定防御", 55.0),
                    Map.entry("百分比攻击", 9.0),
                    Map.entry("百分比生命", 9.0),
                    Map.entry("百分比防御", 11.3875),
                    Map.entry("暴击率", 8.4),
                    Map.entry("暴击伤害", 16.8),
                    Map.entry("共鸣效率", 9.6),
                    Map.entry("普攻伤害加成", 9.0),
                    Map.entry("重击伤害加成", 9.0),
                    Map.entry("共鸣技能伤害加成", 9.0),
                    Map.entry("共鸣解放伤害加成", 9.0)
            )
    );
    public static final Map<String, Map<String, Double>> EX_WEIGHTS = Map.ofEntries( // 特别的角色副词条权重
            Map.entry("守岸人", Map.of("暴击率", 15.0))
    );

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        log.info("ApplicationContext 刷新完成，开始首次聚合...");
        refresh();
    }

    @EventListener(classes = {GameDataRefreshEvent.class, ConfigReloadedEvent.class})
    public void onConfigOrDataRefresh(ApplicationEvent event) {
        log.info("收到刷新事件（{}），开始更新游戏数据快照...", event.getClass().getSimpleName());
        refresh();
    }

    @Data
    @AllArgsConstructor
    private static class GameDataSnapshot {
        private final Map<String, Weapon> weapons;
        private final Map<String, Character> characters;
    }

    private static final AtomicReference<GameDataSnapshot> SNAPSHOT =
            new AtomicReference<>(new GameDataSnapshot(Map.of(), Map.of()));

    public void refresh() {
        GameDataCacheService gameDataCacheService =
                SpringContextHolder.getBean(GameDataCacheService.class);
        CharacterExtraConfigService characterExtraConfigService =
                SpringContextHolder.getBean(CharacterExtraConfigService.class);
        if (gameDataCacheService.getWeapons().isEmpty() || gameDataCacheService.getCharacters().isEmpty()) {
            log.warn("游戏数据为空，跳过聚合...");
            return;
        }
        if (characterExtraConfigService.getAllConfigs().isEmpty()) {
            log.warn("配置数据为空，跳过聚合...");
            return;
        }
        Map<String, Weapon> newWeapons = gameDataCacheService.getWeapons();
        Map<String, Character> newCharacters = gameDataCacheService.getCharacters();
        Map<String, CharacterExtraConfigDO> allConfigs = characterExtraConfigService.getAllConfigs();
        for (String name : newCharacters.keySet()) {
            Character character = newCharacters.get(name);
            character.setWeapon(newWeapons.get(
                    character.getWeapon().getName()
            ));
            CharacterExtraConfigDO characterExtraConfigDO = allConfigs.getOrDefault(
                    name, new CharacterExtraConfigDO());
            CharacterExtraConfigDTO configDTO = characterExtraConfigDO.toCharacterExtraConfigDTO();
            character.setPinyin(configDTO.getPinyin());
            character.setWeight(configDTO.getWeight());
            character.setScaleRatio(configDTO.getScaleRatio());
            if (!StringUtil.isBlank(configDTO.getAvatarUrl())) {
                character.setAvatarUrl(configDTO.getAvatarUrl());
            }
        }
        SNAPSHOT.set(new GameDataSnapshot(
                Map.copyOf(newWeapons), Map.copyOf(newCharacters)
        ));
        log.info("完整的武器数据缓存已刷新：{}", getWeapons());
        log.info("完整的角色数据缓存已刷新：{}", getCharacters());
        EchoScoringSystemService echoScoringSystemService =
                SpringContextHolder.getBean(EchoScoringSystemService.class);
        echoScoringSystemService.reData();
        log.info("已重新计算所有声骸数据");
    }

    /**
     * 下面两个方法，返回的 Map 是可变的！
     * 权衡各种方案下，最终还是采用只读的人为约束
     */
    public Map<String, Weapon> getWeapons() {
        return SNAPSHOT.get().getWeapons();
    }

    public Map<String, Character> getCharacters() {
        return SNAPSHOT.get().getCharacters();
    }

    public String getPinyin(String name) { // 获取角色拼音
        return getCharacters().get(name).getPinyin();
    }

    public Map<String, ? extends Number> getDefaultweights(String name, String weapon) { // 获取角色副词条默认权重展开
        Map<String, Character> characters = getCharacters();
        int[] w = characters.get(name).getWeight(), s = characters.get(name).getStats();
        return Map.ofEntries(
                Map.entry("固定攻击", Math.round(w[0] * ECHO_AVERAGE.get("固定攻击") * 100 / ECHO_AVERAGE.get("百分比攻击") / (s[0] + getWeapons().get(weapon).getMaxAtk()))),
                Map.entry("固定生命", Math.round(w[1] * ECHO_AVERAGE.get("固定生命") * 100 / ECHO_AVERAGE.get("百分比生命") / s[1])),
                Map.entry("固定防御", Math.round(w[2] * ECHO_AVERAGE.get("固定防御") * 100 / ECHO_AVERAGE.get("百分比防御") / s[2])),
                Map.entry("百分比攻击", w[0]),
                Map.entry("百分比生命", w[1]),
                Map.entry("百分比防御", w[2]),
                Map.entry("暴击率", w[3]),
                Map.entry("暴击伤害", w[3]),
                Map.entry("共鸣效率", w[4]),
                Map.entry("普攻伤害加成", w[5]),
                Map.entry("重击伤害加成", w[6]),
                Map.entry("共鸣技能伤害加成", w[7]),
                Map.entry("共鸣解放伤害加成", w[8])
        );
    }

    public Map<String, Number> getweights(String name, String weapon) { // 获取角色全部副词条权重
        Map<String, Number> res = new LinkedHashMap<>();
        for (String key: ECHO_KEYS) {
            double val;
            if (EX_WEIGHTS.containsKey(name) && EX_WEIGHTS.get(name).containsKey(key)) {
                val = EX_WEIGHTS.get(name).get(key);
            } else val = getDefaultweights(name, weapon).get(key).doubleValue();
            res.put(key, val);
        }
        return res;
    }

    public static void sortData(Map<String, List<Echo>> data) { // 角色声骸列表排序
        for (String key: data.keySet()) {
            List<Echo> list = data.get(key);
            list.sort((a, b) -> {
                if (a.getScore().isEmpty()) return 1;
                else if (b.getScore().isEmpty()) return -1;
                if (a.getCost().equals(b.getCost()))
                    return Integer.parseInt(b.getScore()) - Integer.parseInt(a.getScore());
                else
                    return Integer.parseInt(b.getCost()) - Integer.parseInt(a.getCost());
            });
        }
    }

    public static boolean verifyEquipPhantom(RoleDTO.EquipPhantom equipPhantom) { // 校验声骸合法性
        if (equipPhantom == null) return false;
        if (equipPhantom.getLevel() != 25) return false;
        if (equipPhantom.getQuality() != 5) return false;
        return equipPhantom.getSubProps().size() == 5;
    }
}