package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
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
public class BaseServiceLogicDeleteTests extends AbstractTestNGSpringContextTests {

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
        int count = dbEntitySameService.logicDelete(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE));
        Assert.assertEquals(6, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestEnum() == DbEntitySameTestEnumEnum.FEMALE) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void sameNotExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.logicDelete(new DbEntitySame().setDbEntitySameId("xxx"));
        Assert.assertEquals(0, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifierName());
            Assert.assertNull(dbEntity.getModifyTime());
            Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
        });
    }

    @Test
    public void sameMoreCondition() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.logicDelete(
                new DbEntitySame()
                        .setTestEnum(DbEntitySameTestEnumEnum.FEMALE).setTestBoolean(true));
        Assert.assertEquals(6, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestEnum() == DbEntitySameTestEnumEnum.FEMALE) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void sameQueryExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.logicDelete(new DbEntitySame(),
                new Query().addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", DbEntitySameTestEnumEnum.FEMALE));
        Assert.assertEquals(6, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestEnum() == DbEntitySameTestEnumEnum.FEMALE) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void sameQueryNotExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.logicDelete(new DbEntitySame(),
                new Query().addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", DbEntitySameTestEnumEnum.FEMALE)
                        .addWhere("testBoolean = #{testBoolean}")
                        .addParam("testBoolean", false));
        Assert.assertEquals(0, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifierName());
            Assert.assertNull(dbEntity.getModifyTime());
            Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
        });

    }
    @Test
    public void sameQueryNotExist2() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.logicDelete(
                new Query().addWhere("testEnum = #{testEnum}")
                        .addParam("testEnum", DbEntitySameTestEnumEnum.FEMALE)
                        .addWhere("testBoolean = #{testBoolean}")
                        .addParam("testBoolean", false));
        Assert.assertEquals(0, count);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifierName());
            Assert.assertNull(dbEntity.getModifyTime());
            Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
        });

    }


    @Test
    public void notSameExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.logicDelete(
                new DbEntityNotSame().setTestBoolean(true));
        Assert.assertEquals(6, count);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestBoolean()) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void notSameNotExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.logicDelete(new DbEntityNotSame().setDbEntityNotSameId("xxx"));
        Assert.assertEquals(0, count);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifyTime());
            Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
        });
    }

    @Test
    public void notSameMoreCondition() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.logicDelete(
                new DbEntityNotSame().setTestBoolean(true).setTestInt(0));
        Assert.assertEquals(2, count);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestBoolean() && dbEntity.getTestInt() == 0) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void notSameQueryExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.logicDelete(new DbEntityNotSame(),
                new Query().addWhere("test_boolean = #{testBoolean}")
                        .addParam("testBoolean", true));
        Assert.assertEquals(6, count);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestBoolean()) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

    @Test
    public void notSameQueryNotExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.logicDelete(new DbEntityNotSame(),
                new Query().addWhere("test_int = #{testInt}")
                        .addParam("testInt", 0)
                        .addWhere("test_boolean = #{testBoolean}")
                        .addParam("testBoolean", true));
        Assert.assertEquals(2, count);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestBoolean() && dbEntity.getTestInt() == 0) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) true, dbEntity.getDeleteFlag());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
                Assert.assertEquals((Boolean) false, dbEntity.getDeleteFlag());
            }
        });
    }

}
