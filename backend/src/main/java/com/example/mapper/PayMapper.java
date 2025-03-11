package com.example.mapper;

import com.example.entity.order.Order;
import org.apache.ibatis.annotations.*;


@Mapper
public interface PayMapper {

    @Select("select vip = 1 from account where username = #{username}")
    Boolean isVipUser(String username);

    @Update("update account set vip = 1 where username = #{username}")
    void updateVipUser(String username);


    @Select("select * from `order` where username = #{username}")
    Order findOrderByUsername(String username);

    @Delete("delete from `order` where username = #{username}")
    void deleteOrderByUsername(String username);

    @Insert("insert into `order` (id, username, create_time, qr_url) values(#{id}, #{username}, #{createTime}, #{qrUrl})")
    void insertOrder(Order order);
}