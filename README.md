# 5分钟上手yt-mybatis！！！

# 介绍
yt-mybatis是基于spring boot、mybaits封装的通用CURD框架。支持无xml复杂查询。  
如果是新项目，建议使用整体解决方案；如果是历史项目，您可以很快集成CURD。

# 为什么使用yt-mybatis
通用CURD，使后端开发效率提升三倍，支持无xml复杂查询，代码更加简洁、异常控制等更加完善。

# 特性
* #### 免费开源，maven直接引用
* #### spring-boot支持，一键接入增删改查
* #### 支持级联 join 多表查询
* #### 支持 entity 默认值自动注入，可以自定义注入值
    创建人，创建时间，修改人，修改时间，逻辑删除状态。
* #### 支持分页查询
    可与yt-web结合，自动设置分页信息到查询条件类中。
* #### 代码生成
    根据数据库中表定义生成 po、entity、dao、service、controller的空实现。

# 使用教程
* ##  maven引入yt-mybatis
    在 `https://mvnrepository.com/artifact/com.github.javawebers/yt-mybatis` 找到最新版引入
    ```xml
    <dependencys>
        <dependency>
            <groupId>com.github.javawebers</groupId>
            <artifactId>yt-mybatis</artifactId>
            <version>1.5.2</version>
        </dependency>
        <!-- 引入 spring -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>2.1.9.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>
        <!-- 引入 mysql 驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.13</version>
        </dependency>
    </dependencys>
    ```

* ## 启用yt-mybatis
    在启动类上家注解`@EnableYtMybatis`
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
    spring.datasource.password=root
    ```
***
通过上面配置即可`零配置`使用 yt-mybatis 的所有功能，下面演示基本使用
***
* ## 导入测试数据
    ```sql
    create database `yt-mybatis`;
    CREATE TABLE  if not exists `yt-mybatis`.`Message` (
      `messageId` varchar(36) NOT NULL COMMENT 'id',
      `userId` varchar(36) NOT NULL COMMENT '用户id',
      `type` varchar(255) NOT NULL COMMENT '消息类型',
      `content` varchar(255) NOT NULL COMMENT '消息内容',
      `businessId` varchar(36) DEFAULT NULL COMMENT '业务id',
      `read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
      `founderId` varchar(255) DEFAULT NULL,
      `founderName` varchar(255) DEFAULT NULL,
      `createTime` datetime DEFAULT NULL,
      `modifierId` varchar(255) DEFAULT NULL,
      `modifierName` varchar(255) DEFAULT NULL,
      `modifyTime` datetime DEFAULT NULL,
      `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
      PRIMARY KEY (`messageId`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';


    INSERT INTO `yt-mybatis`.`Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createTime`, `modifierId`, `modifierName`, `modifyTime`, `deleteFlag`) VALUES ('message_1', 'user_1_admin', 'INVITE_GROUP', 'null邀请你加入到群组移民和闺蜜中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-22 05:06:53', 'user_2_student', '张三feng', '2019-01-23 02:19:10', 0);
    INSERT INTO `yt-mybatis`.`Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createTime`, `modifierId`, `modifierName`, `modifyTime`, `deleteFlag`) VALUES ('message_2', 'user_1_admin', 'INVITE_GROUP', '杨幂邀请你加入到群组uu中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-23 05:47:01', 'user_2_student', '张三feng', '2019-01-23 05:47:09', 0);
    INSERT INTO `yt-mybatis`.`Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createTime`, `modifierId`, `modifierName`, `modifyTime`, `deleteFlag`) VALUES ('message_3', 'user_2_student', 'INVITE_GROUP', '杨幂邀请你加入到群组uu中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-23 03:14:26', 'user_2_student', '张三feng', '2019-01-25 03:59:30', 0);
    INSERT INTO `yt-mybatis`.`Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createTime`, `modifierId`, `modifierName`, `modifyTime`, `deleteFlag`) VALUES ('message_4', 'user_2_student', 'INVITE_GROUP', 'null邀请你加入到群组xkxkxk中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-22 05:04:31', 'user_2_student', '张三feng', '2019-01-22 05:05:37', 0);
    INSERT INTO `yt-mybatis`.`Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createTime`, `modifierId`, `modifierName`, `modifyTime`, `deleteFlag`) VALUES ('message_5', 'user_2_student', 'INVITE_GROUP', '杨幂邀请你加入到群组bnnj中', 'business_1', 0, 'user_2_student', '张三feng', '2019-01-28 21:17:03', NULL, NULL, NULL, 0);

    ```
    
* ## 实现 po、entity、dao、service（可通过代码生成器生成）
    * ### domain，继承BaseEntity\<T\>
    ```java
    package demo.com.github.yt.message.domain;
    import com.github.yt.mybatis.entity.BaseEntity;
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
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createTime, t.modifyTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and (t.messageId = ?)
            //==> Parameters: message_1(String)
    
            // 1.2.根据条件查询一条记录
            // 查询 messageId为"message_1"的记录
            Message condition2 = new Message().setMessageId("message_1");
            Message message2 = messageService.find(condition2);
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createTime, t.modifyTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.messageId = ? limit 0, 2
            //==> Parameters: message_1(String)
    
            // 1.3.根据条件查询列表
            // 查询 消息状态为"已读"，并且userId为"user_1"的所有记录
            Message condition3 = new Message().setRead(true).setUserId("user_1");
            List<Message> messageList3 = messageService.findList(condition3);
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createTime, t.modifyTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.userId = ? and t.read = ?
            //==> Parameters: user_1(String), true(Boolean)
    
            // 1.4.根据条件查询分页
            Message condition4 = new Message().setRead(true);
            Query query4 = new Query().makePageNo(1).makePageSize(20);
            Page<Message> messagePage4 = messageService.findPage(condition4, query4);
            //==>  Preparing: select count(1) from Message t where 1=1 and t.read = ?
            //==> Parameters: true(Boolean)
            //==>  Preparing: select t.messageId, t.userId, t.type, t.content, t.businessId, t.read, t.createTime, t.modifyTime, t.founderId, t.founderName, t.modifierId, t.modifierName, t.deleteFlag from Message t where 1=1 and t.read = ? limit 0, 20
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














