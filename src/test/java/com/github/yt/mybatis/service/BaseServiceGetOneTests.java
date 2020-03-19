package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceGetOneTests extends AbstractTestNGSpringContextTests {

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
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void sameNullId() {
        // null id
        dbEntitySameService.findOneById(null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void sameNotExist() {
        dbEntitySameService.findOneById("xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void sameExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findOneById(entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
    }

    @Test
    public void sameExist2() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.findOneById(entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
    }

    /**
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNullId() {
        // null id
        dbEntityNotSameService.findOneById(null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void notSameNotExist() {
        dbEntityNotSameService.findOneById("xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.findOneById(entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
    }

}
