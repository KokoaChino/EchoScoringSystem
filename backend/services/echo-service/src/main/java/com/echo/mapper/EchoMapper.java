package com.echo.mapper;

import com.echo.dto.EchoPair;
import com.echo.mapper.persistence.CharacterConfigDO;
import com.echo.mapper.persistence.EchoDO;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface EchoMapper { // 声骸相关的映射器

    @Select("select name, echo from echos where username = #{username}")
    List<EchoPair> getEchoAndName(String username);

    @Select("select id, echo from echos where username = #{username} and name = #{name}")
    List<EchoPair> getEchoAndId(String username, String name);

    @Select("select distinct username from echos")
    List<String> selectDistinctUsernames();


    @Update("update echos set echo = #{newEcho} where id = #{id}")
    void updateEcho(int id, String newEcho);

    @Insert("insert into echos(username, name, echo) values (#{username}, #{name}, #{echo})")
    void insertEcho(String username, String name, String echo);

    @Insert("""
    <script>
    INSERT INTO echos (username, name, echo) VALUES
    <foreach collection='echos' item='echo' separator=','>
    (#{echo.username}, #{echo.name}, #{echo.echo})
    </foreach>
    </script>
    """)
    void batchInsertionEcho(@Param("echos") List<EchoDO> echos);

    @Delete("delete from echos where id = #{id}")
    void deleteEchoById(int id);

    @Delete("delete from echos where username = #{username}")
    void deleteEchoByUsername(String username);


    @Select("select weight from weights where username = #{username} and name = #{name}")
    String getWeight(String username, String name);

    @Select("SELECT name, weight FROM weights WHERE username = #{username}")
    List<CharacterConfigDO> getAllWeightsByUsername(String username);

    @Insert("insert into weights(username, name, weight) values(#{username}, #{name}, #{weight})")
    void insertWeight(String username, String name, String weight);

    @Update("update weights set weight = #{weight} where username = #{username} and name = #{name}")
    int updateWeight(String username, String name, String weight);

    @Delete("delete from weights where username = #{username} and name = #{name}")
    void deleteWeight(String username, String name);


    @Select("select weapon from weapons where username = #{username} and name = #{name}")
    String selectWeapon(String username, String name);

    @Select("SELECT name, weapon FROM weapons WHERE username = #{username}")
    List<CharacterConfigDO> getAllWeaponsByUsername(String username);

    @Insert("insert into weapons(username, name, weapon) values (#{username}, #{name}, #{weapon}) " +
            "on duplicate key update weapon = #{weapon}")
    void updateWeapon(String username, String name, String weapon);

    @Delete("delete from weapons where username = #{username} and name = #{name}")
    void deleteWeapon(String username, String name);


    @Select("select id, echo, name_list from temp_echos where username = #{username}")
    List<EchoPair> getTempEchoList(String username);

    @Insert("insert into temp_echos(username, echo, name_list) values (#{username}, #{echo}, #{nameList})")
    void insertTempEcho(String username, String echo, String nameList);

    @Insert("""
    <script>
    INSERT INTO temp_echos (username, echo, name_list) VALUES
    <foreach collection='echoPairs' item='echoPair' separator=','>
    (#{echoPair.name}, #{echoPair.echo}, #{echoPair.name_list})
    </foreach>
    </script>
    """)
    void batchInsertionTempEchos(@Param("echoPairs") List<EchoPair> echoPairs);

    @Update("update temp_echos set name_list = #{nameList} where id = #{id}")
    void updateTempEchoList(int id, String nameList);

    @Delete("delete from temp_echos where id = #{id}")
    void deleteTempEcho(int id);

    @Delete("delete from temp_echos where username = #{username}")
    void deleteTempEchoByUsername(String username);
}