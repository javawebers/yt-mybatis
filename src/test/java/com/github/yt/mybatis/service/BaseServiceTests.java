package com.github.yt.mybatis.service;

import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.entity.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.entity.IntId;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import com.github.yt.mybatis.business.service.IntIdService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceTests extends AbstractTestNGSpringContextTests {

    @Resource
    DbEntitySameService dbEntitySameService;
    @Resource
    DbEntityNotSameService dbEntityNotSameService;
    @Resource
    IntIdService intIdService;

    @AfterClass
    public void after() {
        dbEntitySameService.delete(new DbEntitySame());
        dbEntityNotSameService.delete(new DbEntityNotSame());
        intIdService.delete(new IntId());
    }


    @Test
    public void save_same() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22").setTestEnum(DbEntitySameTestEnumEnum.FEMALE);
        dbEntitySameService.save(entity);
        // id 不为空
        Assert.assertNotNull(entity.getDbEntitySameId());
        // find 然后判断各个值
        DbEntitySame dbEntity = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean)true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean)false);
        //
        Assert.assertNotNull(dbEntity.getCreateTime());
        Assert.assertNotNull(dbEntity.getFounderId());
        Assert.assertNotNull(dbEntity.getFounderName());
        Assert.assertNull(dbEntity.getModifyTime());
        Assert.assertNull(dbEntity.getModifierId());
        Assert.assertNull(dbEntity.getModifierName());
    }

    @Test
    public void save_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22");
        dbEntityNotSameService.save(entity);
        // id 不为空
        Assert.assertNotNull(entity.getDbEntityNotSameId());
        // find 然后判断各个值
        DbEntityNotSame dbEntity = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean)false);
        Assert.assertNotNull(dbEntity.getCreateTime());
        Assert.assertNotNull(dbEntity.getFounderId());
        Assert.assertNull(dbEntity.getModifyTime());
        Assert.assertNull(dbEntity.getModifierId());
    }

    @Test
    public void saveBatch_same() {
        DbEntitySame entity1 = new DbEntitySame();
        DbEntitySame entity2 = new DbEntitySame();
        List<DbEntitySame> dbEntitySameList = Arrays.asList(entity1, entity2);
        dbEntitySameService.saveBatch(dbEntitySameList);
        // TODO 验证
    }

    @Test
    public void saveBatch_notSame() {
        DbEntityNotSame entity1 = new DbEntityNotSame();
        DbEntityNotSame entity2 = new DbEntityNotSame();
        List<DbEntityNotSame> dbEntitySameList = Arrays.asList(entity1, entity2);
        dbEntityNotSameService.saveBatch(dbEntitySameList);
        // TODO 验证
    }

    @Test
    public void update_same() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);
        entity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNull(entity.getTestInt());
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        dbEntitySameService.update(entity.setTestInt(222));
        // 验证
        DbEntitySame dbEntity = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 222);
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
    }

    @Test
    public void update_sameWithFields() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);
        entity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNull(entity.getTestInt());
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        // 指定字段更新
        dbEntitySameService.update(entity.setTestInt(233), "testInt");
        // 验证
        DbEntitySame dbEntity = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 233);
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
    }


    @Test
    public void update_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        entity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNull(entity.getTestInt());
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        dbEntityNotSameService.update(entity.setTestInt(222));
        // 验证
        DbEntityNotSame dbEntity = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 222);
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
    }

    @Test
    public void update_notSameWithFields() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        entity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNull(entity.getTestInt());
        Assert.assertNull(entity.getModifyTime());
        Assert.assertNull(entity.getModifierId());
        dbEntityNotSameService.update(entity.setTestInt(222), "test_int");
        // 验证
        DbEntityNotSame dbEntity = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 222);
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
    }

    @Test
    public void updateByCondition_same() {
        DbEntitySame entity1 = new DbEntitySame().setTestInt(222);
        DbEntitySame entity2 = new DbEntitySame().setTestInt(222);
        DbEntitySame entity3 = new DbEntitySame().setTestInt(233);
        List<DbEntitySame> entityList = Arrays.asList(entity1, entity2, entity3);
        dbEntitySameService.saveBatch(entityList);
        Assert.assertNull(entity1.getTestVarchar());
        Assert.assertNull(entity1.getModifyTime());
        Assert.assertNull(entity1.getModifierId());
        int num = dbEntitySameService.updateByCondition(new DbEntitySame().setTestInt(222), new Query().addUpdate("testVarchar = 'varchar222'"));
        // 验证
        Assert.assertEquals(num, 2);
        List<DbEntitySame> dbEntityList = dbEntitySameService.findList(new DbEntitySame().setTestInt(222));
        Assert.assertEquals(dbEntityList.size(), 2);
        Assert.assertEquals(dbEntityList.get(0).getTestVarchar(), "varchar222");
        Assert.assertNotNull(dbEntityList.get(0).getModifyTime());
        Assert.assertNotNull(dbEntityList.get(0).getModifierId());
    }

    @Test
    public void updateByCondition_notSame() {
        DbEntityNotSame entity1 = new DbEntityNotSame().setTestInt(222);
        DbEntityNotSame entity2 = new DbEntityNotSame().setTestInt(222);
        List<DbEntityNotSame> dbEntitySameList = Arrays.asList(entity1, entity2);
        dbEntityNotSameService.saveBatch(dbEntitySameList);
        Assert.assertNull(entity1.getTestVarchar());
        Assert.assertNull(entity1.getModifyTime());
        Assert.assertNull(entity1.getModifierId());
        int num = dbEntityNotSameService.updateByCondition(new DbEntityNotSame().setTestInt(222), new Query().addUpdate("test_varchar = 'varchar222'"));
        // 验证
        Assert.assertEquals(num, 2);
        List<DbEntityNotSame> dbEntityList = dbEntityNotSameService.findList(new DbEntityNotSame().setTestInt(222));
        Assert.assertEquals(dbEntityList.size(), 2);
        Assert.assertEquals(dbEntityList.get(0).getTestVarchar(), "varchar222");
        Assert.assertNotNull(dbEntityList.get(0).getModifyTime());
        Assert.assertNotNull(dbEntityList.get(0).getModifierId());
    }

    @Test
    public void delete_same() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22").setTestEnum(DbEntitySameTestEnumEnum.FEMALE);
        dbEntitySameService.save(entity);
        // id 不为空
        int num = dbEntitySameService.delete(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertEquals(num, 1);
        DbEntitySame dbEntitySame = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertNull(dbEntitySame);
    }

    @Test
    public void delete_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22");
        dbEntityNotSameService.save(entity);
        // id 不为空
        int num = dbEntityNotSameService.delete(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertEquals(num, 1);
        DbEntityNotSame dbEntitySame = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertNull(dbEntitySame);
    }

    @Test
    public void logicDelete_same() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);
        int num = dbEntitySameService.logicDelete(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        // 验证
        Assert.assertEquals(num, 1);
        DbEntitySame dbEntity = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
        Assert.assertNull(dbEntity.getModifierName());
        Assert.assertTrue(dbEntity.getDeleteFlag());
        dbEntity = dbEntitySameService.find(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()).setDeleteFlag(false));
        Assert.assertNull(dbEntity);
    }

    @Test
    public void logicDelete_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        int num = dbEntityNotSameService.logicDelete(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        // 验证
        Assert.assertEquals(num, 1);
        DbEntityNotSame dbEntity = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));
        Assert.assertNotNull(dbEntity.getModifyTime());
        Assert.assertNotNull(dbEntity.getModifierId());
        Assert.assertTrue(dbEntity.getDeleteFlag());
        dbEntity = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()).setDeleteFlag(false));
        Assert.assertNull(dbEntity);
    }

    /**
     * in 条件查询
     */
    @Test
    public void findList_in() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query()
                        .addWhere("testVarchar in ${testVarcharList}")
                        .addParam("testVarcharList", Arrays.asList("0", "1")));
        Assert.assertEquals(dbList.size(), 6);
        // 清理数据
        deleteSame(list);
    }

    /**
     * in 条件查询， in不在where中，在join中
     */
    @Test
    public void findList_inJoin() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addJoin(Query.JoinType.JOIN, "DbEntitySame t2 on t.dbEntitySameId = t2.dbEntitySameId and t2.testVarchar in ${testVarcharList}")
                        .addParam("testVarcharList", Arrays.asList("0", "1")));
        Assert.assertEquals(dbList.size(), 6);
        // 清理数据
        deleteSame(list);
    }

    private void deleteSame(List<DbEntitySame> list) {
        dbEntitySameService.delete(new DbEntitySame(),
                new Query().addWhere("dbEntitySameId in ${dbEntitySameIdList}")
                        .addParam("dbEntitySameIdList", list.stream().map(DbEntitySame::getDbEntitySameId).collect(Collectors.toList())));
    }

    /**
     * 创建12条记录
     *
     * @return
     */
    private List<DbEntitySame> save12SameThenReturn() {
        List<DbEntitySame> entityList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            DbEntitySame entity = new DbEntitySame();
            boolean testBoolean = (i % 2 == 0);
            int testInt = i % 3;
            String testVarchar = i % 4 + "";
            DbEntitySameTestEnumEnum testEnum = (i % 2 == 0) ? DbEntitySameTestEnumEnum.FEMALE : DbEntitySameTestEnumEnum.MALE;
            entity.setTestBoolean(testBoolean)
                    .setTestInt(testInt)
                    .setTestVarchar(testVarchar)
                    .setTestEnum(testEnum);
            entityList.add(entity);
        }
        dbEntitySameService.saveBatch(entityList);
        return entityList;
    }
}
