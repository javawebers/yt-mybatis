package com.github.yt.mybatis.service;

import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.entity.message.domain.Message;
import com.github.yt.mybatis.entity.message.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class FieldColumnConvertTests {

    @Resource
    private MessageService messageService;

    @Test
    public void saveThenFind() {
        Message message = new Message();
        Integer fieldColumn = 222;
        message.setBusinessId("test").setFieldColumn(fieldColumn);
        messageService.save(message);
        // 查询出来，对比 fieldColumn 是否为222
        Message messageDb = messageService.find(new Message().setMessageId(message.getMessageId()),
                new Query().addSelectColumn("messageId, field_column as fieldColumn"));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn);
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()),
                new Query().addWhere("field_column = #{field_column}").addParam("field_column", fieldColumn));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn);
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()).setFieldColumn(fieldColumn),
                new Query().addWhere("field_column = #{field_column}").addParam("field_column", fieldColumn));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn);

    }

    @Test
    public void updateThenFind() {
        Message message = new Message();
        Integer fieldColumn222 = 222;
        Integer fieldColumn333 = 333;
        message.setBusinessId("test").setFieldColumn(fieldColumn222);
        messageService.save(message);

        // 根据主键更新
        messageService.update(message.setFieldColumn(fieldColumn333).setDeleteFlag(false));
        Message messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn333);
        // 根据主键更新，带更新字段
        messageService.update(message.setFieldColumn(fieldColumn222), "field_column");
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn222);

        // 根据主键更新，不为空字段
        messageService.updateForSelective(message.setFieldColumn(fieldColumn333));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn333);

        // 根据条件更新
        messageService.updateByCondition(new Message().setMessageId(message.getMessageId()).setFieldColumn(fieldColumn333),
                new Query().addUpdate("field_column = #{field_column}")
                        .addParam("field_column", fieldColumn222));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，messageDb.getFieldColumn()：" + messageDb.getFieldColumn(),
                messageDb.getFieldColumn(), fieldColumn222);
    }

    @Test
    public void deleteThenFind() {
        Message message = new Message();
        Integer fieldColumn222 = 222;
        message.setBusinessId("test").setFieldColumn(fieldColumn222);

        // 根据主键删除
        messageService.save(message.setMessageId(null));
        messageService.delete(Message.class, message.getMessageId());
        Message messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertNull("删除对象失败", messageDb);

        // 根据条件删除
        messageService.save(message.setMessageId(null));
        messageService.delete(new Message().setMessageId(message.getMessageId()).setFieldColumn(fieldColumn222));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertNull("删除对象失败", messageDb);

        // 根据条件删除2
        messageService.save(message.setMessageId(null));
        messageService.delete(new Message().setMessageId(message.getMessageId()),
                new Query().addWhere("field_column = #{field_column}").addParam("field_column", fieldColumn222));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()));
        Assert.assertNull("删除对象失败", messageDb);

        // 逻辑删除
        messageService.save(message.setMessageId(null));
        messageService.logicDelete(new Message().setMessageId(message.getMessageId()).setFieldColumn(fieldColumn222));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()).setDeleteFlag(false));
        Assert.assertNull("删除对象失败", messageDb);

        // 逻辑删除2
        messageService.save(message.setMessageId(null));
        messageService.logicDelete(new Message().setMessageId(message.getMessageId()),
                new Query().addWhere("field_column = #{field_column}").addParam("field_column", fieldColumn222));
        messageDb = messageService.find(new Message().setMessageId(message.getMessageId()).setDeleteFlag(false));
        Assert.assertNull("删除对象失败", messageDb);
    }

}
