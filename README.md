# yt-mybatis
欢迎加入QQ群~ 489333310~，我们一起进步，不限于mybatis。

# 介绍
yt-mybatis是基于spring boot、mybaits封装的通用CURD框架。支持无xml复杂查询，支持无xml复杂查询，支持无xml复杂查询。  
如果您是新项目，欢迎您使用整体解决方案；如果您是历史项目，您可以很快集成CURD。

# 为什么使用yt-mybatis
通用CURD，使后端开发效率提升三倍，支持无xml复杂查询，代码更加简洁、异常控制等更加完善。

# 特性
* #### 免费开源，maven直接引用
* #### spring-boot支持，一键接入增删改查
* #### 支持级联join多表查询
* #### 支持domain默认值自动注入，可以自定义注入值
    创建人，创建时间，修改人，修改时间，逻辑删除状态。
* #### 支持分页查询
    可与yt-web结合，自动设置分页信息到查询条件类中。
* #### 代码生成
    根据数据库中表定义生成实体类及dao、service、controller的空实现。

# 使用教程
可参考```https://github.com/javawebers/yt-mybatis-example```

* ##  maven引入yt-mybatis
    ```xml
    <dependency>
        <groupId>com.github.javawebers</groupId>
        <artifactId>yt-mybatis</artifactId>
        <version>1.2.1</version>
    </dependency>
    ```
* ## 启用yt-mybatis
    ```java
    @SpringBootApplication
    @EnableYtMybatis
    public class YtMybatisDemoApplication {
        public static void main(String[] args) {
            SpringApplication.run(YtMybatisDemoApplication.class, args);
        }
    }
    ```
* ## 配置mysql连接参数
    ```properties
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/yt-mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=123456
    ```
***
通过上面配置即可```零配置```使用yt-mybatis的所有功能，下面演示基本使用
***

* ## 实现domain、dao、service（可通过代码生成器生成）
    * ### domain，继承BaseEntity\<T\>
    ```java
    package demo.com.github.yt.message.domain;
    import com.github.yt.mybatis.domain.BaseEntity;
    @javax.persistence.Table(name = "Message")
    public class Message extends BaseEntity<Message> {
        @javax.persistence.Id
        private String messageId;
        /** 
         * 用户id  
         */
        private String userId;
        /** 
         * 消息类型  
         */
        private String type;
        /** 
         * 消息内容  
         */
        private String content;
        /** 
         * 业务id  
         */
        private String businessId;
        /** 
         * 是否已读  
         */
        private Boolean read;
        //  getter setter
    }
    ```
    * ### dao 继承BaseMapper\<T\>（空类，无需任何实现）
    ```java
    package demo.com.github.yt.message.dao;
    import com.github.yt.mybatis.mapper.BaseMapper;
    import demo.com.github.yt.message.domain.Message;
    import org.apache.ibatis.annotations.Mapper;
    
    @Mapper
    public interface MessageMapper extends BaseMapper<Message> {
    }
    ```
    * ### service继承BaseService\<T\>（空类，无需任何实现，需要注入一个mapper）
     ```java
    package demo.com.github.yt.message.service;
    import com.github.yt.mybatis.service.BaseService;
    import demo.com.github.yt.message.dao.MessageMapper;
    import demo.com.github.yt.message.domain.Message;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    @Transactional
    public class  MessageService extends BaseService<Message> {
        @Autowired
        private MessageMapper mapper;
    }
    ```
* ## 导入测试数据
    略
* ## 测试功能
    ```java
    package test.mybatis;
    import com.github.yt.commons.query.Page;
    import com.github.yt.commons.query.Query;
    import demo.com.github.yt.YtMybatisDemoApplication;
    import demo.com.github.yt.message.domain.Message;
    import demo.com.github.yt.message.service.MessageService;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
    import org.testng.annotations.Test;
    import javax.annotation.Resource;
    import java.util.ArrayList;
    import java.util.List;
    
    @SpringBootTest(classes = YtMybatisDemoApplication.class)
    public class MessageServiceTest extends AbstractTestNGSpringContextTests {
    
        @Resource
        private MessageService messageService;
    
        @Test
        public void test() {
            // 1.1.根据主键查询一条记录
            Message message1 = messageService.get(Message.class, "message_1");
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createDateTime, t.modifyDateTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and (t.messageId = ?)
            //==> Parameters: message_1(String)
    
            // 1.2.根据条件查询一条记录
            // 查询 messageId为"message_1"的记录
            Message condition2 = new Message().setMessageId("message_1");
            Message message2 = messageService.find(condition2);
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createDateTime, t.modifyDateTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.messageId = ? limit 0, 2
            //==> Parameters: message_1(String)
    
            // 1.3.根据条件查询列表
            // 查询 消息状态为"已读"，并且userId为"user_1"的所有记录
            Message condition3 = new Message().setRead(true).setUserId("user_1");
            List<Message> messageList3 = messageService.findList(condition3);
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createDateTime, t.modifyDateTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.userId = ? and t.read = ?
            //==> Parameters: user_1(String), true(Boolean)
    
            // 1.4.根据条件查询分页
            Message condition4 = new Message().setRead(true);
            Query query4 = new Query().makePageNo(1).makePageSize(20);
            Page<Message> messagePage4 = messageService.findPage(condition4, query4);
            //==>  Preparing: select count(1) from Message t where 1=1 and t.read = ?
            //==> Parameters: true(Boolean)
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createDateTime, t.modifyDateTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.read = ? limit 0, 20
            //==> Parameters: true(Boolean)
    
            // 1.5.复杂查询，以findList为例，find和findPage均支持
            Message condition5 = new Message().setRead(true).setUserId("user_1");
            Query query5 = new Query();
            query5.addWhere("t.messageId = #{messageId}").addParam("messageId", "message_1");
            // in查询
            List<String> messageIdList = new ArrayList<>();
            messageIdList.add("message_1");
            messageIdList.add("message_2");
            query5.addWhere("t.messageId in ${messageIdList}").addParam("messageIdList", messageIdList);
            // 关联查询
            query5.addJoin(Query.JoinType.LEFT_JOIN, "Message t2 on t.messageId = t2.messageId");
            // 返回结果包含指定的字段
            query5.addSelectColumn("t.messageId, t.read");
            // 分组查询
            query5.addGroupBy("t.messageId, t.userId");
            // 排序
            query5.addOrderBy("t.messageId, t.userId");
            //
            query5.addWhere("t.messageId = #{messageId} or t.read = true").addParam("messageId", "message_1");
            messageService.findList(condition5, query5);
            //==>  Preparing: select t.messageId, t.read from Message t LEFT JOIN Message t2 on t.messageId = t2.messageId where 1=1 and t.userId = ? and t.read = ? and (t.messageId = ?) and (t.messageId in (?, ?)) and (t.messageId = ? or t.read = true) GROUP BY t.messageId, t.userId ORDER BY t.messageId, t.userId
            //==> Parameters: user_1(String), true(Boolean), message_1(String), message_1(String), message_2(String), message_1(String)
    
            // 2.update delete 略
        }
    }
    ```
***
通过配置修改默认行为。
***

待补充














