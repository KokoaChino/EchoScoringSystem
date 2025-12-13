package com.echo.external.dto;

import com.echo.entity.Character;
import com.echo.entity.Weapon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDetailDTO { // 角色 DTO

    private String id;
    private String name;
    private String type;
    private Integer star;
    private Integer maxAtk;
    private Integer maxHp;
    private Integer maxDef;
    private String exclusiveWeapon;
    private String squadFigureUrl;
    private String avatarUrl;

    public Character toCharacter() {
        Character res = new Character();
        Weapon weapon = new Weapon();
        weapon.setName(this.exclusiveWeapon);
        res.setId(this.id);
        res.setName(this.name);
        res.setType(this.type);
        res.setStar(this.star);
        res.setWeapon(weapon);
        res.setStats(new int[]{this.maxAtk, this.maxHp, this.maxDef});
        res.setSquadFigureUrl(this.squadFigureUrl);
        res.setAvatarUrl(this.avatarUrl);
        res.setPinyin(null);
        res.setScaleRatio(null);
        res.setWeight(null);
        return res;
    }
}