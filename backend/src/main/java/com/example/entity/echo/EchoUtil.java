package com.example.entity.echo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
    public static final Map<String, Weapon> WEAPONS = new LinkedHashMap<>() {{ // 武器
        put("时和岁稔", new Weapon("时和岁稔", "长刃", 5, 587));
        put("苍鳞千嶂", new Weapon("苍鳞千嶂", "长刃", 5, 587));
        put("浩境粼光", new Weapon("浩境粼光", "长刃", 5, 587));
        put("诸方玄枢", new Weapon("诸方玄枢", "臂铠", 5, 587));
        put("擎渊怒涛", new Weapon("擎渊怒涛", "臂铠", 5, 587));
        put("裁春", new Weapon("裁春", "迅刀", 5, 587));
        put("赫奕流明", new Weapon("赫奕流明", "迅刀", 5, 587));
        put("千古洑流", new Weapon("千古洑流", "迅刀", 5, 587));
        put("停驻之烟", new Weapon("停驻之烟", "佩枪", 5, 587));
        put("星序协响", new Weapon("星序协响", "音感仪", 5, 412));
        put("琼枝冰绡", new Weapon("琼枝冰绡", "音感仪", 5, 500));
        put("掣傀之手", new Weapon("掣傀之手", "音感仪", 5, 500));
        put("漪澜浮录", new Weapon("漪澜浮录", "音感仪", 5, 500));

        put("凋亡频移", new Weapon("凋亡频移", "长刃", 4, 462));
        put("纹秋", new Weapon("纹秋", "长刃", 4, 412));
        put("异响空灵", new Weapon("异响空灵", "长刃", 4, 337));
        put("重破刃-41型", new Weapon("重破刃-41型", "长刃", 4, 412));
        put("永夜长明", new Weapon("永夜长明", "长刃", 4, 337));
        put("东落", new Weapon("东落", "长刃", 4, 412));

        put("尘云旋臂", new Weapon("尘云旋臂", "臂铠", 4, 462));
        put("金掌", new Weapon("金掌", "臂铠", 4, 412));
        put("呼啸重音", new Weapon("呼啸重音", "臂铠", 4, 337));
        put("钢影拳-21丁型", new Weapon("钢影拳-21丁型", "臂铠", 4, 387));
        put("袍泽之固", new Weapon("袍泽之固", "臂铠", 4, 337));
        put("骇行", new Weapon("骇行", "臂铠", 4, 412));

        put("心之锚", new Weapon("心之锚", "迅刀", 4, 462));
        put("永续坍缩", new Weapon("永续坍缩", "迅刀", 4, 462));
        put("飞景", new Weapon("飞景", "迅刀", 4, 387));
        put("行进序曲", new Weapon("行进序曲", "迅刀", 4, 337));
        put("瞬斩刀-18型", new Weapon("瞬斩刀-18型", "迅刀", 4, 387));
        put("不归孤军", new Weapon("不归孤军", "迅刀", 4, 412));
        put("西升", new Weapon("西升", "迅刀", 4, 412));

        put("悖论喷流", new Weapon("悖论喷流", "佩枪", 4, 462));
        put("奔雷", new Weapon("奔雷", "佩枪", 4, 387));
        put("华彩乐段", new Weapon("华彩乐段", "佩枪", 4, 337));
        put("穿击枪-26型", new Weapon("穿击枪-26型", "佩枪", 4, 387));
        put("无眠烈火", new Weapon("无眠烈火", "佩枪", 4, 412));
        put("飞逝", new Weapon("飞逝", "佩枪", 4, 412));

        put("核熔星盘", new Weapon("核熔星盘", "音感仪", 4, 462));
        put("清音", new Weapon("清音", "音感仪", 4, 412));
        put("奇幻变奏", new Weapon("奇幻变奏", "音感仪", 4, 337));
        put("鸣动仪-25型", new Weapon("鸣动仪-25型", "音感仪", 4, 337));
        put("今州守望", new Weapon("今州守望", "音感仪", 4, 387));
        put("异度", new Weapon("异度", "音感仪", 4, 412));
    }};
    public static final Map<String, Character> CHARACTERS = new LinkedHashMap<>() {{ // 角色
        put("安可", new Character("安可", "anke", "热熔", WEAPONS.get("漪澜浮录"), 5,
                new int[]{425, 10512, 1246}, new int[]{11, 1, 1, 20, 2, 9, 2, 6, 3}));
        put("长离", new Character("长离", "changli", "热熔", WEAPONS.get("赫奕流明"), 5,
                new int[]{462, 10387, 1099}, new int[]{11, 1, 1, 20, 3, 2, 3, 8, 5}));
        put("椿", new Character("椿", "chun", "湮灭", WEAPONS.get("裁春"), 5,
                new int[]{450, 10325, 1161}, new int[]{11, 1, 1, 20, 2, 9, 3, 0, 5}));
        put("丹瑾", new Character("丹瑾", "danjing", "湮灭", WEAPONS.get("千古洑流"), 4,
                new int[]{262, 9437, 1148}, new int[]{11, 1, 1, 20, 2, 3, 9, 5, 2}));
        put("守岸人", new Character("守岸人", "shouanren", "衍射", WEAPONS.get("星序协响"), 5,
                new int[]{287, 16712, 1099}, new int[]{2, 14, 1, 16, 20, 1, 1, 1, 10}));
        put("维里奈", new Character("维里奈", "weilinai", "衍射", WEAPONS.get("奇幻变奏"), 5,
                new int[]{337, 14237, 1099}, new int[]{17, 3, 3, 2, 20, 1, 1, 1, 0}));
    }};
    public static final Map<String, Map<String, Double>> EX_WEIGTHS = Map.ofEntries( // 特别的角色副词条权重
            Map.entry("守岸人", Map.of("暴击率", 3.0))
    );

    public static String getPinyin(String name) { // 获取角色拼音
        return CHARACTERS.get(name).getPinyin();
    }

    public static Map<String, ? extends Number> getDefaultWeigths(String name, String weapon) { // 获取角色副词条默认权重展开
        int[] w = CHARACTERS.get(name).getWeight(), s = CHARACTERS.get(name).getStats();
        return Map.ofEntries(
                Map.entry("固定攻击", Math.round(w[0] * ECHO_AVERAGE.get("固定攻击") * 10000 / ECHO_AVERAGE.get("百分比攻击") / (s[0] + WEAPONS.get(weapon).getATH())) / 100.0),
                Map.entry("固定生命", Math.round(w[1] * ECHO_AVERAGE.get("固定生命") * 10000 / ECHO_AVERAGE.get("百分比生命") / s[1]) / 100.0),
                Map.entry("固定防御", Math.round(w[2] * ECHO_AVERAGE.get("固定防御") * 10000 / ECHO_AVERAGE.get("百分比防御") / s[2]) / 100.0),
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
    public static Map<String, Number> getWeigths(String name, String weapon) { // 获取角色全部副词条权重
        Map<String, Number> res = new LinkedHashMap<>();
        for (String key: ECHO_KEYS) {
            double val;
            if (EX_WEIGTHS.containsKey(name) && EX_WEIGTHS.get(name).containsKey(key)) {
                val = EX_WEIGTHS.get(name).get(key);
            } else val = getDefaultWeigths(name, weapon).get(key).doubleValue();
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
}
