package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceQueryCompareTests extends AbstractTestNGSpringContextTests {

    @Resource
    private DataBasicService dataBasicService;

    @Resource
    DbEntitySameService dbEntitySameService;
    @Resource
    DbEntityNotSameService dbEntityNotSameService;

    @AfterMethod
    public void after() {
        dbEntitySameService.delete(new DbEntitySame());
        dbEntityNotSameService.delete(new DbEntityNotSame());
    }

    @Test
    public void sameEqualDate() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Date createTime = list.get(0).getCreateTime();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.createTime", createTime));
        Assert.assertEquals(12, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.createTime", new Date(System.currentTimeMillis() + 50000)));
        Assert.assertEquals(0, dbList.size());
    }

    @Test
    public void sameEqualInt() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.testInt", 1));
        Assert.assertEquals(4, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.testInt", 5));
        Assert.assertEquals(0, dbList.size());
    }


    @Test
    public void sameEqualStr() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.testVarchar", "varchar_1"));
        Assert.assertEquals(3, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().equal("t.testVarchar", "varchar_5"));
        Assert.assertEquals(0, dbList.size());
    }


    @Test
    public void sameNotEqualDate() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Date createTime = list.get(0).getCreateTime();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.createTime", createTime));
        Assert.assertEquals(0, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.createTime", new Date(System.currentTimeMillis() + 50000)));
        Assert.assertEquals(12, dbList.size());
    }

    @Test
    public void sameNotEqualInt() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.testInt", 1));
        Assert.assertEquals(8, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.testInt", 5));
        Assert.assertEquals(12, dbList.size());
    }


    @Test
    public void sameNotEqualStr() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.testVarchar", "varchar_1"));
        Assert.assertEquals(9, dbList.size());

        dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().notEqual("t.testVarchar", "varchar_5"));
        Assert.assertEquals(12, dbList.size());
    }


    @Test
    public void sameGtDate() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Date createTime = list.get(0).getCreateTime();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().gt("t.createTime", createTime));
        Assert.assertEquals(0, dbList.size());
    }

    @Test
    public void sameGeDate() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Date createTime = list.get(0).getCreateTime();
        List<DbEntitySame> dbList = dbEntitySameService.findList(new DbEntitySame(),
                new Query().ge("t.createTime", createTime));
        Assert.assertEquals(12, dbList.size());
    }

    @Test
    public void sameGt() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().gt("t.testInt", 1));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 2, entity.getTestInt());
        });
    }

    @Test
    public void sameGt2() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().gt("t.testInt", 2));
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void sameGt3() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().gt("t.testInt", null));
        Assert.assertEquals(12, list.size());
    }

    @Test
    public void sameGe() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().ge("t.testInt", 1));
        Assert.assertEquals(8, list.size());
        list.forEach(entity -> {
            Assert.assertTrue(entity.getTestInt() >= 1);
        });
    }

    @Test
    public void sameGe2() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().ge("t.testInt", 2));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 2, entity.getTestInt());
        });
    }

    @Test
    public void sameGe3() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().ge("t.testInt", 3));
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void sameGe4() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().ge("t.testInt", null));
        Assert.assertEquals(12, list.size());
    }

    @Test
    public void sameLt() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().lt("t.testInt", 1));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 0, entity.getTestInt());
        });
    }

    @Test
    public void sameLt2() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().lt("t.testInt", 0));
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void sameLt3() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().lt("t.testInt", null));
        Assert.assertEquals(12, list.size());
    }

    @Test
    public void sameLe() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().le("t.testInt", 1));
        Assert.assertEquals(8, list.size());
        list.forEach(entity -> {
            Assert.assertTrue(entity.getTestInt() <= 1);
        });
    }

    @Test
    public void sameLe2() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().le("t.testInt", 0));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 0, entity.getTestInt());
        });
    }

    @Test
    public void sameLe3() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().le("t.testInt", -1));
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void sameLe4() {
        dataBasicService.save12Same();
        List<DbEntitySame> list = dbEntitySameService.findList(new DbEntitySame(),
                new Query().le("t.testInt", null));
        Assert.assertEquals(12, list.size());
    }


    @Test
    public void notSameGt() {
        dataBasicService.save12NotSame();
        List<DbEntityNotSame> list = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().gt("t.test_int", 1));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 2, entity.getTestInt());
        });
    }

    @Test
    public void notSameGe() {
        dataBasicService.save12NotSame();
        List<DbEntityNotSame> list = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().ge("t.test_int", 1));
        Assert.assertEquals(8, list.size());
        list.forEach(entity -> {
            Assert.assertTrue(entity.getTestInt() >= 1);
        });
    }


    @Test
    public void notSameLt() {
        dataBasicService.save12NotSame();
        List<DbEntityNotSame> list = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().lt("t.test_int", 1));
        Assert.assertEquals(4, list.size());
        list.forEach(entity -> {
            Assert.assertEquals((Integer) 0, entity.getTestInt());
        });
    }

    @Test
    public void notSameLe() {
        dataBasicService.save12NotSame();
        List<DbEntityNotSame> list = dbEntityNotSameService.findList(new DbEntityNotSame(),
                new Query().le("t.test_int", 1));
        Assert.assertEquals(8, list.size());
        list.forEach(entity -> {
            Assert.assertTrue(entity.getTestInt() <= 1);
        });
    }


}
