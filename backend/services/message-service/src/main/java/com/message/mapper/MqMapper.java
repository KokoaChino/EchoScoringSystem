package com.message.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface MqMapper {

    @Insert("insert into message_id_log(message_id, status) values (#{messageId}, #{status})")
    int insertMessageIdLog(String messageId, int status);

    @Update("update message_id_log set status = #{status} where message_id = #{messageId}")
    void updateMessageIdLog(String messageId, int status);
}
