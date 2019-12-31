package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.entity.IntId;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.example.service.IntIdService;
import com.github.yt.mybatis.query.Page;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @AfterMethod
    public void after() {
        dbEntitySameService.delete(new DbEntitySame());
        dbEntityNotSameService.delete(new DbEntityNotSame());
        intIdService.delete(new IntId());
    }


    /**
     * entity 类中扩展字段，分组查询的 countNum 测试
     */
    @Test
    public void findList_extendField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().excludeAllSelectColumn().addExtendSelectColumn("testVarchar, count(testVarchar) as countNum").addGroupBy("testVarchar"));
        Assert.assertEquals(dbList.size(), 4);
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void findList_notSameExtendField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().excludeAllSelectColumn().addExtendSelectColumn("test_varchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
        Assert.assertEquals(dbList.size(), 4);
        // 清理数据
        deleteNotSame(list);
    }

    @Test
    public void find_excludeField() {
        List<DbEntitySame> list = save12SameThenReturn();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addExcludeSelectColumn("testVarchar, testInt"));
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
                        .addExcludeSelectColumn("test_varchar, test_int"));
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
                        .excludeAllSelectColumn()
                        .addExtendSelectColumn("testVarchar, count(testVarchar) as countNum").addGroupBy("testVarchar"));
        DbEntitySame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestBoolean());
        Assert.assertNotNull(dbEntity.getCountNum());
        // 清理数据
        deleteSame(list);
    }

    @Test
    public void find_notSameExtendAndExcludeField() {
        List<DbEntityNotSame> list = save12NotSameThenReturn();
        List<DbEntityNotSame> dbList = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query()
                        .excludeAllSelectColumn()
                        .addExtendSelectColumn("test_varchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
        DbEntityNotSame dbEntity = dbList.get(0);
        Assert.assertNull(dbEntity.getTestInt());
        Assert.assertNull(dbEntity.getTestBoolean());
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
                        .addExtendSelectColumn("test_varchar as testVarchar, count(test_varchar) as countNum").addGroupBy("test_varchar"));
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
        Assert.assertEquals(((List) dbPage.get(YtMybatisConfig.pageDataName)).size(), 2);
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

    /**
     * 创建12条记录
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
