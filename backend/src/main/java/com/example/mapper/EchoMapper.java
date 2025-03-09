package com.example.mapper;

import com.example.dto.EchoPair;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface EchoMapper { // 声骸相关的映射器

    @Select("select name, echo from echos where username = #{username}")
    List<EchoPair> getEchoAndName(String username);

    @Select("select id, echo from echos where username = #{username} and name = #{name}")
    List<EchoPair> getEchoAndId(String username, String name);


    @Update("update echos set echo = #{newEcho} where id = #{id}")
    void updateEcho(int id, String newEcho);

    @Insert("insert into echos(username, name, echo) values (#{username}, #{name}, #{echo})")
    void insertEcho(String username, String name, String echo);

    @Delete("delete from echos where id = #{id}")
    void deleteEcho(int id);


    @Select("select weight from weights where username = #{username} and name = #{name}")
    String getWeight(String username, String name);

    @Insert("insert into weights(username, name, weight) values(#{username}, #{name}, #{weight})")
    void insertWeight(String username, String name, String weight);

    @Update("update weights set weight = #{weight} where username = #{username} and name = #{name}")
    int updateWeight(String username, String name, String weight);

    @Delete("delete from weights where username = #{username} and name = #{name}")
    void deleteWeight(String username, String name);


    @Select("select weapon from weapons where username = #{username} and name = #{name}")
    String selectWeapon(String username, String name);

    @Insert("insert into weapons(username, name, weapon) values (#{username}, #{name}, #{weapon}) " +
            "on duplicate key update weapon = #{weapon}")
    void updateWeapon(String username, String name, String weapon);

    @Delete("delete from weapons where username = #{username} and name = #{name}")
    void deleteWeapon(String username, String name);


    @Select("select id, echo, name_list from temp_echos where username = #{username}")
    List<EchoPair> getTempEchoList(String username);

    @Insert("insert into temp_echos(username, echo, name_list) values (#{username}, #{echo}, #{nameList})")
    void insertTempEcho(String username, String echo, String nameList);

    @Update("update temp_echos set name_list = #{nameList} where id = #{id}")
    void updateTempEchoList(int id, String nameList);

    @Delete("delete from temp_echos where id = #{id}")
    void deleteTempEcho(int id);
}