package com.echo.mapper;

import com.echo.mapper.persistence.CharacterExtraConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface CharacterExtraConfigMapper {

    @Select("SELECT * FROM character_extra_config WHERE enabled = 1")
    List<CharacterExtraConfigDO> selectAllEnabled();

    @Select("SELECT * FROM character_extra_config WHERE name = #{name} AND enabled = 1")
    CharacterExtraConfigDO selectByName(String name);
}