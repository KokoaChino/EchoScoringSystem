package com.pay.mapper;

import com.pay.entity.Order;
import org.apache.ibatis.annotations.*;


@Mapper
public interface PayMapper {

    @Select("select * from `order` where username = #{username}")
    Order findOrderByUsername(String username);

    @Delete("delete from `order` where username = #{username}")
    void deleteOrderByUsername(String username);

    @Insert("insert into `order` (id, username, create_time, qr_url) values(#{id}, #{username}, #{createTime}, #{qrUrl})")
    void insertOrder(Order order);
}