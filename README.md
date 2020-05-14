# 简介
yt-mybatis是基于spring boot、mybaits封装的通用CURD框架。支持无xml复杂查询。  
如果是新项目，建议使用整体解决方案；如果是历史项目，您可以很快集成CURD。

# 为什么使用 yt-mybatis
通用CURD，使后端开发效率提升 3 倍，支持无xml复杂查询，代码更加简洁、异常控制等更加完善。

# 特性
* #### 支持 mybatis 一键接入通用增删改查，无需写 xml 即可实现通过 mybatis 操作数据库
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
* ## 创建 mysql 表，创建测试数据
```sql
create database if not exists `yt-mybatis`;
use `yt-mybatis`;
CREATE TABLE  if not exists `mysql_example` (
  `example_id` varchar(36) NOT NULL COMMENT 'id',
  `test_varchar` varchar(36) DEFAULT NULL COMMENT 'String类型',
  `test_int` int(11) DEFAULT NULL COMMENT 'int类型',
  `test_boolean` tinyint(1) DEFAULT NULL COMMENT 'boolean类型',
  `test_Enum` enum('MALE','FEMALE','OTHER') DEFAULT NULL COMMENT 'enum 类型，MALE:男 ，FEMALE:女，OTHER:其他',
  `founder_id` varchar(255) DEFAULT NULL,
  `founder_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modifier_id` varchar(255) DEFAULT NULL,
  `modifier_name` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`example_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='示例';
-- insert测试数据

```
* ## 定义 BaseEntity，代码生成时指定 （可选）
```java
package com.github.yt.mybatis.example.entity;

import com.github.yt.mybatis.entity.YtBaseEntityColumn;
import com.github.yt.mybatis.entity.YtColumnType;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table
public class BaseEntity<T extends BaseEntity<T>> implements Serializable {

    private static final long serialVersionUID = 6468926052770326422L;

    // 创建时间
    @Column(name = "create_time")
    @YtBaseEntityColumn(YtColumnType.CREATE_TIME)
    private Date createTime;

    // 修改时间
    @Column(name = "modify_time")
    @YtBaseEntityColumn(YtColumnType.MODIFY_TIME)
    private Date modifyTime;

    // 创建人ID
    @Column(name = "founder_id")
    @YtBaseEntityColumn(YtColumnType.FOUNDER_ID)
    private String founderId;

    // 创建人名称
    @Column(name = "founder_name")
    @YtBaseEntityColumn(YtColumnType.FOUNDER_NAME)
    private String founderName;

    // 修改人ID
    @Column(name = "modifier_id")
    @YtBaseEntityColumn(YtColumnType.MODIFIER_ID)
    private String modifierId;

    // 修改人名称
    @Column(name = "modifier_name")
    @YtBaseEntityColumn(YtColumnType.MODIFIER_ID)
    private String modifierName;

    // 删除标示
    @Column(name = "delete_flag", nullable = false)
    @YtBaseEntityColumn(YtColumnType.DELETE_FLAG)
    private Boolean deleteFlag;

    public String getFounderName() {
        return founderName;
    }

    public T setFounderName(String founderName) {
        this.founderName = founderName;
        return (T) this;
    }

    public String getModifierName() {
        return modifierName;
    }

    public T setModifierName(String modifierName) {
        this.modifierName = modifierName;
        return (T) this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public T setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public T setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return (T) this;
    }

    public String getFounderId() {
        return founderId;
    }

    public T setFounderId(String founderId) {
        this.founderId = founderId;
        return (T) this;
    }

    public String getModifierId() {
        return modifierId;
    }

    public T setModifierId(String modifierId) {
        this.modifierId = modifierId;
        return (T) this;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public T setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
        return (T) this;
    }
}

```
* ## 生成代码 po、entity、dao、service
```java
package com.github.yt.mybatis.generator;

import com.github.yt.mybatis.example.entity.BaseEntity;

public class ExampleCodeGeneratorExample {

    public static void main(String[] args) {
        createJavaCode();
    }

    private static void createJavaCode() {
        JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(
                "root",
                "root",
                "yt-mybatis",
                "jdbc:mysql://localhost:3306/yt-mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        javaCodeGenerator.create("mysql_example", "", "com.github.yt.mybatis.example",
                JavaCodeGenerator.CodePath.SRC_TEST,
                BaseEntity.class
                ,JavaCodeGenerator.TemplateEnum.PO
                ,JavaCodeGenerator.TemplateEnum.BEAN
                ,JavaCodeGenerator.TemplateEnum.MAPPER
//                , JavaCodeGenerator.TemplateEnum.MAPPER_XML
                ,JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }
}
```
  
* #### PO：对应数据库的实体类，时刻和数据库保持一致，不可手动修改，每次代码生成
```java
package com.github.yt.mybatis.example.po;

import javax.persistence.Table;
import javax.persistence.Column;

import com.github.yt.mybatis.example.entity.BaseEntity;

@Table(name = "mysql_example")
public class MysqlExamplePO<T extends MysqlExamplePO<T>> extends BaseEntity<T> {

    /** 
     * id  
     */
    @javax.persistence.Id
    @Column(name = "example_id")
    private String exampleId;
    /** 
     * String类型  
     */
    @Column(name = "test_varchar")
    private String testVarchar;
    /** 
     * int类型  
     */
    @Column(name = "test_int")
    private Integer testInt;
    /** 
     * boolean类型  
     */
    @Column(name = "test_boolean")
    private Boolean testBoolean;
    /** 
     * enum 类型，MALE:男 ，FEMALE:女，OTHER:其他  
     */
    @Column(name = "test_Enum")
    private MysqlExampleTestEnumEnum testEnum;

    public String getExampleId() {
        return this.exampleId;
    }

    public T setExampleId(String exampleId) {
        this.exampleId = exampleId;
        return (T) this;
    }

    public String getTestVarchar() {
        return this.testVarchar;
    }

    public T setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
        return (T) this;
    }

    public Integer getTestInt() {
        return this.testInt;
    }

    public T setTestInt(Integer testInt) {
        this.testInt = testInt;
        return (T) this;
    }

    public Boolean getTestBoolean() {
        return this.testBoolean;
    }

    public T setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
        return (T) this;
    }

    public MysqlExampleTestEnumEnum getTestEnum() {
        return this.testEnum;
    }

    public T setTestEnum(MysqlExampleTestEnumEnum testEnum) {
        this.testEnum = testEnum;
        return (T) this;
    }
}
```
  
* #### entity：PO 的扩展类，针对此类编程。用于查询时的扩展字段等
```java
package com.github.yt.mybatis.example.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.example.entity.BaseEntity;
import com.github.yt.mybatis.example.po.MysqlExamplePO;

/**
* PO 类的扩展类
*/
@Table(name = "mysql_example")
public class MysqlExample extends MysqlExamplePO<MysqlExample> {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解

}
```
  
* #### dao： 继承BaseMapper\<T\>（空类，无需任何实现）
```java
package com.github.yt.mybatis.example.dao;

import com.github.yt.mybatis.example.entity.MysqlExample;
import com.github.yt.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MysqlExample Mapper
 *
 */
@Mapper
@Repository
public interface MysqlExampleMapper extends BaseMapper<MysqlExample> {

}
```
    
* #### service 继承BaseService\<T\>（空类，无需任何实现）
```java
package com.github.yt.mybatis.example.service;

import com.github.yt.mybatis.service.IBaseService;
import com.github.yt.mybatis.example.dao.MysqlExampleMapper;
import com.github.yt.mybatis.example.entity.MysqlExample;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 示例服务层
 *
 */
public interface MysqlExampleService extends IBaseService<MysqlExample> {

}
```

```java
package com.github.yt.mybatis.example.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.example.dao.MysqlExampleMapper;
import com.github.yt.mybatis.example.entity.MysqlExample;
import com.github.yt.mybatis.example.service.MysqlExampleService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mysqlExampleService")
@Transactional
public class MysqlExampleServiceImpl extends BaseService<MysqlExample> implements MysqlExampleService {

    @Resource
    private MysqlExampleMapper mapper;
}
```
    
* ## 功能示例
```java
package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.service.MysqlExampleService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import javax.annotation.Resource;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class MysqlExampleServiceTests extends AbstractTestNGSpringContextTests {

    @Resource
    private MysqlExampleService mysqlExampleService;
    
    // 测试内容
}
```
* ### insert （保存）
* ##### 保存一条记录，会自动设置 create_time, founder_id, founder_name 字段
```java
@Test
public void save() {
    MysqlExample mysqlExample = new MysqlExample();
    mysqlExampleService.save(mysqlExample);
}
```

* ##### 批量保存
```java
@Test
public void saveBatch() {
    MysqlExample mysqlExample1 = new MysqlExample();
    MysqlExample mysqlExample2 = new MysqlExample();
    mysqlExampleService.saveBatch(Arrays.asList(mysqlExample1, mysqlExample2));
}
```
###### 当主键为字符串类型，自动生成uuid的主键并设置到保存的对象中。

* ### update （修改）
* ##### 根据主键更新所选字段，或者所有字段，为空也更新
```java
@Test
public void updateById() {
    MysqlExample mysqlExample = new MysqlExample();
    // 设置值主键和要更新的值
    mysqlExample.setExampleId("1");
    mysqlExample.setTestInt(222);
    mysqlExample.setTestVarchar("varchar_222");

    // 更新所有的字段，为空也会更新
    mysqlExampleService.updateById(mysqlExample);

    // 更新指定的字段，为空也会更新，这里只更新 test_int
    mysqlExampleService.updateById(mysqlExample, "test_int");
}
```
* ##### 根据主键更新所选字段，或者所有字段，为空不更新
```java
@Test
public void updateForSelectiveById() {
    MysqlExample mysqlExample = new MysqlExample();
    // 设置值主键和要更新的值
    mysqlExample.setExampleId("1");
    mysqlExample.setTestInt(222);
    mysqlExample.setTestVarchar("varchar_222");

    // 更新所有不为空的字段，这里更新 test_int 和 test_varchar
    mysqlExampleService.updateForSelectiveById(mysqlExample);
    // 更新指定不为空的字段，这里只更新 test_int
    mysqlExampleService.updateForSelectiveById(mysqlExample, "test_int", "test_boolean");
}
```
* ##### 根据条件更新
```java
@Test
public void update() {
    // 更新条件
    MysqlExample condition = new MysqlExample();
    condition.setTestInt(222);
    condition.setTestVarchar("varchar_222");

    Query query = new Query();
    // 设置要更新的字段
    query.addUpdate("test_boolean = #{testBoolean}").addParam("testBoolean", true);
    // 将 test_int 为 222 , test_varchar 为 varchar_222 记录的 test_boolean 字段更新为 true
    mysqlExampleService.update(condition, query);
}
```
###### 根据条件更新可以指定复杂查询条件，条件设置方式和查询一样，详见查询的写法。

* ### delete （删除）
* ##### 根据主键删除一条记录
```java
@Test
public void deleteById() {
    mysqlExampleService.deleteById("1");
}
```

* ##### 根据主键删除一条记录，如果记录不存在抛出异常
```java
@Test
public void deleteOneById() {
    mysqlExampleService.deleteOneById("1");
}
```

* ##### 根据条件删除
```java
@Test
public void delete() {
    // 更新条件
    MysqlExample condition = new MysqlExample();
    condition.setTestInt(222);
    condition.setTestVarchar("varchar_222");

    Query query = new Query();
    // 设置要更新的字段
    query.addWhere("test_boolean = #{testBoolean}").addParam("testBoolean", true);
    // 将 test_int 为 222 , test_varchar 为 varchar_222 , test_boolean 为 true 的记录删除
    mysqlExampleService.delete(condition, query);
}
```

* ### logicDelete （逻辑删除）
###### PO对象中需要有 `@YtBaseEntityColumn(YtColumnType.DELETE_FLAG)` 注解的字段，该字段可以在 `BaseEntity` 中，字段为 Boolean 类型
* ##### 根据主键逻辑删除一条记录
```java
@Test
public void logicDeleteById() {
    mysqlExampleService.logicDeleteById("1");
}
```

* ##### 根据主键逻辑删除一条记录，如果记录不存在抛出异常
```java
@Test
public void logicDeleteOneById() {
    mysqlExampleService.logicDeleteOneById("1");
}
```

* ##### 根据条件逻辑删除
```java
@Test
public void logicDelete() {
    // 更新条件
    MysqlExample condition = new MysqlExample();
    condition.setTestInt(222);
    condition.setTestVarchar("varchar_222");

    Query query = new Query();
    // 设置条件
    query.addWhere("test_boolean = #{testBoolean}").addParam("testBoolean", true);
    // 将 test_int 为 222 , test_varchar 为 varchar_222 , test_boolean 为 true 的记录逻辑删除
    mysqlExampleService.logicDelete(condition, query);
}
```

* ### find （查询记录）
* ##### 根据主键查询一条记录，无记录返回 null
```java
@Test
public void findById() {
    mysqlExampleService.get("1");
}
```

* ##### 根据主键查询一条记录，如果记录不存在抛出异常
```java
@Test
public void findOneById() {
    mysqlExampleService.getOne("1");
}
```

* ##### 查询一条记录，无记录返回 null
```java
@Test
public void find() {
    MysqlExample condition = new MysqlExample();
    condition.setTestInt(222);
    condition.setTestVarchar("varchar_222");

    Query query = new Query();
    // 设置条件
    query.addWhere("test_boolean = #{testBoolean}").addParam("testBoolean", true);
    // 查询 test_int 为 222 , test_varchar 为 varchar_222 , test_boolean 为 true 的记录
    // condition 是条件，不为空的字段之间进行 "and" 
    mysqlExampleService.find(condition, query);
}
```

* ##### 查询一条记录，如果记录不存在抛出异常，其他和 find 一致
```java
@Test
public void findOne() {
    mysqlExampleService.findOne(new MysqlExample());
}
```

* ##### 查询列表，使用方式和 find 一致，返回多条记录，无记录返回空集合
```java
@Test
public void findList() {
    mysqlExampleService.findList(new MysqlExample());
}
```

* ##### 分页查询，使用方式和 find 一致，返回分页数据
```java
@Test
public void findPage() {
    Query query = new Query();
    // 如果使用 yt-web 分页信息可通过 controller 注入，无需手动设置
    query.makePageNo(1);
    query.makePageSize(10);

    mysqlExampleService.findPage(new MysqlExample(), query);
}
```
* ### 扩展用法
* ##### in 查询
```java
@Test
public void findListIn() {
    Query query = new Query();
    query.addWhere("test_int in ${testIntList}");
    query.addParam("testIntList", Arrays.asList(1, 2, 3));
    // 查询 test_int 为 1，2，3 的记录
    mysqlExampleService.findList(new MysqlExample(), query);
}
```
```java
public void findListIn2() {
    Query query = new Query();
    // 可变参数
    query.in("test_int", 1, 2, 3);
    // 数组
    query.in("test_int", new int[]{1, 2, 3});
    // 集合
    query.in("test_int", Arrays.asList(1, 2, 3));
    mysqlExampleService.findList(new MysqlExample(), query);
}
```
* ##### 常用 where 条件
```java
public void findListCompare() {
    Query query = new Query();
    // 大于
    query.gt("test_int", 1);
    // 大于等于
    query.ge("test_int", 2);
    // 小于
    query.lt("test_int", 1);
    // 小于等于
    query.le("test_int", 1);
    // 等于
    query.equal("test_int", 1);
    mysqlExampleService.findList(new MysqlExample(), query);
}
```
* ##### or 查询
```java
public void findListOr() {
    Query query = new Query();
    query.addWhere("test_int = 1 or test_int = 2");
    mysqlExampleService.findList(new MysqlExample(), query);
}
```

* ##### join 查询，支持 JOIN、LEFT_JOIN、RIGHT_JOIN
```java
@Test
public void findListJoin() {
    Query query = new Query();
    query.addJoin(QueryJoinType.JOIN, "mysql_example t2 on t1.example_id = t2.example_id");
    mysqlExampleService.findList(new MysqlExample(), query);
}
```
* ##### limit
```java
@Test
public void findListLimit() {
    Query query = new Query();
    // from:开始条数，包含，从 0 开始
    query.limit(0, 10);
    mysqlExampleService.findList(new MysqlExample(), query);
}
```

* ##### groupBy、排除所有默认字段、扩展查询字段
###### 扩展实体类
```java
@Table(name = "mysql_example")
public class MysqlExample extends MysqlExamplePO<MysqlExample> {
    // 扩展字段，一对一对象等。字段上加 @Transient 注解
    @Transient
    private int countNum;

    public int getCountNum() {
        return countNum;
    }
    public MysqlExample setCountNum(int countNum) {
        this.countNum = countNum;
        return this;
    }
}
```
###### 执行查询
```java
@Test
public void findListExtendSelectColumn() {
    Query query = new Query();
    // 排除所有主表中的字段
    query.excludeAllSelectColumn();
    // 扩展分组字段、test_int
    query.addExtendSelectColumn("test_int as testInt, count(*) as countNum");
    // 分组查询
    query.addGroupBy("test_int");
    mysqlExampleService.findList(new MysqlExample(), query);
}
```

* ##### orderBy 排序查询
```java
@Test
public void findListOrderBy() {
    Query query = new Query();
    query.addOrderBy("test_int asc");
    mysqlExampleService.findList(new MysqlExample(), query);
}
```

* ##### 排除查询字段。本次查询没有用到的字段 如：longtext，或者敏感字段
```java
public void findListExcludeSelectColumn() {
    Query query = new Query();
    query.addExcludeSelectColumn("test_int");
    mysqlExampleService.findList(new MysqlExample(), query);
}
```
* ##### updateBaseColumn 是否更新基础字段。默认更新 modify_time、modifier_id、modifier_name
###### 实现获取默认值的方式
```java
package com.github.yt.mybatis.example.entity;

import com.github.yt.mybatis.entity.BaseEntityValue;

/**
 * entity 操作人信息默认实现
 * @author liujiasheng
 */
public class BusinessBaseEntityValue implements BaseEntityValue {
    // TODO 业务实现，可从登录信息中获取真实的用户信息。下面示例写死
    @Override
    public String getFounderId() {
        return "founderId222";
    }

    @Override
    public String getFounderName() {
        return "founderName222";
    }

    @Override
    public String getModifierId() {
        return "ModifierId222";
    }

    @Override
    public String getModifierName() {
        return null;
    }
}

```
###### 添加全局配置 application.properties
```properties
yt.entity.baseEntityValue=com.github.yt.mybatis.example.entity.BusinessBaseEntityValue
```
###### 更新
```java
@Test
public void updateBaseColumn() {
    // 更新条件
    MysqlExample condition = new MysqlExample();
    condition.setTestInt(222);
    condition.setTestVarchar("varchar_222");

    Query query = new Query();
    // 不更新修改时间、修改人、修改人名称字段
    query.updateBaseColumn(false);
    // 设置要更新的字段
    query.addUpdate("test_boolean = #{testBoolean}").addParam("testBoolean", true);
    // 将 test_int 为 222 , test_varchar 为 varchar_222 记录的 test_boolean 字段更新为 true
    mysqlExampleService.updateByCondition(condition, query);
}
```


* ## 配置 application.properties

* ### 数据库方言 
###### 目前支持 Mysql\Postgres\Oracle，默认为 Mysql。方言的实现都在 com.github.yt.mybatis.dialect.impl 下
```properties
yt.mybatis.dialect=com.github.yt.mybatis.dialect.impl.MysqlDialect
```

* ### insert、update、logicDelete 操作自动设置操作人信息
###### 默认时间返回 null，可扩展实现，根据业务进行实现，实现 `BaseEntityValue` 接口，从 ThreadLocal 中获取或者 从 request 中获取
```properties
yt.entity.baseEntityValue=com.github.yt.mybatis.entity.DefaultBaseEntityValue
```

* ### 设置分页字段信息
###### 分页 `Page<T>` 实际上是个 map ，序列化给前端可通过修改如下配置修改默认字段信息
```properties
yt.page.pageNoName=pageNo
yt.page.pageSizeName=pageSize
yt.page.pageDataName=data
yt.page.pageTotalCountName=totalCount
```
###### 默认示例
```json
{
    "pageNo": 1,
    "pageSize": 10,
    "totalCount": 107,
    "data": []
}
```

