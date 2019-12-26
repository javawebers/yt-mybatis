package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnum2Enum;
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
public class BaseServiceUpdateTests extends AbstractTestNGSpringContextTests {

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
    public void sameNull() {
        dbEntitySameService.update(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void sameNullId() {
        dbEntitySameService.update(new DbEntitySame());
    }

    @Test
    public void sameExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();

        int num = dbEntitySameService.update(new DbEntitySame()
                .setDbEntitySameId(list.get(0).getDbEntitySameId())
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222")
                .setTestEnum2(DbEntitySameTestEnum2Enum.OTHER));
        Assert.assertEquals(1, num);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        for (int i = 0; i < dbList.size(); i++) {
            DbEntitySame dbEntity = dbList.get(i);
            if (i == 0) {
                Assert.assertEquals(DbEntitySameTestEnum2Enum.OTHER, dbEntity.getTestEnum2());
                Assert.assertEquals("xxx_222", dbEntity.getTestVarchar());
                // 其他字段更新为 null
                Assert.assertNull(dbEntity.getTestBoolean());

                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getTestEnum2());
                Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getTestBoolean());

                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        }
    }

    @Test
    public void sameNotExist() {
        List<DbEntitySame> list = dataBasicService.save12Same();

        int num = dbEntitySameService.update(new DbEntitySame()
                .setDbEntitySameId("xxx_DbEntitySame")
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222")
                .setTestEnum2(DbEntitySameTestEnum2Enum.OTHER));
        Assert.assertEquals(0, num);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        for (DbEntitySame dbEntity : dbList) {
            Assert.assertNull(dbEntity.getTestEnum2());
            Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
            Assert.assertNotNull(dbEntity.getTestBoolean());

            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifierName());
            Assert.assertNull(dbEntity.getModifyTime());
        }
    }


    @Test
    public void sameWithColumn() {
        List<DbEntitySame> list = dataBasicService.save12Same();

        int num = dbEntitySameService.update(new DbEntitySame()
                .setDbEntitySameId(list.get(0).getDbEntitySameId())
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222")
                .setTestInt(22222)
                .setTestEnum2(DbEntitySameTestEnum2Enum.OTHER), "testVarchar", "testBoolean", "testEnum");
        Assert.assertEquals(1, num);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        for (int i = 0; i < dbList.size(); i++) {
            DbEntitySame dbEntity = dbList.get(i);
            if (i == 0) {
                Assert.assertNull(dbEntity.getTestEnum2());
                Assert.assertEquals("xxx_222", dbEntity.getTestVarchar());
                // 其他字段更新为 null
                Assert.assertNull(dbEntity.getTestBoolean());
                Assert.assertNull(dbEntity.getTestEnum());

                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNull(dbEntity.getTestEnum2());
                Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getTestBoolean());
                Assert.assertNotNull(dbEntity.getTestEnum());

                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifierName());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        }
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNull() {
        dbEntityNotSameService.update(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void notSameNullId() {
        dbEntityNotSameService.update(new DbEntityNotSame());
    }

    @Test
    public void notSameExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();

        int num = dbEntityNotSameService.update(new DbEntityNotSame()
                .setDbEntityNotSameId(list.get(0).getDbEntityNotSameId())
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222"));
        Assert.assertEquals(1, num);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        for (int i = 0; i < dbList.size(); i++) {
            DbEntityNotSame dbEntity = dbList.get(i);
            if (i == 0) {
                Assert.assertEquals("xxx_222", dbEntity.getTestVarchar());
                // 其他字段更新为 null
                Assert.assertNull(dbEntity.getTestBoolean());

                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getTestBoolean());

                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        }
    }

    @Test
    public void notSameNotExist() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();

        int num = dbEntityNotSameService.update(new DbEntityNotSame()
                .setDbEntityNotSameId("xxx_DbEntitySame")
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222"));
        Assert.assertEquals(0, num);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        for (DbEntityNotSame dbEntity : dbList) {
            Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
            Assert.assertNotNull(dbEntity.getTestBoolean());

            Assert.assertNull(dbEntity.getModifierId());
            Assert.assertNull(dbEntity.getModifyTime());
        }
    }


    @Test
    public void notSameWithColumn() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();

        int num = dbEntityNotSameService.update(new DbEntityNotSame()
                .setDbEntityNotSameId(list.get(0).getDbEntityNotSameId())
                .setDeleteFlag(false)
                .setTestVarchar("xxx_222")
                .setTestInt(22222), "test_varchar", "test_boolean");
        Assert.assertEquals(1, num);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        for (int i = 0; i < dbList.size(); i++) {
            DbEntityNotSame dbEntity = dbList.get(i);
            if (i == 0) {
                Assert.assertEquals("xxx_222", dbEntity.getTestVarchar());
                // 其他字段更新为 null
                Assert.assertNull(dbEntity.getTestBoolean());
                Assert.assertEquals(list.get(0).getTestInt(), dbEntity.getTestInt());

                Assert.assertNotNull(dbEntity.getModifierId());
                Assert.assertNotNull(dbEntity.getModifyTime());
            } else {
                Assert.assertNotEquals("xxx_222", dbEntity.getTestVarchar());
                Assert.assertNotNull(dbEntity.getTestBoolean());
                Assert.assertNotNull(dbEntity.getTestInt());

                Assert.assertNull(dbEntity.getModifierId());
                Assert.assertNull(dbEntity.getModifyTime());
            }
        }
    }

}
