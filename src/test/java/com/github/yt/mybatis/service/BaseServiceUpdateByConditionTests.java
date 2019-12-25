package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.service.DataBasicService;
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
public class BaseServiceUpdateByConditionTests extends AbstractTestNGSpringContextTests {

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
    public void sameUpdateBaseColumn() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.updateByCondition(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
                new Query().addUpdate("testInt = #{testInt}")
                        .addParam("testInt", 22222222));
        Assert.assertEquals(6, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestInt() == 22222222) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }

    @Test
    public void sameNotUpdateBaseColumn() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.updateByCondition(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
                new Query().updateBaseColumn(false).addUpdate("testInt = #{testInt}")
                        .addParam("testInt", 22222222));
        Assert.assertEquals(6, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifierName());
            Assert.assertNull(dbEntity.getModifyTime());
        });
    }

    @Test
    public void sameNotExist() {

    }

    @Test
    public void sameMoreCondition() {

    }

    @Test
    public void sameQueryExist() {

    }

    @Test
    public void sameQueryNotExist() {

    }

    @Test
    public void notSameExist() {

    }

    @Test
    public void notSameNotExist() {

    }

    @Test
    public void notSameMoreCondition() {

    }

    @Test
    public void notSameQueryExist() {

    }

    @Test
    public void notSameQueryNotExist() {

    }


}
