package com.common.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;


@Mapper
public interface DataGovernanceMapper {

    @Select("select distinct table_name from information_schema.COLUMNS " +
            "where table_schema = '${serviceDb}' and column_name = 'username'")
    List<String> findAllTables(String serviceDb);

    @Delete("<script>" +
            "delete from `${tableName}` where username = #{username}" +
            "</script>")
    void deleteAccountByUsername(String tableName, String username);

    @Update("<script>" +
            "update `${tableName}` set username = #{username} where username = #{oldUsername}" +
            "</script>")
    void resetUsername(String tableName, String username, String oldUsername);
}