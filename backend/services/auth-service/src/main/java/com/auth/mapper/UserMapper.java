package com.auth.mapper;

import com.common.entity.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper { // 用户相关的映射器

    @Select("select * from account where username = #{text} or email = #{text}")
    User findUserByNameOrEmail(String text);

    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);


    @Update("update account set username = #{username} where email = #{email}")
    int resetUsernameByEmail(String username, String email);

    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);

    @Update("update account set email = #{newEmail} where email = #{oldEmail}")
    int resetEmailByEmail(String oldEmail, String newEmail);

    @Update("update account set vip = 1 where username = #{username} and vip = 0")
    int updateVipUser(String username);

    @Update("UPDATE account SET last_visited_at = NOW() WHERE username = #{username}")
    void updateLastVisitedByUsername(String username);
}