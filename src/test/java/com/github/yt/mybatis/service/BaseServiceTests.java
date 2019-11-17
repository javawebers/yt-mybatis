package com.github.yt.mybatis.service;

import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.YtMybatisDemoApplication;

import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import com.github.yt.mybatis.business.service.DbEntitySameService;
import com.github.yt.mybatis.business.service.IntIdService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceTests extends AbstractTestNGSpringContextTests {

    @Resource
    DbEntitySameService dbEntitySameService;
    @Resource
    DbEntityNotSameService dbEntityNotSameService;
    @Resource
    IntIdService intIdService;

    @Test
    public void save_same() {
        DbEntitySame entity = new DbEntitySame();
        dbEntitySameService.save(entity);
        // TODO 验证
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

}
