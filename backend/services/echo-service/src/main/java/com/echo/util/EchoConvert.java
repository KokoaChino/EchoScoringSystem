package com.echo.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class EchoConvert { // 声骸转换类

    private static final Map<Integer, Map<String, String>> MAIN_ATTRIBUTE_NAME_MAP = Map.of( // 主词条名称映射
            1, new HashMap<>() {{
                put("攻击", "百分比攻击 18%");
                put("生命", "百分比生命 22.8%");
                put("防御", "百分比防御 18%");
            }},
            3, new HashMap<>() {{
                put("攻击", "百分比攻击 30%");
                put("生命", "百分比生命 30%");
                put("防御", "百分比防御 38%");
                put("共鸣效率", "共鸣效率 32%");
                put("冷凝伤害加成", "属性伤害加成 30%");
                put("热熔伤害加成", "属性伤害加成 30%");
                put("导电伤害加成", "属性伤害加成 30%");
                put("气动伤害加成", "属性伤害加成 30%");
                put("衍射伤害加成", "属性伤害加成 30%");
                put("湮灭伤害加成", "属性伤害加成 30%");
            }},
            4, new HashMap<>() {{
                put("攻击", "百分比攻击 33%");
                put("生命", "百分比生命 33%");
                put("防御", "百分比防御 41.8%");
                put("治疗效果加成", "治疗效果加成 26.4%");
                put("暴击", "暴击率 22%");
                put("暴击伤害", "暴击伤害 44%");
            }}
    );
    private static final Map<String, String> SUB_ATTRIBUTE_NAME_MAP = new HashMap<>() {{ // 副词条名称映射
        put("攻击", "固定攻击");
        put("生命", "固定生命");
        put("防御", "固定防御");
        put("攻击%", "百分比攻击");
        put("生命%", "百分比生命");
        put("防御%", "百分比防御");
        put("暴击", "暴击率");
        put("暴击伤害", "暴击伤害");
        put("共鸣效率", "共鸣效率");
        put("普攻伤害加成", "普攻伤害加成");
        put("重击伤害加成", "重击伤害加成");
        put("共鸣技能伤害加成", "共鸣技能伤害加成");
        put("共鸣解放伤害加成", "共鸣解放伤害加成");
    }};
    private static final Set<String> BASE_ATTRIBUTE_NAME_SET = Set.of("攻击", "生命", "防御"); // 固定属性名称

    public static String attributeValueMap(String attributeValue) { // 属性值映射
        return attributeValue.replaceAll("%$", "").replaceAll("\\.0$", "");
    }

    public static String mainAttributeNameMap(String mainAttributeName, int cost) { // 主词条名称映射
        return MAIN_ATTRIBUTE_NAME_MAP.get(cost).get(mainAttributeName);
    }

    public static String subAttributeNameMap(String subAttributeName, String subAttributeValue) { // 副词条名称映射
        if (BASE_ATTRIBUTE_NAME_SET.contains(subAttributeName) && subAttributeValue.endsWith("%")) {
            subAttributeName += "%";
        }
        return SUB_ATTRIBUTE_NAME_MAP.get(subAttributeName);
    }

    public static String mainRoleNameMap(String attributeName) { // 主角名称映射
        return "漂泊者 - 男 - " + attributeName;
    }
}