package com.echo.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;


@Mapper
public interface DataGovernanceMapper {

    @Select("select distinct table_name from information_schema.COLUMNS " +
            "where table_schema = 'echo_service_db' and column_name = 'username'")
    List<String> findAllTables();

    @Delete("<script>" +
            "delete from ${table_name} where username = #{username}" +
            "</script>")
    void deleteAccountByUsername(String table_name, String username);

    @Update("<script>" +
            "update ${table_name} set username = #{username} where username = #{old_username}" +
            "</script>")
    void resetUsername(String table_name, String username, String old_username);
}