package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceQueryOtherTests extends AbstractTestNGSpringContextTests {

    @Resource
    private DataBasicService dataBasicService;

    @Resource
    DbEntitySameService dbEntitySameService;
    @Resource
    DbEntityNotSameService dbEntityNotSameService;

    @AfterMethod
    public void after() {
        dbEntitySameService.delete(new DbEntitySame());
        dbEntityNotSameService.delete(new DbEntityNotSame());
    }

    /**
     * entity 类中扩展字段，分组查询的 countNum 测试
     */
    @Test
    public void sameFindListExtendField() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().excludeAllSelectColumn()
                        .addExtendSelectColumn("testVarchar, count(testVarchar) as countNum")
                        .addGroupBy("testVarchar"));
        Assert.assertEquals(dbList.size(), 4);

        dbList.forEach(entity -> {
            Assert.assertEquals(entity.getCountNum(), (Integer) 3);
            Assert.assertNotNull(entity.getTestVarchar());

            Assert.assertNull(entity.getTestInt());
            Assert.assertNull(entity.getTestBoolean());
            Assert.assertNull(entity.getFounderId());
            Assert.assertNull(entity.getCreateTime());
        });
    }


    @Test
    public void sameFindListExcludeField() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addExcludeSelectColumn("testVarchar, testInt").addExcludeSelectColumn("testBoolean"));
        Assert.assertEquals(dbList.size(), 12);

        dbList.forEach(entity -> {
            Assert.assertNull(entity.getCountNum());
            Assert.assertNull(entity.getTestVarchar());
            Assert.assertNull(entity.getTestInt());
            Assert.assertNull(entity.getTestBoolean());

            Assert.assertNotNull(entity.getFounderId());
            Assert.assertNotNull(entity.getCreateTime());
            Assert.assertNotNull(entity.getTestEnum());
        });
    }


    /**
     * entity 类中扩展字段，分组查询的 countNum 测试
     */
    @Test
    public void notSameFindListExtendField() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().excludeAllSelectColumn()
                        .addExtendSelectColumn("test_varchar as testVarchar, count(test_varchar) as countNum")
                        .addGroupBy("test_varchar"));
        Assert.assertEquals(dbList.size(), 4);

        dbList.forEach(entity -> {
            Assert.assertEquals(entity.getCountNum(), (Integer) 3);
            Assert.assertNotNull(entity.getTestVarchar());

            Assert.assertNull(entity.getTestInt());
            Assert.assertNull(entity.getTestBoolean());
            Assert.assertNull(entity.getFounderId());
            Assert.assertNull(entity.getCreateTime());
        });
    }

    @Test
    public void notSameFindListExcludeField() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().addExcludeSelectColumn("test_varchar, test_int").addExcludeSelectColumn("test_boolean"));
        Assert.assertEquals(dbList.size(), 12);

        dbList.forEach(entity -> {
            Assert.assertNull(entity.getCountNum());
            Assert.assertNull(entity.getTestVarchar());
            Assert.assertNull(entity.getTestInt());
            Assert.assertNull(entity.getTestBoolean());

            Assert.assertNotNull(entity.getFounderId());
            Assert.assertNotNull(entity.getCreateTime());
        });
    }
}
