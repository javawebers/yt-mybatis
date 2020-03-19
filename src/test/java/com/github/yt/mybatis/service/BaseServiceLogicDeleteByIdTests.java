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
public class BaseServiceLogicDeleteByIdTests extends AbstractTestNGSpringContextTests {

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
    public void sameNotExist() {
        int num = dbEntitySameService.logicDeleteById("sameNotExist_xxx");
        Assert.assertEquals(0, num);
    }

    @Test
    public void sameExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNull(entity.getModifierId());
        Assert.assertNull(entity.getModifierName());
        Assert.assertNull(entity.getModifyTime());
        int num = dbEntitySameService.logicDeleteById(entity.getDbEntitySameId());
        Assert.assertEquals(1, num);
        DbEntitySame dbEntity = dbEntitySameService.findById(entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
        Assert.assertNotNull(dbEntity.getModifierId());
        Assert.assertNull(dbEntity.getModifierName());
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
    }

    @Test
    public void notSameNotExist() {
        int num = dbEntityNotSameService.logicDeleteById("sameNotExist_xxx");
        Assert.assertEquals(0, num);
    }

    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNull(entity.getModifierId());
        Assert.assertNull(entity.getModifyTime());
        int num = dbEntityNotSameService.logicDeleteById(entity.getDbEntityNotSameId());
        Assert.assertEquals(1, num);
        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
        Assert.assertNotNull(dbEntity.getModifierId());
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
    }

}
