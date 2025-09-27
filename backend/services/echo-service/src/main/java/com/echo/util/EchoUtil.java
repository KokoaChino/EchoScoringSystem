package com.echo.util;

import com.echo.dto.RoleDTO;
import com.echo.entity.Character;
import com.echo.entity.Echo;
import com.echo.entity.Weapon;
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
        put("焰痕", new Weapon("焰痕", "长刃", 5, 587));
        put("驭冕铸雷之权", new Weapon("驭冕铸雷之权", "长刃", 5, 675));

        put("诸方玄枢", new Weapon("诸方玄枢", "臂铠", 5, 587));
        put("擎渊怒涛", new Weapon("擎渊怒涛", "臂铠", 5, 587));
        put("悲喜剧", new Weapon("悲喜剧", "臂铠", 5, 587));
        put("焰光裁定", new Weapon("焰光裁定", "臂铠", 5, 587));
        put("万物持存的注释", new Weapon("万物持存的注释", "臂铠", 5, 500));

        put("裁春", new Weapon("裁春", "迅刀", 5, 587));
        put("赫奕流明", new Weapon("赫奕流明", "迅刀", 5, 587));
        put("千古洑流", new Weapon("千古洑流", "迅刀", 5, 587));
        put("不灭航路", new Weapon("不灭航路", "迅刀", 5, 412));
        put("血誓盟约", new Weapon("血誓盟约", "迅刀", 5, 587));
        put("不屈命定之冠", new Weapon("不屈命定之冠", "迅刀", 5, 412));

        put("死与舞", new Weapon("死与舞", "佩枪", 5, 500));
        put("停驻之烟", new Weapon("停驻之烟", "佩枪", 5, 587));
        put("林间的咏叹调", new Weapon("林间的咏叹调", "佩枪", 5, 500));

        put("星序协响", new Weapon("星序协响", "音感仪", 5, 412));
        put("琼枝冰绡", new Weapon("琼枝冰绡", "音感仪", 5, 500));
        put("掣傀之手", new Weapon("掣傀之手", "音感仪", 5, 500));
        put("漪澜浮录", new Weapon("漪澜浮录", "音感仪", 5, 500));
        put("和光回唱", new Weapon("和光回唱", "音感仪", 5, 500));
        put("海的呢喃", new Weapon("海的呢喃", "音感仪", 5, 500));
        put("幽冥的忘忧章", new Weapon("幽冥的忘忧章", "音感仪", 5, 587));

        put("凋亡频移", new Weapon("凋亡频移", "长刃", 4, 462));
        put("纹秋", new Weapon("纹秋", "长刃", 4, 412));
        put("异响空灵", new Weapon("异响空灵", "长刃", 4, 337));
        put("重破刃-41型", new Weapon("重破刃-41型", "长刃", 4, 412));
        put("永夜长明", new Weapon("永夜长明", "长刃", 4, 337));
        put("东落", new Weapon("东落", "长刃", 4, 412));
        put("容赦的沉思录", new Weapon("容赦的沉思录", "长刃", 4, 462));
        put("金穹", new Weapon("金穹", "长刃", 4, 412));

        put("尘云旋臂", new Weapon("尘云旋臂", "臂铠", 4, 462));
        put("金掌", new Weapon("金掌", "臂铠", 4, 412));
        put("呼啸重音", new Weapon("呼啸重音", "臂铠", 4, 337));
        put("钢影拳-21丁型", new Weapon("钢影拳-21丁型", "臂铠", 4, 387));
        put("袍泽之固", new Weapon("袍泽之固", "臂铠", 4, 337));
        put("骇行", new Weapon("骇行", "臂铠", 4, 412));
        put("酩酊的英雄志", new Weapon("酩酊的英雄志", "臂铠", 4, 462));
        put("凌空", new Weapon("凌空", "臂铠", 4, 412));

        put("心之锚", new Weapon("心之锚", "迅刀", 4, 462));
        put("永续坍缩", new Weapon("永续坍缩", "迅刀", 4, 462));
        put("飞景", new Weapon("飞景", "迅刀", 4, 387));
        put("行进序曲", new Weapon("行进序曲", "迅刀", 4, 337));
        put("瞬斩刀-18型", new Weapon("瞬斩刀-18型", "迅刀", 4, 387));
        put("不归孤军", new Weapon("不归孤军", "迅刀", 4, 412));
        put("西升", new Weapon("西升", "迅刀", 4, 412));
        put("风流的寓言诗", new Weapon("风流的寓言诗", "迅刀", 4, 462));
        put("翼锋", new Weapon("翼锋", "迅刀", 4, 412));

        put("悖论喷流", new Weapon("悖论喷流", "佩枪", 4, 462));
        put("奔雷", new Weapon("奔雷", "佩枪", 4, 387));
        put("华彩乐段", new Weapon("华彩乐段", "佩枪", 4, 337));
        put("穿击枪-26型", new Weapon("穿击枪-26型", "佩枪", 4, 387));
        put("无眠烈火", new Weapon("无眠烈火", "佩枪", 4, 412));
        put("飞逝", new Weapon("飞逝", "佩枪", 4, 412));
        put("叙别的罗曼史", new Weapon("叙别的罗曼史", "佩枪", 4, 462));
        put("阳焰", new Weapon("阳焰", "佩枪", 4, 412));

        put("核熔星盘", new Weapon("核熔星盘", "音感仪", 4, 462));
        put("清音", new Weapon("清音", "音感仪", 4, 412));
        put("奇幻变奏", new Weapon("奇幻变奏", "音感仪", 4, 337));
        put("鸣动仪-25型", new Weapon("鸣动仪-25型", "音感仪", 4, 337));
        put("今州守望", new Weapon("今州守望", "音感仪", 4, 387));
        put("异度", new Weapon("异度", "音感仪", 4, 412));
        put("渊海回声", new Weapon("渊海回声", "音感仪", 4, 337));
        put("虚饰的华尔兹", new Weapon("虚饰的华尔兹", "音感仪", 4, 462));
        put("大海的馈赠", new Weapon("大海的馈赠", "音感仪", 4, 462));
        put("曜光", new Weapon("曜光", "音感仪", 4, 412));
    }};
    public static Map<String, Character> getCharacters () {
        return new LinkedHashMap<>() {{
            /* 下面的角色权重通过搜寻各种攻略 + 自己的理解综合得出 */
            put("长离", new Character("长离", "changli", "热熔", WEAPONS.get("赫奕流明"), 5,
                    new int[]{462, 10387, 1099}, new int[]{71, 2, 2, 100, 8, 7, 2, 46, 16}));
            put("安可", new Character("安可", "anke", "热熔", WEAPONS.get("掣傀之手"), 5,
                    new int[]{425, 10512, 1246}, new int[]{71, 2, 2, 100, 9, 35, 8, 23, 5}));
            put("维里奈", new Character("维里奈", "weilinai", "衍射", WEAPONS.get("星序协响"), 5,
                    new int[]{337, 14237, 1099}, new int[]{85, 20, 20, 10, 100, 5, 5, 5, 0}));
            put("守岸人", new Character("守岸人", "shouanren", "衍射", WEAPONS.get("星序协响"), 5,
                    new int[]{287, 16712, 1099}, new int[]{10, 70, 5, 80, 100, 5, 5, 5, 50}));
            put("丹瑾", new Character("丹瑾", "danjing", "湮灭", WEAPONS.get("赫奕流明"), 4,
                    new int[]{262, 9437, 1148}, new int[]{71, 12, 12, 100, 2, 7, 44, 17, 3}));
            put("椿", new Character("椿", "chun", "湮灭", WEAPONS.get("裁春"), 5,
                    new int[]{450, 10325, 1161}, new int[]{71, 2, 2, 100, 10, 54, 3, 0, 14}));
            put("散华", new Character("散华", "sanhua", "冷凝", WEAPONS.get("赫奕流明"), 4,
                    new int[]{275, 10062, 941}, new int[]{71, 2, 2, 100, 6, 7, 24, 25, 15}));
            put("今汐", new Character("今汐", "jinxi", "衍射", WEAPONS.get("时和岁稔"), 5,
                    new int[]{412, 10825, 1258}, new int[]{71, 2, 2, 100, 9, 8, 4, 41, 18}));
            put("洛可可", new Character("洛可可", "luokeke", "湮灭", WEAPONS.get("悲喜剧"), 5,
                    new int[]{375, 12250, 1197}, new int[]{71, 2, 2, 100, 10, 10, 48, 13, 0}));
            put("卡提希娅", new Character("卡提希娅", "katixiya", "气动", WEAPONS.get("不屈命定之冠"), 5,
                    new int[]{312, 14800, 611}, new int[]{1, 63, 4, 100, 10, 37, 0, 6, 20}));
            put("弗洛洛", new Character("弗洛洛", "fuluoluo", "湮灭", WEAPONS.get("幽冥的忘忧章"), 5,
                    new int[]{437, 10775, 1136}, new int[]{71, 2, 2, 100, 0, 9, 2, 55, 5}));
            /* 下面的角色权重仅通过搜寻攻略得出 */
            put("尤诺", new Character("尤诺", "younuo", "气动", WEAPONS.get("万物持存的注释"), 5,
                    new int[]{450, 10525, 1124}, new int[]{71, 2, 2, 100, 15, 13, 0, 7, 51}));
            put("奥古斯塔", new Character("奥古斯塔", "aogusita", "导电", WEAPONS.get("驭冕铸雷之权"), 5,
                    new int[]{462, 10300, 1112}, new int[]{71, 2, 2, 100, 11, 5, 56, 10, 0}));
            put("露帕", new Character("露帕", "lupa", "热熔", WEAPONS.get("焰痕"), 5,
                    new int[]{387, 11912, 1185}, new int[]{71, 2, 2, 100, 10, 3, 3, 20, 45}));
            put("夏空", new Character("夏空", "xiakong", "气动", WEAPONS.get("林间的咏叹调"), 5,
                    new int[]{375, 12237, 1197}, new int[]{71, 2, 2, 100, 12, 20, 15, 6, 30}));
            put("赞妮", new Character("赞妮", "zanni", "衍射", WEAPONS.get("焰光裁定"), 5,
                    new int[]{437, 10775, 1136}, new int[]{71, 2, 2, 100, 10, 4, 50, 4, 13}));
            put("坎特蕾拉", new Character("坎特蕾拉", "kanteleila", "湮灭", WEAPONS.get("海的呢喃"), 5,
                    new int[]{400, 11600, 1099}, new int[]{71, 2, 2, 100, 13, 63, 1, 7, 0}));
            put("菲比", new Character("菲比", "feibi", "衍射", WEAPONS.get("和光回唱"), 5,
                    new int[]{412, 10825, 1258}, new int[]{71, 2, 2, 100, 9, 13, 39, 6, 13}));
            put("珂莱塔", new Character("珂莱塔", "kelaita", "冷凝", WEAPONS.get("死与舞"), 5,
                    new int[]{462, 12450, 1197}, new int[]{71, 2, 2, 100, 11, 9, 2, 60, 0}));
            /* 下面的角色权重未进行人工设置，默认权重由 AI 生成 */
            put("漂泊者 - 男 - 气动", new Character("漂泊者 - 男 - 气动", "piaobozhe-nan-qidong", "气动", WEAPONS.get("血誓盟约"), 5,
                    new int[]{437, 10775, 1136}, new int[]{71, 2, 2, 100, 60, 0, 0, 50, 21}));
            put("漂泊者 - 女 - 气动", new Character("漂泊者 - 女 - 气动", "piaobozhe-nv-qidong", "气动", WEAPONS.get("血誓盟约"), 5,
                    new int[]{437, 10775, 1136}, new int[]{71, 2, 2, 100, 60, 0, 0, 50, 21}));
            put("布兰特", new Character("布兰特", "bulante", "热熔", WEAPONS.get("不灭航路"), 5,
                    new int[]{375, 11675, 1307}, new int[]{71, 2, 2, 100, 85, 50, 0, 15, 6}));
            put("相里要", new Character("相里要", "xiangliyao", "导电", WEAPONS.get("诸方玄枢"), 5,
                    new int[]{425, 10625, 1222}, new int[]{71, 2, 2, 100, 10, 3, 2, 15, 51}));
            put("折枝", new Character("折枝", "zhezhi", "冷凝", WEAPONS.get("琼枝冰绡"), 5,
                    new int[]{375, 12250, 1197}, new int[]{71, 2, 2, 100, 8, 50, 0, 15, 6}));
            put("吟霖", new Character("吟霖", "yinlin", "导电", WEAPONS.get("掣傀之手"), 5,
                    new int[]{400, 11000, 1283}, new int[]{71, 2, 2, 100, 8, 7, 30, 24, 10}));
            put("忌炎", new Character("忌炎", "jiyan", "气动", WEAPONS.get("苍鳞千嶂"), 5,
                    new int[]{437, 10487, 1185}, new int[]{71, 2, 2, 100, 14, 5, 50, 10, 6}));
            put("卡卡罗", new Character("卡卡罗", "kakaluo", "导电", WEAPONS.get("苍鳞千嶂"), 5,
                    new int[]{437, 10500, 1185}, new int[]{71, 2, 2, 100, 3, 35, 10, 16, 10}));
            put("凌阳", new Character("凌阳", "lingyang", "冷凝", WEAPONS.get("擎渊怒涛"), 5,
                    new int[]{437, 10387, 1209}, new int[]{71, 2, 2, 100, 3, 46, 3, 22, 0}));
            put("鉴心", new Character("鉴心", "jianxin", "气动", WEAPONS.get("呼啸重音"), 5,
                    new int[]{337, 14112, 1124}, new int[]{71, 2, 2, 100, 20, 2, 3, 51, 15}));
            put("漂泊者 - 男 - 衍射", new Character("漂泊者 - 男 - 衍射", "piaobozhe-nan-yanshe", "衍射", WEAPONS.get("赫奕流明"), 5,
                    new int[]{375, 11400, 1368}, new int[]{71, 2, 2, 100, 8, 20, 1, 40, 10}));
            put("漂泊者 - 女 - 衍射", new Character("漂泊者 - 女 - 衍射", "piaobozhe-nv-yanshe", "衍射", WEAPONS.get("赫奕流明"), 5,
                    new int[]{375, 11400, 1368}, new int[]{71, 2, 2, 100, 8, 20, 1, 40, 10}));
            put("漂泊者 - 男 - 湮灭", new Character("漂泊者 - 男 - 湮灭", "piaobozhe-nan-yanmie", "湮灭", WEAPONS.get("赫奕流明"), 5,
                    new int[]{412, 10825, 1258}, new int[]{71, 2, 2, 100, 8, 30, 10, 20, 11}));
            put("漂泊者 - 女 - 湮灭", new Character("漂泊者 - 女 - 湮灭", "piaobozhe-nv-yanmie", "湮灭", WEAPONS.get("赫奕流明"), 5,
                    new int[]{412, 10825, 1258}, new int[]{71, 2, 2, 100, 8, 30, 10, 20, 11}));
            put("灯灯", new Character("灯灯", "dengdeng", "导电", WEAPONS.get("浩境粼光"), 4,
                    new int[]{337, 8500, 879}, new int[]{71, 2, 2, 100, 8, 50, 1, 5, 15}));
            put("釉瑚", new Character("釉瑚", "youhu", "冷凝", WEAPONS.get("擎渊怒涛"), 4,
                    new int[]{262, 9975, 1051}, new int[]{71, 2, 2, 100, 10, 21, 31, 12, 7}));
            put("炽霞", new Character("炽霞", "chixia", "热熔", WEAPONS.get("停驻之烟"), 4,
                    new int[]{300, 9087, 953}, new int[]{71, 2, 2, 100, 8, 10, 0, 46, 15}));
            put("秧秧", new Character("秧秧", "yangyang", "气动", WEAPONS.get("千古洑流"), 4,
                    new int[]{250, 10200, 1099}, new int[]{71, 2, 2, 100, 8, 7, 2, 46, 16}));
            put("秋水", new Character("秋水", "qiushui", "气动", WEAPONS.get("华彩乐段"), 4,
                    new int[]{262, 9850, 1075}, new int[]{71, 2, 2, 100, 8, 50, 7, 7, 7}));
            put("莫特斐", new Character("莫特斐", "motefei", "热熔", WEAPONS.get("停驻之烟"), 4,
                    new int[]{250, 10025, 1136}, new int[]{71, 2, 2, 100, 8, 7, 2, 16, 46}));
            put("白芷", new Character("白芷", "baizhi", "冷凝", WEAPONS.get("星序协响"), 4,
                    new int[]{212, 12812, 1002}, new int[]{10, 80, 20, 14, 100, 0, 0, 3, 7}));
            put("渊武", new Character("渊武", "yuanwu", "导电", WEAPONS.get("呼啸重音"), 4,
                    new int[]{225, 8525, 1637}, new int[]{10, 20, 80, 14, 100, 0, 0, 4, 6}));
            put("桃祈", new Character("桃祈", "taoqi", "湮灭", WEAPONS.get("异响空灵"), 4,
                    new int[]{225, 8950, 1564}, new int[]{10, 20, 80, 14, 100, 0, 2, 3, 5}));
        }};
    }
    public static final Map<String, Character> CHARACTERS = getCharacters(); // 角色
    public static final Map<String, Map<String, Double>> EX_weightS = Map.ofEntries( // 特别的角色副词条权重
            Map.entry("守岸人", Map.of("暴击率", 15.0))
    );

    public static String getPinyin(String name) { // 获取角色拼音
        return CHARACTERS.get(name).getPinyin();
    }

    public static Map<String, ? extends Number> getDefaultweights(String name, String weapon) { // 获取角色副词条默认权重展开
        int[] w = CHARACTERS.get(name).getWeight(), s = CHARACTERS.get(name).getStats();
        return Map.ofEntries(
                Map.entry("固定攻击", Math.round(w[0] * ECHO_AVERAGE.get("固定攻击") * 100 / ECHO_AVERAGE.get("百分比攻击") / (s[0] + WEAPONS.get(weapon).getATH()))),
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
    public static Map<String, Number> getweights(String name, String weapon) { // 获取角色全部副词条权重
        Map<String, Number> res = new LinkedHashMap<>();
        for (String key: ECHO_KEYS) {
            double val;
            if (EX_weightS.containsKey(name) && EX_weightS.get(name).containsKey(key)) {
                val = EX_weightS.get(name).get(key);
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