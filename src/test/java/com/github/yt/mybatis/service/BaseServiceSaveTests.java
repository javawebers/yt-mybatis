package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceSaveTests extends AbstractTestNGSpringContextTests {

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
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNotNull(entity.getDbEntitySameId());
        dataBasicService.deleteSame(entity);
    }

    @Test(expectedExceptions = DuplicateKeyException.class)
    public void sameIdAlreadyExist() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        dbEntitySameService.save(entity);
    }

    @Test
    public void sameDeleteFlagNotNull() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertEquals((Boolean) false, entity.getDeleteFlag());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void sameAutoSetCreatorInfo() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(entity.getFounderId());
        Assert.assertNotNull(entity.getFounderName());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void sameSetCreatorInfo() {
        DbEntitySame entity = new DbEntitySame();
        entity.setFounderId("testId");
        entity.setFounderName("testName");
        dbEntitySameService.save(entity);
        DbEntitySame dbEntity = dbEntitySameService.findById(entity.getDbEntitySameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertNull(dbEntity.getTestEnum());
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) false);
        Assert.assertEquals(dbEntity.getFounderId(), "testId");
        Assert.assertEquals(dbEntity.getFounderName(), "testName");
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void sameNotSetUpdaterInfo() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        Assert.assertNull(entity.getModifierName());
        dataBasicService.deleteSame(entity);
    }

    /**
     * 保存后再取出保证值是正确的
     */
    @Test
    public void sameSavedValueSurefire() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22")
                .setTestEnum(DbEntitySameTestEnumEnum.FEMALE).setDeleteFlag(true);
        dbEntitySameService.save(entity);

        DbEntitySame dbEntity = dbEntitySameService.findById(entity.getDbEntitySameId());
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) true);
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void sameSavedNullValue() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);

        DbEntitySame dbEntity = dbEntitySameService.findById(entity.getDbEntitySameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertNull(dbEntity.getTestEnum());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void sameExistId() {
        String sameExistId = "sameExistId_001";
        DbEntitySame entity = new DbEntitySame().setDbEntitySameId(sameExistId);
        dbEntitySameService.save(entity);
        DbEntitySame dbEntity = dbEntitySameService.findById(sameExistId);
        Assert.assertEquals(sameExistId, dbEntity.getDbEntitySameId());
        dataBasicService.deleteSame(entity);
    }

    // 下面 not same

    @Test
    public void notSameIdNotNull() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNotNull(entity.getDbEntityNotSameId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test(expectedExceptions = DuplicateKeyException.class)
    public void notSameIdAlreadyExist() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        dbEntityNotSameService.save(entity);
    }

    @Test
    public void notSameDeleteFlagNotNull() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertEquals((Boolean) false, entity.getDeleteFlag());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameAutoSetCreatorInfo() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(entity.getFounderId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameSetCreatorInfo() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setFounderId("testId");
        dbEntityNotSameService.save(entity);
        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) false);
        Assert.assertEquals(dbEntity.getFounderId(), "testId");
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameNotSetUpdaterInfo() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameSavedValueSurefire() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22").setDeleteFlag(true);
        dbEntityNotSameService.save(entity);

        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) true);
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameSavedNullValue() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);

        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void notSameExistId() {
        String sameExistId = "notSameExistId_001";
        DbEntityNotSame entity = new DbEntityNotSame().setDbEntityNotSameId(sameExistId);
        dbEntityNotSameService.save(entity);
        DbEntityNotSame dbEntity = dbEntityNotSameService.findById(sameExistId);
        Assert.assertEquals(sameExistId, dbEntity.getDbEntityNotSameId());
        dataBasicService.deleteNotSame(entity);
    }
}
