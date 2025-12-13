package com.echo.external.dto;

import lombok.Data;
import com.echo.entity.Weapon;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeaponDetailDTO { // 武器 DTO

    private String id;
    private String name;
    private String type;
    private Integer star;
    private Integer maxAtk;
    private String imageUrl;

    public Weapon toWeapon() {
        Weapon res = new Weapon();
        res.setId(id);
        res.setName(name);
        res.setType(type);
        res.setStar(star);
        res.setMaxAtk(maxAtk);
        res.setImageUrl(imageUrl);
        return res;
    }
}