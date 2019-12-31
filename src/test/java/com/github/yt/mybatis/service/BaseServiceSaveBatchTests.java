package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceSaveBatchTests extends AbstractTestNGSpringContextTests {

    @Resource
    private DataBasicService dataBasicService;

    @Resource
    private DbEntitySameService dbEntitySameService;
    @Resource
    private DbEntityNotSameService dbEntityNotSameService;

    @AfterMethod
    public void after() {
        dbEntitySameService.delete(new DbEntitySame());
        dbEntityNotSameService.delete(new DbEntityNotSame());
    }

    @Test
    public void sameIdNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> Assert.assertNotNull(entity.getDbEntitySameId()));
    }

    @Test
    public void sameDeleteFlagNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> Assert.assertEquals((Boolean) false, entity.getDeleteFlag()));
    }

    @Test
    public void sameAutoSetCreatorInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> {
            Assert.assertNotNull(entity.getCreateTime());
            Assert.assertNotNull(entity.getFounderId());
            Assert.assertNotNull(entity.getFounderName());
        });
    }

    @Test
    public void sameSetCreatorInfo() {
        List<DbEntitySame> list = new ArrayList<>();
        DbEntitySame entity1 = new DbEntitySame();
        DbEntitySame entity2 = new DbEntitySame();
        entity2.setFounderId("testId");
        entity2.setFounderName("testName");
        list.add(entity1);
        list.add(entity2);
        dbEntitySameService.saveBatch(list);

        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        Assert.assertNull(dbList.get(0).getTestBoolean());
        Assert.assertNull(dbList.get(0).getTestInt());
        Assert.assertNull(dbList.get(0).getTestVarchar());
        Assert.assertNull(dbList.get(0).getTestEnum());
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) false);

        Assert.assertNull(dbList.get(1).getTestBoolean());
        Assert.assertNull(dbList.get(1).getTestInt());
        Assert.assertNull(dbList.get(1).getTestVarchar());
        Assert.assertNull(dbList.get(1).getTestEnum());
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);
        Assert.assertEquals(dbList.get(1).getFounderId(), "testId");
        Assert.assertEquals(dbList.get(1).getFounderName(), "testName");

    }

    @Test
    public void sameNotSetUpdaterInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> {
            Assert.assertNull(entity.getModifyTime());
            Assert.assertNull(entity.getModifierId());
            Assert.assertNull(entity.getModifierName());
        });
    }

    @Test
    public void sameSavedValueSurefire() {
        List<DbEntitySame> list = new ArrayList<>();

        DbEntitySame entity1 = new DbEntitySame();
        entity1.setTestBoolean(true).setTestInt(1).setTestVarchar("1")
                .setTestEnum(DbEntitySameTestEnumEnum.FEMALE)
                .setDeleteFlag(true);

        DbEntitySame entity2 = new DbEntitySame();
        entity2.setTestBoolean(false).setTestInt(2).setTestVarchar("2")
                .setTestEnum(DbEntitySameTestEnumEnum.MALE)
                .setDeleteFlag(false);
        list.add(entity1);
        list.add(entity2);
        dbEntitySameService.saveBatch(list);

        List<DbEntitySame> dbList = dataBasicService.findSameList(list);

        Assert.assertEquals(dbList.get(0).getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbList.get(0).getTestInt(), (Integer) 1);
        Assert.assertEquals(dbList.get(0).getTestVarchar(), "1");
        Assert.assertEquals(dbList.get(0).getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) true);

        Assert.assertEquals(dbList.get(1).getTestBoolean(), (Boolean) false);
        Assert.assertEquals(dbList.get(1).getTestInt(), (Integer) 2);
        Assert.assertEquals(dbList.get(1).getTestVarchar(), "2");
        Assert.assertEquals(dbList.get(1).getTestEnum(), DbEntitySameTestEnumEnum.MALE);
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);

    }

    @Test
    public void sameSavedNullValue() {
        List<DbEntitySame> list = new ArrayList<>();
        DbEntitySame entity1 = new DbEntitySame();
        DbEntitySame entity2 = new DbEntitySame();
        entity2.setTestBoolean(true).setTestInt(2).setTestVarchar("2");
        list.add(entity1);
        list.add(entity2);
        dbEntitySameService.saveBatch(list);

        List<DbEntitySame> dbList = dataBasicService.findSameList(list);

        Assert.assertNull(dbList.get(0).getTestBoolean());
        Assert.assertNull(dbList.get(0).getTestInt());
        Assert.assertNull(dbList.get(0).getTestVarchar());
        Assert.assertNull(dbList.get(0).getTestEnum());
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) false);

        Assert.assertEquals(dbList.get(1).getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbList.get(1).getTestInt(), (Integer) 2);
        Assert.assertEquals(dbList.get(1).getTestVarchar(), "2");
        Assert.assertNull(dbList.get(1).getTestEnum());
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);

    }

    @Test
    public void sameExistId() {
        String sameExistId = "0000000000000000000000_001";
        List<DbEntitySame> list = new ArrayList<>();
        DbEntitySame entity1 = new DbEntitySame().setDbEntitySameId(sameExistId);
        DbEntitySame entity2 = new DbEntitySame();
        list.add(entity1);
        list.add(entity2);
        dbEntitySameService.saveBatch(list);
        List<DbEntitySame> dbList = dataBasicService.findSameList(list);
        Assert.assertEquals(dbList.get(0).getDbEntitySameId(), sameExistId);
        Assert.assertNotNull(dbList.get(1).getDbEntitySameId());
    }

    //

    @Test
    public void notSameIdNotNull() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        list.forEach(entity -> Assert.assertNotNull(entity.getDbEntityNotSameId()));
    }

    @Test
    public void notSameDeleteFlagNotNull() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        list.forEach(entity -> Assert.assertEquals((Boolean) false, entity.getDeleteFlag()));
    }

    @Test
    public void notSameAutoSetCreatorInfo() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        list.forEach(entity -> {
            Assert.assertNotNull(entity.getCreateTime());
            Assert.assertNotNull(entity.getFounderId());
        });
    }

    @Test
    public void notSameSetCreatorInfo() {
        List<DbEntityNotSame> list = new ArrayList<>();
        DbEntityNotSame entity1 = new DbEntityNotSame();
        DbEntityNotSame entity2 = new DbEntityNotSame();
        entity2.setFounderId("testId");
        list.add(entity1);
        list.add(entity2);
        dbEntityNotSameService.saveBatch(list);

        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        Assert.assertNull(dbList.get(0).getTestBoolean());
        Assert.assertNull(dbList.get(0).getTestInt());
        Assert.assertNull(dbList.get(0).getTestVarchar());
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) false);

        Assert.assertNull(dbList.get(1).getTestBoolean());
        Assert.assertNull(dbList.get(1).getTestInt());
        Assert.assertNull(dbList.get(1).getTestVarchar());
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);
        Assert.assertEquals(dbList.get(1).getFounderId(), "testId");

    }

    @Test
    public void notSameNotSetUpdaterInfo() {
        List<DbEntityNotSame> list = dataBasicService.save12NotSame();
        list.forEach(entity -> {
            Assert.assertNull(entity.getModifyTime());
            Assert.assertNull(entity.getModifierId());
        });
    }

    @Test
    public void notSameSavedValueSurefire() {
        List<DbEntityNotSame> list = new ArrayList<>();

        DbEntityNotSame entity1 = new DbEntityNotSame();
        entity1.setTestBoolean(true).setTestInt(1).setTestVarchar("1")
                .setDeleteFlag(true);

        DbEntityNotSame entity2 = new DbEntityNotSame();
        entity2.setTestBoolean(false).setTestInt(2).setTestVarchar("2")
                .setDeleteFlag(false);
        list.add(entity1);
        list.add(entity2);
        dbEntityNotSameService.saveBatch(list);

        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);

        Assert.assertEquals(dbList.get(0).getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbList.get(0).getTestInt(), (Integer) 1);
        Assert.assertEquals(dbList.get(0).getTestVarchar(), "1");
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) true);

        Assert.assertEquals(dbList.get(1).getTestBoolean(), (Boolean) false);
        Assert.assertEquals(dbList.get(1).getTestInt(), (Integer) 2);
        Assert.assertEquals(dbList.get(1).getTestVarchar(), "2");
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);
    }

    @Test
    public void notSameSavedNullValue() {
        List<DbEntityNotSame> list = new ArrayList<>();
        DbEntityNotSame entity1 = new DbEntityNotSame();
        DbEntityNotSame entity2 = new DbEntityNotSame();
        entity2.setTestBoolean(true).setTestInt(2).setTestVarchar("2");
        list.add(entity1);
        list.add(entity2);
        dbEntityNotSameService.saveBatch(list);

        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);

        Assert.assertNull(dbList.get(0).getTestBoolean());
        Assert.assertNull(dbList.get(0).getTestInt());
        Assert.assertNull(dbList.get(0).getTestVarchar());
        Assert.assertEquals(dbList.get(0).getDeleteFlag(), (Boolean) false);

        Assert.assertEquals(dbList.get(1).getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbList.get(1).getTestInt(), (Integer) 2);
        Assert.assertEquals(dbList.get(1).getTestVarchar(), "2");
        Assert.assertEquals(dbList.get(1).getDeleteFlag(), (Boolean) false);
    }

    @Test
    public void notSameExistId() {
        String sameExistId = "00000000000000000000_001";
        List<DbEntityNotSame> list = new ArrayList<>();
        DbEntityNotSame entity1 = new DbEntityNotSame().setDbEntityNotSameId(sameExistId);
        DbEntityNotSame entity2 = new DbEntityNotSame();
        list.add(entity1);
        list.add(entity2);
        dbEntityNotSameService.saveBatch(list);
        List<DbEntityNotSame> dbList = dataBasicService.findNotSameList(list);
        Assert.assertEquals(dbList.get(0).getDbEntityNotSameId(), sameExistId);
        Assert.assertNotNull(dbList.get(1).getDbEntityNotSameId());
    }


}
