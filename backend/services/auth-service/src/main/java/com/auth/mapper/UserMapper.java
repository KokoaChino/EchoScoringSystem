package com.auth.mapper;

import com.common.entity.Account;
import com.common.entity.AccountUser;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper { // 用户相关的映射器

    @Select("select * from account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select id, username, email from account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);


    @Update("update account set username = #{username} where email = #{email}")
    int resetUsernameByEmail(String username, String email);

    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);

    @Update("update account set email = #{newEmail} where email = #{oldEmail}")
    int resetEmailByEmail(String oldEmail, String newEmail);


    @Select("select vip = 1 from account where username = #{username}")
    Boolean isVipUser(String username);

    @Update("update account set vip = 1 where username = #{username}")
    void updateVipUser(String username);
}