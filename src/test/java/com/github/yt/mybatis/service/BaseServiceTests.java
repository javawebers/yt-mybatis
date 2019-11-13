package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.message.domain.Message;
import com.github.yt.mybatis.example.message.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceTests {

    @Resource
    private MessageService messageService;

    @Test
    public void test() {
        Message message1 = messageService.get(Message.class, "message_1");
    }
}
