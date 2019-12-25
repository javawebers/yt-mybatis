package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceFindListTests extends AbstractTestNGSpringContextTests {

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

    @Test
    public void sameExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE));
        Assert.assertEquals(6, dbList.size());
    }

    @Test
    public void sameNotExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame().setDbEntitySameId("xxx"));
        Assert.assertEquals(0, dbList.size());
    }

    @Test
    public void sameMoreCondition() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(
                new DbEntitySame()
                        .setTestEnum(DbEntitySameTestEnumEnum.FEMALE).setTestBoolean(true));
        Assert.assertEquals(6, dbList.size());
    }

    @Test
    public void sameQueryExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", DbEntitySameTestEnumEnum.FEMALE));
        Assert.assertEquals(6, dbList.size());
    }

    @Test
    public void sameQueryNotExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", DbEntitySameTestEnumEnum.FEMALE)
                        .addWhere("testBoolean = #{testBoolean}")
                        .addParam("testBoolean", false));
        Assert.assertEquals(0, dbList.size());
    }


    @Test
    public void notSameExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(
                new DbEntityNotSame().setTestBoolean(true));
        Assert.assertEquals(6, dbList.size());
    }

    @Test
    public void notSameNotExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame().setDbEntityNotSameId("xxx"));
        Assert.assertEquals(0, dbList.size());
    }

    @Test
    public void notSameMoreCondition() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(
                new DbEntityNotSame().setTestBoolean(true).setTestInt(0));
        Assert.assertEquals(2, dbList.size());
    }

    @Test
    public void notSameQueryExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().addWhere("test_boolean = #{testBoolean}")
                        .addParam("testBoolean", true));
        Assert.assertEquals(6, dbList.size());
    }

    @Test
    public void notSameQueryNotExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().addWhere("test_int = #{testInt}")
                        .addParam("testInt", 0)
                        .addWhere("test_boolean = #{testBoolean}")
                        .addParam("testBoolean", true));
        Assert.assertEquals(2, dbList.size());
    }

}
