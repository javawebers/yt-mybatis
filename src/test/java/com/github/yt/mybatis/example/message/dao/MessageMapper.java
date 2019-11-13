package com.github.yt.mybatis.example.message.dao;

import com.github.yt.mybatis.example.message.domain.Message;
import com.github.yt.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Message Mapper
 *
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {


}
