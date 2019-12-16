package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.entity.IntId;
import com.github.yt.mybatis.business.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import com.github.yt.mybatis.business.service.IntIdService;
import com.github.yt.mybatis.query.Page;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.QueryJoinType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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

    /**
     * get id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void get_sameNullId() {
        // null id
        dbEntitySameService.get(DbEntitySame.class, null);
    }

    /**
     * get id 不为空，记录不存在
     */
    @Test
    public void get_sameNotExist() {
        DbEntitySame entity = dbEntitySameService.get(DbEntitySame.class, "xxxx_null");
        Assert.assertNull(entity);
    }

    /**
     * get id 不为空，记录存在
     */
    @Test
    public void get_sameExist() {
        DbEntitySame entity = saveSameThenReturn();
        DbEntitySame dbEntity = dbEntitySameService.get(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
    }

    /**
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getOne_sameNullId() {
        // null id
        dbEntitySameService.getOne(DbEntitySame.class, null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void getOne_sameNotExist() {
        dbEntitySameService.getOne(DbEntitySame.class, "xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void getOne_sameExist() {
        DbEntitySame entity = saveSameThenReturn();
        DbEntitySame dbEntity = dbEntitySameService.getOne(DbEntitySame.class, entity.getDbEntitySameId());
        Assert.assertNotNull(dbEntity);
    }


    /**
     * get id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void get_notSameNullId() {
        // null id
        dbEntityNotSameService.get(DbEntityNotSame.class, null);
    }

    /**
     * get id 不为空，记录不存在
     */
    @Test
    public void get_notSameNotExist() {
        DbEntityNotSame entity = dbEntityNotSameService.get(DbEntityNotSame.class, "xxxx_null");
        Assert.assertNull(entity);
    }

    /**
     * get id 不为空，记录存在
     */
    @Test
    public void get_notSameExist() {
        DbEntityNotSame entity = saveNotSameThenReturn();
        DbEntityNotSame dbEntity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
    }

    /**
     * getOne id 为空
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getOne_notSameNullId() {
        // null id
        dbEntityNotSameService.getOne(DbEntityNotSame.class, null);
    }

    /**
     * getOne id 不为空，记录不存在
     */
    @Test(expectedExceptions = EmptyResultDataAccessException.class)
    public void getOne_notSameNotExist() {
        dbEntityNotSameService.getOne(DbEntityNotSame.class, "xxxx_null");
    }

    /**
     * getOne id 不为空，记录存在
     */
    @Test
    public void getOne_notSameExist() {
        DbEntityNotSame entity = saveNotSameThenReturn();
        DbEntityNotSame dbEntity = dbEntityNotSameService.getOne(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        Assert.assertNotNull(dbEntity);
    }


    @Test
    public void save_same() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22).setTestVarchar("22").setTestEnum(DbEntitySameTestEnumEnum.FEMALE);
        dbEntitySameService.save(entity);
        // id 不为空
        Assert.assertNotNull(entity.getDbEntitySameId());
        // find 然后判断各个值
        DbEntitySame dbEntitySame = new DbEntitySame();
        dbEntitySame.setDbEntitySameId(entity.getDbEntitySameId());
        DbEntitySame dbEntity = dbEntitySameService.find(dbEntitySame);
        Assert.assertEquals(dbEntity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(dbEntity.getTestInt(), (Integer) 22);
        Assert.assertEquals(dbEntity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) false);
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
        Assert.assertEquals(dbEntity.getDeleteFlag(), (Boolean) false);
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
                new Query().addJoin(QueryJoinType.JOIN, "DbEntitySame t2 on t.dbEntitySameId = t2.dbEntitySameId and t2.testVarchar in ${testVarcharList}")
                        .addParam("testVarcharList", Arrays.asList("0", "1")));
        Assert.assertEquals(dbList.size(), 6);
        // 清理数据
        deleteSame(list);
    }

    /**
     * entity 类中扩展字段，分组查询的 countNum 测试
     */
    @Test
    public void findList_extendField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addExtendSelectColumn("testVarchar, count(testVarchar) as countNum").addGroupBy("testVarchar"));
        Assert.assertEquals(dbList.size(), 4);
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void findList_notSameExtendField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().addExtendSelectColumn("test_varchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
        Assert.assertEquals(dbList.size(), 4);
        // 清理数据
        deleteNotSame(list);
    }

    @Test
    public void find_excludeField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addExcludeSelectColumn("t.testVarchar, testInt"));
        DbEntitySame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNotNull(dbEntity.getTestBoolean());
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void find_notSameExcludeField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query()
                        .addExcludeSelectColumn("t.test_varchar, test_int"));
        DbEntityNotSame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestVarchar());
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNotNull(dbEntity.getTestBoolean());
        // 清理数据
        deleteNotSame(list);
    }

    @Test
    public void find_extendAndExcludeField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query()
                        .addExcludeSelectColumn("testInt")
                        .addExtendSelectColumn("testVarchar, count(testVarchar) as countNum").addGroupBy("testVarchar"));
        DbEntitySame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNotNull(dbEntity.getTestBoolean());
        Assert.assertNotNull(dbEntity.getCountNum());
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void find_notSameExtendAndExcludeField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query()
                        .addExcludeSelectColumn("test_int")
                        .addExtendSelectColumn("test_varchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
        DbEntityNotSame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNotNull(dbEntity.getTestBoolean());
        Assert.assertNotNull(dbEntity.getCountNum());
        // 清理数据
        deleteNotSame(list);
    }

    @Test
    public void find_excludeAllField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query()
                        .excludeAllSelectColumn()
                        .addExtendSelectColumn("testVarchar, count(testVarchar) as countNum").addGroupBy("testVarchar"));
        Assert.assertEquals(dbList.size(), 4);
        DbEntitySame dbEntity = dbList.get(0);
        Assert.assertNotNull(dbEntity.getTestVarchar());
        Assert.assertNotNull(dbEntity.getCountNum());
        Assert.assertNull(dbEntity.getTestBoolean());
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void find_notSameExcludeAllField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query()
                        .excludeAllSelectColumn()
                        .addExtendSelectColumn("test_varchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
        Assert.assertEquals(dbList.size(), 4);
        DbEntityNotSame dbEntity = dbList.get(0);
        Assert.assertNotNull(dbEntity.getTestVarchar());
        Assert.assertNotNull(dbEntity.getCountNum());
        Assert.assertNull(dbEntity.getTestBoolean());
        // 清理数据
        deleteNotSame(list);
    }

    @Test
    public void findPage_propertiesName() {
        List<DbEntitySame> list = save12SameThenReturn();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(2).makePageSize(2));
        Assert.assertTrue(dbPage.containsKey(YtMybatisConfig.pageNoName));
        Assert.assertTrue(dbPage.containsKey(YtMybatisConfig.pageSizeName));
        Assert.assertTrue(dbPage.containsKey(YtMybatisConfig.pageTotalCountName));
        Assert.assertTrue(dbPage.containsKey(YtMybatisConfig.pageDataName));
        Assert.assertEquals(dbPage.getPageNo(), 2);

        Assert.assertEquals(dbPage.get(YtMybatisConfig.pageNoName), 2);
        Assert.assertEquals(dbPage.get(YtMybatisConfig.pageSizeName), 2);
        Assert.assertEquals(dbPage.get(YtMybatisConfig.pageTotalCountName), 12);
        Assert.assertEquals(((List)dbPage.get(YtMybatisConfig.pageDataName)).size(), 2);
        // 清理数据
        deleteSame(list);
    }

    private void deleteSame(List<DbEntitySame> list) {
        dbEntitySameService.delete(new DbEntitySame(),
                new Query().addWhere("dbEntitySameId in ${dbEntitySameIdList}")
                        .addParam("dbEntitySameIdList", list.stream().map(DbEntitySame::getDbEntitySameId).collect(Collectors.toList())));
    }

    private void deleteNotSame(List<DbEntityNotSame> list) {
        dbEntityNotSameService.delete(new DbEntityNotSame(),
                new Query().addWhere("db_entity_not_same_id in ${dbEntityNotSameIdList}")
                        .addParam("dbEntityNotSameIdList", list.stream().map(DbEntityNotSame::getDbEntityNotSameId).collect(Collectors.toList())));
    }

    private DbEntitySame saveSameThenReturn() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true).setTestInt(22222).setTestVarchar("22xxxx").setTestEnum(DbEntitySameTestEnumEnum.FEMALE);
        dbEntitySameService.save(entity);
        return entity;
    }

    private DbEntityNotSame saveNotSameThenReturn() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true).setTestInt(22222).setTestVarchar("22xxxx");
        dbEntityNotSameService.save(entity);
        return entity;
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
            // true false
            boolean testBoolean = (i % 2 == 0);
            // 0 1 2
            int testInt = i % 3;
            // 0 1 2 3
            String testVarchar = i % 4 + "";
            // FEMALE MALE
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

    private List<DbEntityNotSame> save12NotSameThenReturn() {
        List<DbEntityNotSame> entityList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            DbEntityNotSame entity = new DbEntityNotSame();
            // true false
            boolean testBoolean = (i % 2 == 0);
            // 0 1 2
            int testInt = i % 3;
            // 0 1 2 3
            String testVarchar = i % 4 + "";
            entity.setTestBoolean(testBoolean)
                    .setTestInt(testInt)
                    .setTestVarchar(testVarchar);
            entityList.add(entity);
        }
        dbEntityNotSameService.saveBatch(entityList);
        return entityList;
    }


}
