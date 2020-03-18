package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceFindOneTests extends AbstractTestNGSpringContextTests {

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
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findOne(
                new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertNotNull(dbEntity);
    }

    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void sameNotExist() {
        dbEntitySameService.findOne(
                new DbEntitySame().setDbEntitySameId("sameNotExist_xxx"));
    }

    @Test(expectedExceptions = MyBatisSystemException.class)
    public void sameMultiResult() {
        dataBasicService.save12Same();
        dbEntitySameService.findOne(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE));
    }

    @Test
    public void sameMoreCondition() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findOne(entity);
        Assert.assertNotNull(dbEntity);
    }

    @Test
    public void sameQueryExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findOne(new DbEntitySame().setTestVarchar(entity.getTestVarchar()),
                new Query().addWhere("testVarchar = #{testVarchar}")
                        .addParam("testVarchar", entity.getTestVarchar())
                        .addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", entity.getTestEnum()));
        Assert.assertNotNull(dbEntity);

        dbEntity = dbEntitySameService.findOne(new DbEntitySame().setTestVarchar(entity.getTestVarchar()),
                new Query().equal("testVarchar", entity.getTestVarchar())
                        .equal("testEnum", entity.getTestEnum()));
        Assert.assertNotNull(dbEntity);
        dbEntity = dbEntitySameService.findOne(
                new Query().equal("testVarchar", entity.getTestVarchar()));
        Assert.assertNotNull(dbEntity);
    }

    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void sameQueryNotExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        dbEntitySameService.findOne(new DbEntitySame().setTestVarchar(entity.getTestVarchar()),
                new Query().addWhere("testVarchar = #{testVarchar}")
                        .addParam("testVarchar", entity.getTestVarchar() + "_sss")
                        .addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", entity.getTestEnum()));
    }


    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.findOne(
                new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertNotNull(dbEntity);
    }

    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void notSameNotExist() {
        dbEntityNotSameService.findOne(
                new DbEntityNotSame().setDbEntityNotSameId("notSameNotExist_xxx"));
    }

    @Test(expectedExceptions = MyBatisSystemException.class)
    public void notSameMultiResult() {
        dataBasicService.save12NotSame();
        dbEntityNotSameService.findOne(new DbEntityNotSame().setTestBoolean(true));
    }

    @Test
    public void notSameMoreCondition() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.findOne(entity);
        Assert.assertNotNull(dbEntity);
    }

    @Test
    public void notSameQueryExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.findOne(new DbEntityNotSame().setTestVarchar(entity.getTestVarchar()),
                new Query().addWhere("test_varchar = #{testVarchar}")
                        .addParam("testVarchar", entity.getTestVarchar()));
        Assert.assertNotNull(dbEntity);
    }

    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void notSameQueryNotExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        dbEntityNotSameService.findOne(new DbEntityNotSame().setTestVarchar(entity.getTestVarchar()),
                new Query().addWhere("test_varchar = #{testVarchar}")
                        .addParam("testVarchar", entity.getTestVarchar() + "_sss"));
    }


}
