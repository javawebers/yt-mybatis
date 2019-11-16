package com.github.yt.mybatis.entity.message.service;

import com.github.yt.mybatis.entity.message.dao.MessageMapper;
import com.github.yt.mybatis.entity.message.domain.Message;
import com.github.yt.mybatis.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息服务层
 *
 */
@Service
//@Transactional
public class  MessageService extends BaseService<Message> {

    @Autowired
    private MessageMapper mapper;


}
