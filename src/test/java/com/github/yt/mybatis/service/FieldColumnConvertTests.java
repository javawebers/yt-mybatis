package com.github.yt.mybatis.service;

import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class FieldColumnConvertTests {

    @Resource
    private DbEntityNotSameService dbEntityNotSameService;

    @Test
    public void saveThenFind() {
        DbEntityNotSame dbEntityNotSame = new DbEntityNotSame();
        Integer testInt = 222;
        dbEntityNotSame.setTestInt(testInt);
        dbEntityNotSameService.save(dbEntityNotSame);
        // 查询出来，对比 testInt 是否为222
        DbEntityNotSame dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()),
                new Query().addSelectColumn("db_entity_not_same_id as dbEntityNotSameId, test_int as testInt"));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt);
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()),
                new Query().addWhere("test_int = #{test_int}").addParam("test_int", testInt));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt);
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setTestInt(testInt),
                new Query().addWhere("test_int = #{test_int}").addParam("test_int", testInt));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt);

    }

    @Test
    public void updateThenFind() {
        DbEntityNotSame dbEntityNotSame = new DbEntityNotSame();
        Integer testInt222 = 222;
        Integer testInt333 = 333;
        dbEntityNotSame.setTestInt(testInt222);
        dbEntityNotSameService.save(dbEntityNotSame);

        // 根据主键更新
        dbEntityNotSameService.update(dbEntityNotSame.setTestInt(testInt333).setDeleteFlag(false));
        DbEntityNotSame dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt333);
        // 根据主键更新，带更新字段
        dbEntityNotSameService.update(dbEntityNotSame.setTestInt(testInt222), "test_int");
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt222);

        // 根据主键更新，不为空字段
        dbEntityNotSameService.updateForSelective(dbEntityNotSame.setTestInt(testInt333));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt333);

        // 根据条件更新
        dbEntityNotSameService.updateByCondition(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setTestInt(testInt333),
                new Query().addUpdate("test_int = #{test_int}")
                        .addParam("test_int", testInt222));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertEquals("查询出内容和保存的内容不一致，dbEntityNotSameDb.getTestInt()：" + dbEntityNotSameDb.getTestInt(),
                dbEntityNotSameDb.getTestInt(), testInt222);
    }

    @Test
    public void deleteThenFind() {
        DbEntityNotSame dbEntityNotSame = new DbEntityNotSame();
        Integer testInt222 = 222;
        dbEntityNotSame.setTestInt(testInt222);

        // 根据主键删除
        dbEntityNotSameService.save(dbEntityNotSame.setDbEntityNotSameId(null));
        dbEntityNotSameService.delete(DbEntityNotSame.class, dbEntityNotSame.getDbEntityNotSameId());
        DbEntityNotSame dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertNull("删除对象失败", dbEntityNotSameDb);

        // 根据条件删除
        dbEntityNotSameService.save(dbEntityNotSame.setDbEntityNotSameId(null));
        dbEntityNotSameService.delete(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setTestInt(testInt222));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertNull("删除对象失败", dbEntityNotSameDb);

        // 根据条件删除2
        dbEntityNotSameService.save(dbEntityNotSame.setDbEntityNotSameId(null));
        dbEntityNotSameService.delete(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()),
                new Query().addWhere("test_int = #{test_int}").addParam("test_int", testInt222));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()));
        Assert.assertNull("删除对象失败", dbEntityNotSameDb);

        // 逻辑删除
        dbEntityNotSameService.save(dbEntityNotSame.setDbEntityNotSameId(null));
        dbEntityNotSameService.logicDelete(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setTestInt(testInt222));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setDeleteFlag(false));
        Assert.assertNull("删除对象失败", dbEntityNotSameDb);

        // 逻辑删除2
        dbEntityNotSameService.save(dbEntityNotSame.setDbEntityNotSameId(null));
        dbEntityNotSameService.logicDelete(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()),
                new Query().addWhere("test_int = #{test_int}").addParam("test_int", testInt222));
        dbEntityNotSameDb = dbEntityNotSameService.find(new DbEntityNotSame().setDbEntityNotSameId(dbEntityNotSame.getDbEntityNotSameId()).setDeleteFlag(false));
        Assert.assertNull("删除对象失败", dbEntityNotSameDb);
    }

}
