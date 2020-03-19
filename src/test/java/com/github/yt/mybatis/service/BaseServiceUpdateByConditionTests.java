package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnum2Enum;
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
        int count = dbEntitySameService.update(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
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
        int count = dbEntitySameService.update(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
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

    /**
     * 没有指定更新字段默认更新 base 字段
     */
    @Test
    public void sameDefaultUpdateBaseColumn() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.update(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
                new Query());
        Assert.assertEquals(6, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE));
        Assert.assertEquals(6, dbCount);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestEnum() == DbEntitySameTestEnumEnum.FEMALE) {
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
    public void sameMultiColumn() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.update(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
                new Query().addUpdate("testInt = #{testInt}, testEnum2 = #{testEnum2}")
                        .addUpdate("testVarchar = 'xxx333'")
                        .addParam("testInt", 22222222)
                        .addParam("testEnum2", DbEntitySameTestEnum2Enum.OTHER));
        Assert.assertEquals(6, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestInt() == 22222222) {
                Assert.assertEquals(DbEntitySameTestEnum2Enum.OTHER, dbEntity.getTestEnum2());
                Assert.assertEquals("xxx333", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getTestEnum2());
                Assert.assertNotEquals("xxx333", dbEntity.getTestVarchar());
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }

    @Test
    public void sameWithQuery() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.update(new DbEntitySame().setTestEnum(DbEntitySameTestEnumEnum.FEMALE),
                new Query().addUpdate("testInt = #{testInt}")
                        .addParam("testInt", 22222222)
                        .addWhere("testInt = 0")
        );
        Assert.assertEquals(2, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestInt(22222222));
        Assert.assertEquals(2, dbCount);
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
    public void sameWithQuery2() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        int count = dbEntitySameService.update(
                new Query().addUpdate("testInt = #{testInt}")
                        .addParam("testInt", 22222222)
                        .addWhere("testInt = 0")
                        .equal("testEnum", DbEntitySameTestEnumEnum.FEMALE)
        );
        Assert.assertEquals(2, count);
        int dbCount = dbEntitySameService.count(new DbEntitySame().setTestInt(22222222));
        Assert.assertEquals(2, dbCount);
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
    public void notSameUpdateBaseColumn() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.update(new DbEntityNotSame().setTestBoolean(false),
                new Query().addUpdate("test_int = #{testInt}")
                        .addParam("testInt", 22222222));
        Assert.assertEquals(6, count);
        int dbCount = dbEntityNotSameService.count(new DbEntityNotSame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestInt() == 22222222) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }

    @Test
    public void notSameNotUpdateBaseColumn() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.update(new DbEntityNotSame().setTestBoolean(false),
                new Query().updateBaseColumn(false).addUpdate("test_int = #{testInt}")
                        .addParam("testInt", 22222222));
        Assert.assertEquals(6, count);
        int dbCount = dbEntityNotSameService.count(new DbEntityNotSame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifyTime());
        });
    }

    /**
     * 没有指定更新字段默认更新 base 字段
     */
    @Test
    public void notSameDefaultUpdateBaseColumn() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.update(new DbEntityNotSame().setTestBoolean(false),
                new Query());
        Assert.assertEquals(6, count);
        int dbCount = dbEntityNotSameService.count(new DbEntityNotSame().setTestBoolean(false));
        Assert.assertEquals(6, dbCount);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (!dbEntity.getTestBoolean()) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }

    @Test
    public void notSameMultiColumn() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.update(new DbEntityNotSame().setTestBoolean(false),
                new Query().addUpdate("test_int = #{testInt}")
                        .addUpdate("test_varchar = 'xxx333'")
                        .addParam("testInt", 22222222));
        Assert.assertEquals(6, count);
        int dbCount = dbEntityNotSameService.count(new DbEntityNotSame().setTestInt(22222222));
        Assert.assertEquals(6, dbCount);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestInt() == 22222222) {
                Assert.assertEquals("xxx333", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNotEquals("xxx333", dbEntity.getTestVarchar());
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }

    @Test
    public void notSameWithQuery() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.update(new DbEntityNotSame().setTestBoolean(false),
                new Query().addUpdate("test_int = #{testInt}")
                        .addParam("testInt", 22222222)
                        .addWhere("test_int = 0")
        );
        Assert.assertEquals(2, count);
        int dbCount = dbEntityNotSameService.count(new DbEntityNotSame().setTestInt(22222222));
        Assert.assertEquals(2, dbCount);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        dbList.forEach(dbEntity -> {
            if (dbEntity.getTestInt() == 22222222) {
                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        });
    }


}
