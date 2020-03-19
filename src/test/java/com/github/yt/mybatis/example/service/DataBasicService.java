package com.github.yt.mybatis.example.service;

import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.po.DbEntitySameTestEnumEnum;
import com.github.yt.mybatis.query.Query;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据基本操作服务类
 * 包含生成数据，销毁数据等
 */
@Service
public class DataBasicService {

    @Resource
    DbEntitySameService dbEntitySameService;
    @Resource
    DbEntityNotSameService dbEntityNotSameService;

    public DbEntitySame saveOneSame() {
        DbEntitySame entity = new DbEntitySame();
        entity.setTestBoolean(true)
                .setTestInt(entity.hashCode())
                .setTestVarchar("varchar_" + entity.hashCode())
                .setTestEnum(DbEntitySameTestEnumEnum.FEMALE);
        dbEntitySameService.save(entity);
        return dbEntitySameService.findById(entity.getDbEntitySameId());
    }

    public void deleteSame(DbEntitySame entity) {
        int count = dbEntitySameService.deleteById(entity.getDbEntitySameId());
        Assert.assertEquals(1, count);
    }

    public void deleteSame(List<DbEntitySame> list) {
        int count = dbEntitySameService.delete(new DbEntitySame(),
                new Query().addWhere("dbEntitySameId in ${dbEntitySameIdList}")
                        .addParam("dbEntitySameIdList",
                                list.stream().map(DbEntitySame::getDbEntitySameId).collect(Collectors.toList())));
        Assert.assertEquals(list.size(), count);
    }

    public List<DbEntitySame> findSameList(List<DbEntitySame> list) {
        List<String> idList = list.stream().map(DbEntitySame::getDbEntitySameId).collect(Collectors.toList());
        List<String> idSqlList = new ArrayList<>();
        for (String id : idList) {
            idSqlList.add("'" + id + "'");
        }
        List<DbEntitySame> result = dbEntitySameService.findList(new DbEntitySame(),
                new Query().addWhere("dbEntitySameId in ${dbEntitySameIdList}")
                        .addParam("dbEntitySameIdList", idList)
                        .addOrderBy("dbEntitySameId"));
        Assert.assertEquals(result.size(), list.size());
        return result;
    }

    public DbEntityNotSame saveOneNotSame() {
        DbEntityNotSame entity = new DbEntityNotSame();
        entity.setTestBoolean(true)
                .setTestInt(entity.hashCode())
                .setTestVarchar("varchar_" + entity.hashCode());
        dbEntityNotSameService.save(entity);
        return dbEntityNotSameService.findById(entity.getDbEntityNotSameId());
    }

    public void deleteNotSame(DbEntityNotSame entity) {
        int count = dbEntityNotSameService.deleteById(entity.getDbEntityNotSameId());
        Assert.assertEquals(1, count);
    }

    public void deleteNotSame(List<DbEntityNotSame> list) {
        int count = dbEntityNotSameService.delete(new DbEntityNotSame(),
                new Query().addWhere("db_entity_not_same_id in ${dbEntityNotSameIdList}")
                        .addParam("dbEntityNotSameIdList",
                                list.stream().map(DbEntityNotSame::getDbEntityNotSameId).collect(Collectors.toList())));
        Assert.assertEquals(list.size(), count);
    }
    public List<DbEntityNotSame> findNotSameList(List<DbEntityNotSame> list) {
        List<String> idList = list.stream().map(DbEntityNotSame::getDbEntityNotSameId).collect(Collectors.toList());
        List<String> idSqlList = new ArrayList<>();
        for (String id : idList) {
            idSqlList.add("'" + id + "'");
        }
        List<DbEntityNotSame> result = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().addWhere("db_entity_not_same_id in ${dbEntityNotSameIdList}")
                        .addParam("dbEntityNotSameIdList", idList)
                        .addOrderBy("db_entity_not_same_id"));
        Assert.assertEquals(list.size(), result.size());
        return result;
    }

    /**
     * 创建12条记录
     *
     * @return
     */
    public List<DbEntitySame> save12Same() {
        List<DbEntitySame> entityList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            DbEntitySame entity = new DbEntitySame();
            // true false
            boolean testBoolean = (i % 2 == 0);
            // 0 1 2
            int testInt = i % 3;
            // 0 1 2 3
            String testVarchar = "varchar_" + i % 4;
            // FEMALE MALE
            DbEntitySameTestEnumEnum testEnum = (i % 2 == 0) ? DbEntitySameTestEnumEnum.FEMALE : DbEntitySameTestEnumEnum.MALE;
            entity.setTestBoolean(testBoolean)
                    .setTestInt(testInt)
                    .setTestVarchar(testVarchar)
                    .setTestEnum(testEnum);
            entityList.add(entity);
        }
        dbEntitySameService.saveBatch(entityList);
        return findSameList(entityList);
    }

    public List<DbEntityNotSame> save12NotSame() {
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
        return findNotSameList(entityList);
    }


}
