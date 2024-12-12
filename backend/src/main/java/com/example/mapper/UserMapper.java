package com.example.mapper;

import com.example.entity.auth.Account;
import com.example.entity.user.AccountUser;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface UserMapper { // 用户相关的映射器

    @Select("select * from account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);


    @Update("update account set username = #{username} where email = #{email}")
    int resetUsernameByEmail(String username, String email);

    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);

    @Update("update account set email = #{newEmail} where email = #{oldEmail}")
    int resetEmailByEmail(String oldEmail, String newEmail);


    @Select("select table_name from information_schema.TABLES where table_schema = 'echo_scoring_system'")
    List<String> findAllTables();

    @Delete("<script>" +
            "delete from ${table_name} where username = #{username}" +
            "</script>")
    void deleteAccountByUsername(String table_name, String username);

    @Update("<script>" +
            "update ${table_name} set username = #{username} where username = #{old_username}" +
            "</script>")
    int resetUsername(String table_name, String username, String old_username);
}