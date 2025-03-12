package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface MqMapper {

    @Insert("insert into message_id_log(message_id, status) values (#{messageId}, #{status})")
    void insertMessageIdLog(String messageId, int status);

    @Select("select status from message_id_log where message_id = #{messageId}")
    Integer selectMessageIdLog(String messageId);

    @Update("update message_id_log set status = #{status} where message_id = #{messageId}")
    void updateMessageIdLog(String messageId, int status);
}
