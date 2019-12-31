package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceDeleteByIdTests extends AbstractTestNGSpringContextTests {

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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void sameNullId() {
        dbEntitySameService.delete(DbEntitySame.class, null);
    }

    @Test
    public void sameNotExist() {
        int num = dbEntitySameService.delete(DbEntitySame.class, "sameNotExist_xxx");
        Assert.assertEquals(0, num);
    }

    @Test
    public void sameExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        int num = dbEntitySameService.delete(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertEquals(1, num);
        DbEntitySame dbEntity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNull(dbEntity);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNullId() {
        dbEntityNotSameService.delete(DbEntityNotSame.class, null);
    }

    @Test
    public void notSameNotExist() {
        int num = dbEntityNotSameService.delete(DbEntityNotSame.class, "sameNotExist_xxx");
        Assert.assertEquals(0, num);
    }

    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        int num = dbEntityNotSameService.delete(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertEquals(1, num);
        DbEntityNotSame dbEntity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNull(dbEntity);
    }


}
