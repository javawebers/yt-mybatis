package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
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


    @Test
    public void saveBatchSameIdNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> Assert.assertNotNull(entity.getDbEntitySameId()));
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameDeleteFlagNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> Assert.assertEquals((Boolean) false, entity.getDeleteFlag()));
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameAutoSetCreatorInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> {
            Assert.assertNotNull(entity.getCreateTime());
            Assert.assertNotNull(entity.getFounderId());
            Assert.assertNotNull(entity.getFounderName());
        });
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameNotSetUpdaterInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        list.forEach(entity -> {
            Assert.assertNull(entity.getModifyTime());
            Assert.assertNull(entity.getModifierId());
            Assert.assertNull(entity.getModifierName());
        });
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameSavedValueSurefire() {
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

        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameSavedNullValue() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchSameExistId() {

    }

    //

    @Test
    public void saveBatchNotSameIdNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameDeleteFlagNotNull() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameAutoSetCreatorInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameNotSetUpdaterInfo() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameSavedValueSurefire() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameSavedNullValue() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        dataBasicService.deleteSame(list);
    }

    @Test
    public void saveBatchNotSameExistId() {

    }


}
