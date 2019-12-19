package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
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

    @Test
    public void saveSameIdNotNull() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNotNull(entity.getDbEntitySameId());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void saveSameDeleteFlagNotNull() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertEquals((Boolean) false, entity.getDeleteFlag());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void saveSameAutoSetCreatorInfo() {
        DbEntitySame entity = dataBasicService.saveOneSame();
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(entity.getFounderId());
        Assert.assertNotNull(entity.getFounderName());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void saveSameNotSetUpdaterInfo() {
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
    public void saveSameSavedValueSurefire() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22")
                .setTestEnum(DbEntitySameTestEnumEnum.FEMALE).setDeleteFlag(true);
        dbEntitySameService.save(entity);

        DbEntitySame dbEntity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) true);
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void saveSameSavedNullValue() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);

        DbEntitySame dbEntity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertNull(dbEntity.getTestEnum());
        dataBasicService.deleteSame(entity);
    }

    @Test
    public void saveSameExistId() {
        String sameExistId = "sameExistId_001";
        DbEntitySame entity = new DbEntitySame().setDbEntitySameId(sameExistId);
        dbEntitySameService.save(entity);
        DbEntitySame dbEntity = dbEntitySameService.get(DbEntitySame.class, sameExistId);
        Assert.assertEquals(sameExistId, dbEntity.getDbEntitySameId());
        dataBasicService.deleteSame(entity);
    }

    // 下面 not same

    @Test
    public void saveNotSameIdNotNull() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNotNull(entity.getDbEntityNotSameId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameDeleteFlagNotNull() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertEquals((Boolean) false, entity.getDeleteFlag());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameAutoSetCreatorInfo() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNotNull(entity.getCreateTime());
        Assert.assertNotNull(entity.getFounderId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameNotSetUpdaterInfo() {
        DbEntityNotSame entity = dataBasicService.saveOneNotSame();
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameSavedValueSurefire() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22").setDeleteFlag(true);
        dbEntityNotSameService.save(entity);

        DbEntityNotSame dbEntity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) true);
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameSavedNullValue() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);

        DbEntityNotSame dbEntity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestVarchar());
        dataBasicService.deleteNotSame(entity);
    }

    @Test
    public void saveNotSameExistId() {
        String sameExistId = "notSameExistId_001";
        DbEntityNotSame entity = new DbEntityNotSame().setDbEntityNotSameId(sameExistId);
        dbEntityNotSameService.save(entity);
        DbEntityNotSame dbEntity = dbEntityNotSameService.get(DbEntityNotSame.class, sameExistId);
        Assert.assertEquals(sameExistId, dbEntity.getDbEntityNotSameId());
        dataBasicService.deleteNotSame(entity);
    }
}
