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
public class BaseServiceGetTests extends AbstractTestNGSpringContextTests {

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
     * get id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void sameNullId() {
        // null id
        dbEntitySameService.findById(null);
    }

    /**
     * get id 不为空，记录不存在
     */
    @Test
    public void sameNotExist() {
        DbEntitySame entity = dbEntitySameService.findById("xxxx_null");
        Assert.assertNull(entity);
    }

    /**
     * get id 不为空，记录存在
     */
    @Test
    public void sameExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findById(entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
    }

    /**
     * get id 不为空，记录存在
     */
    @Test
    public void sameIdTypeNotSame() {
        // 隐式类型转换 mysql 支持，oracle、postgres、sqlserver 不支持
        DbEntitySame entity = dataBasicService.saveOneSame();
        entity.setDbEntitySameId("222");
        dbEntitySameService.save(entity);
        DbEntitySame dbEntity = dbEntitySameService.findById(222);
        Assert.assertNotNull(dbEntity);
    }

    /**
     * get id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNullId() {
        // null id
        dbEntityNotSameService.findById(null);
    }

    /**
     * get id 不为空，记录不存在
     */
    @Test
    public void notSameNotExist() {
        DbEntityNotSame entity = dbEntityNotSameService.findById("xxxx_null");
        Assert.assertNull(entity);
    }

    /**
     * get id 不为空，记录存在
     */
    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
    }


}
