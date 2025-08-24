package com.echo.dto;

import lombok.Data;
import java.util.List;


/**
 * 角色信息DTO
 */
@Data
public class RoleDTO {

    private List<Chain> chainList;
    private List<EquipPhantomAttribute> equipPhantomAddPropList;
    private List<EquipPhantomAttribute> equipPhantomAttributeList;
    private Integer level;
    private PhantomData phantomData;
    private Role role;
    private List<RoleAttribute> roleAttributeList;
    private RoleSkin roleSkin;
    private List<SkillInfo> skillList;
    private WeaponData weaponData;

    /**
     * 命座信息
     */
    @Data
    public static class Chain {
        private String description;  // 命座效果描述
        private String iconUrl;      // 图标 URL
        private String name;         // 命座名称
        private Integer order;       // 命座顺序
        private Boolean unlocked;    // 是否已解锁
    }

    /**
     * 装备声骸属性
     */
    @Data
    public static class EquipPhantomAttribute {
        private String attributeName;  // 属性名称
        private String attributeValue; // 属性值
        private String iconUrl;        // 图标 URL
        private Boolean valid;         // 是否有效（仅在 equipPhantomAttributeList 中存在）
    }

    /**
     * 声骸数据
     */
    @Data
    public static class PhantomData {
        private Integer cost;                        // 声骸装备 Cost
        private List<EquipPhantom> equipPhantomList; // 装备的声骸列表
    }

    /**
     * 装备的声骸
     */
    @Data
    public static class EquipPhantom {
        private Integer cost;              // 声骸 Cost
        private FetterDetail fetterDetail; // 羁绊信息
        private Integer level;             // 声骸等级
        private List<EquipPhantomAttribute> mainProps;    // 主属性
        private PhantomProp phantomProp;   // 声骸属性
        private Integer quality;           // 品质
        private List<EquipPhantomAttribute> subProps;     // 副属性
    }

    /**
     * 羁绊信息
     */
    @Data
    public static class FetterDetail {
        private String firstDescription;  // 第一描述
        private Integer groupId;          // 羁绊组 ID
        private String iconUrl;           // 图标 URL
        private String name;              // 羁绊名称
        private Integer num;              // 羁绊数量
        private String secondDescription; // 第二描述
        private String tripleDescription; // 第三描述
        private Integer type;             // 羁绊类型
    }

    /**
     * 声骸属性
     */
    @Data
    public static class PhantomProp {
        private Integer cost;              // Cost
        private String iconUrl;            // 图标 URL
        private String name;               // 声骸名称
        private Integer phantomId;         // 声骸 ID
        private Integer phantomPropId;     // 声骸属性 ID
        private Integer quality;           // 品质
        private String skillDescription;   // 技能描述
    }

    /**
     * 角色基本信息
     */
    @Data
    public static class Role {
        private String acronym;          // 角色缩写
        private Integer attributeId;     // 属性 ID
        private String attributeName;    // 属性名称
        private Integer breach;          // 突破次数
        private Integer chainUnlockNum;  // 解锁命座数量
        private Boolean isMainRole;      // 是否为主角色
        private Integer level;           // 等级
        private String roleIconUrl;      // 角色图标 URL
        private Integer roleId;          // 角色 ID
        private String roleName;         // 角色名称
        private String rolePicUrl;       // 角色图片 URL
        private Integer starLevel;       // 星级
        private Integer weaponTypeId;    // 武器类型 ID
        private String weaponTypeName;   // 武器类型名称
    }

    /**
     * 角色属性
     */
    @Data
    public static class RoleAttribute {
        private Integer attributeId;     // 属性 ID
        private String attributeName;    // 属性名称
        private String attributeValue;   // 属性值
        private String iconUrl;          // 图标 URL
        private Integer sort;            // 排序
    }

    /**
     * 角色皮肤
     */
    @Data
    public static class RoleSkin {
        private Boolean isAddition;   // 是否为额外皮肤
        private String picUrl;        // 皮肤图片 URL
        private Integer priority;     // 优先级
        private Integer quality;      // 品质
        private String qualityName;   // 品质名称
        private String skinIcon;      // 皮肤图标 URL
        private Integer skinId;       // 皮肤 ID
        private String skinName;      // 皮肤名称
    }

    /**
     * 技能信息
     */
    @Data
    public static class SkillInfo {
        private Integer level;    // 技能等级
        private Skill skill;      // 技能详情
    }

    /**
     * 技能详情
     */
    @Data
    public static class Skill {
        private String description;  // 技能描述
        private String iconUrl;      // 图标 URL
        private Integer id;          // 技能 ID
        private String name;         // 技能名称
        private String type;         // 技能类型
    }

    /**
     * 武器数据
     */
    @Data
    public static class WeaponData {
        private Integer breach;              // 突破次数
        private Integer level;               // 等级
        private List<EquipPhantomAttribute> mainPropList;  // 主属性列表
        private Integer resonLevel;          // 共鸣等级
        private Weapon weapon;               // 武器信息
    }

    /**
     * 武器信息
     */
    @Data
    public static class Weapon {
        private String effectDescription;   // 武器效果描述
        private String weaponEffectName;    // 武器效果名称
        private String weaponIcon;          // 武器图标 URL
        private Integer weaponId;           // 武器 ID
        private String weaponName;          // 武器名称
        private Integer weaponStarLevel;    // 武器星级
        private Integer weaponType;         // 武器类型
    }
}