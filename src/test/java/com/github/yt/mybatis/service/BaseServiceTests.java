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
        Assert.assertEquals(entity.getTestBoolean(), (Boolean) true);
        Assert.assertEquals(entity.getTestInt(), (Integer) 22);
        Assert.assertEquals(entity.getTestVarchar(), "22");
        Assert.assertEquals(dbEntity.getTestEnum(), DbEntitySameTestEnumEnum.FEMALE);
    }

    @Test
    public void save_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        // TODO 验证
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
        entity.setTestInt(222);
        dbEntitySameService.update(entity);
        // TODO 验证
    }

    @Test
    public void update_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        entity = dbEntityNotSameService.get(DbEntityNotSame.class, entity.getDbEntityNotSameId());
        entity.setTestInt(222);
        dbEntityNotSameService.update(entity);
        // TODO 验证
    }

    @Test
    public void updateByCondition_same() {
        DbEntitySame entity1 = new DbEntitySame().setTestInt(222);
        DbEntitySame entity2 = new DbEntitySame().setTestInt(222);
        List<DbEntitySame> dbEntitySameList = Arrays.asList(entity1, entity2);
        dbEntitySameService.saveBatch(dbEntitySameList);
        dbEntitySameService.updateByCondition(new DbEntitySame().setTestInt(222), new Query().addUpdate("testVarchar = 'varchar222'"));

        // TODO 验证
    }

    @Test
    public void updateByCondition_notSame() {
        DbEntityNotSame entity1 = new DbEntityNotSame().setTestInt(222);
        DbEntityNotSame entity2 = new DbEntityNotSame().setTestInt(222);
        List<DbEntityNotSame> dbEntitySameList = Arrays.asList(entity1, entity2);
        dbEntityNotSameService.saveBatch(dbEntitySameList);
        dbEntityNotSameService.updateByCondition(new DbEntityNotSame().setTestInt(222), new Query().addUpdate("test_varchar = 'varchar222'"));

        // TODO 验证
    }

    @Test
    public void delete_same() {
        // TODO
    }

    @Test
    public void delete_notSame() {
        // TODO
    }

    @Test
    public void logicDelete_same() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);
        dbEntitySameService.logicDelete(new DbEntitySame().setDbEntitySameId(entity.getDbEntitySameId()));
        // TODO 验证
    }

    @Test
    public void logicDelete_notSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        dbEntityNotSameService.save(entity);
        dbEntityNotSameService.logicDelete(new DbEntityNotSame().setDbEntityNotSameId(entity.getDbEntityNotSameId()));

        // TODO
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
