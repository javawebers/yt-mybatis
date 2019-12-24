package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
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


    /**
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void sameNullId() {
        // null id
        dbEntitySameService.getOne(DbEntitySame.class, null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void sameNotExist() {
        dbEntitySameService.getOne(DbEntitySame.class, "xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void sameExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        DbEntitySame dbEntity = dbEntitySameService.getOne(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
        dataBasicService.deleteSame(dbEntity);
    }

    /**
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNullId() {
        // null id
        dbEntityNotSameService.getOne(DbEntityNotSame.class, null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void notSameNotExist() {
        dbEntityNotSameService.getOne(DbEntityNotSame.class, "xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void notSameExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        DbEntityNotSame dbEntity = dbEntityNotSameService.getOne(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
        dataBasicService.deleteNotSame(dbEntity);
    }

}
