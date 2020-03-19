package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.MysqlExample;
import com.github.yt.mybatis.example.service.MysqlExampleService;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.QueryJoinType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class MysqlExampleServiceTests extends AbstractTestNGSpringContextTests {

    @Resource
    private MysqlExampleService mysqlExampleService;

    @Test
    public void save() {
        MysqlExample mysqlExample = new MysqlExample();
        mysqlExampleService.save(mysqlExample);
    }

    @Test
    public void saveBatch() {
        MysqlExample mysqlExample1 = new MysqlExample();
        MysqlExample mysqlExample2 = new MysqlExample();
        mysqlExampleService.saveBatch(Arrays.asList(mysqlExample1, mysqlExample2));
    }

    // 根据主键更新所选字段，或者所有字段。为空也更新
    @Test
    public void update() {
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


    // 根据主键更新所选字段，或者所有字段。为空不更新
    @Test
    public void updateForSelective() {
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

    // 根据条件更新
    @Test
    public void updateByCondition() {
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

    // 根据主键删除一条记录
    @Test
    public void delete() {
        mysqlExampleService.deleteById("1");
    }

    // 根据主键删除一条记录，如果记录不存在抛出异常
    @Test
    public void deleteOne() {
        mysqlExampleService.deleteOneById("1");
    }

    // 根据条件删除
    @Test
    public void deleteByCondition() {
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

    // 逻辑删除一条记录
    @Test
    public void logicDelete() {
        mysqlExampleService.logicDeleteById("1");
    }

    // 逻辑删除一条记录，如果记录不存在抛出异常（记录已经被逻辑删除也会抛出异常）
    @Test
    public void logicDeleteOne() {
        mysqlExampleService.logicDeleteOneById("1");
    }

    // 根据条件逻辑删除
    @Test
    public void logicDeleteByCondition() {
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


    // 根据主键查询一条记录
    @Test
    public void get() {
        mysqlExampleService.findById("1");
    }

    // 根据主键查询一条记录，如果记录不存在抛出异常
    @Test
    public void getOne() {
        mysqlExampleService.findOneById("1");
    }

    // 查询一条记录
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

    // 查询一条记录，如果记录不存在抛出异常，其他和 find 一致
    @Test
    public void findOne() {
        mysqlExampleService.findOne(new MysqlExample());
    }

    // 查询列表，使用方式和 find 一致，返回多条记录
    @Test
    public void findList() {
        mysqlExampleService.findList(new MysqlExample());
    }


    // 分页查询，使用方式和 find 一致，返回分页数据
    @Test
    public void findPage() {
        Query query = new Query();
        // 如果使用 yt-web 分页信息可通过 controller 注入，无需手动设置
        query.makePageNo(1);
        query.makePageSize(10);

        mysqlExampleService.findPage(new MysqlExample(), query);
    }

    @Test
    public void findListIn() {
        Query query = new Query();
        query.addWhere("test_int in ${testIntList}");
        query.addParam("testIntList", Arrays.asList(1, 2, 3));
        // 查询 test_int 为 1，2，3 的记录
        mysqlExampleService.findList(new MysqlExample(), query);
    }

    // 常用 where 条件
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

    // or 查询
    public void findListOr() {
        Query query = new Query();
        query.addWhere("test_int = 1 or test_int = 2");
        mysqlExampleService.findList(new MysqlExample(), query);
    }

    @Test
    public void findListOrderBy() {
        Query query = new Query();
        query.addOrderBy("test_int asc");
        mysqlExampleService.findList(new MysqlExample(), query);
    }


    @Test
    public void findListJoin() {
        Query query = new Query();
        query.addJoin(QueryJoinType.JOIN, "mysql_example t2 on t1.example_id = t2.example_id");
        mysqlExampleService.findList(new MysqlExample(), query);
    }

    @Test
    public void findListLimit() {
        Query query = new Query();
        // from:开始条数，包含，从 0 开始
        query.limit(0, 10);
        mysqlExampleService.findList(new MysqlExample(), query);
    }

    // groupBy、排除所有默认字段、扩展查询字段
    @Test
    public void findListGroupBy() {
        Query query = new Query();
        // 排除所有主表中的字段
        query.excludeAllSelectColumn();
        // 扩展分组字段、test_int
        query.addExtendSelectColumn("test_int as testInt, count(*) as countNum");
        // 分组查询
        query.addGroupBy("test_int");
        mysqlExampleService.findList(new MysqlExample(), query);
    }

    // 排除查询字段，本次查询没有用到的字段 如：longtext，或者敏感字段
    @Test
    public void findListExcludeSelectColumn() {
        Query query = new Query();
        query.addExcludeSelectColumn("test_int");
        mysqlExampleService.findList(new MysqlExample(), query);
    }


    // updateBaseColumn 是否更新基础字段。默认更新 修改时间、修改人、修改人名称字段
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
        mysqlExampleService.update(condition, query);
    }


}
