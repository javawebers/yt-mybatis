package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.QueryJoinType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceQueryInTests extends AbstractTestNGSpringContextTests {

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
    public void in1() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", Arrays.asList(1, 2)));
        Assert.assertEquals(8, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", 1, 2));
        Assert.assertEquals(8, count);

        int[] testInts = new int[]{1, 2};
        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", testInts));
        Assert.assertEquals(8, count);
    }

    @Test
    public void in2() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", Collections.singletonList(1)));
        Assert.assertEquals(4, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", 1));
        Assert.assertEquals(4, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", new int[]{1}));
        Assert.assertEquals(4, count);
    }
    @Test
    public void in3() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", new ArrayList<>()));
        Assert.assertEquals(0, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().in("testInt", new int[]{}));
        Assert.assertEquals(0, count);
    }



    @Test
    public void notIn1() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", Arrays.asList(1, 2)));
        Assert.assertEquals(4, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", 1, 2));
        Assert.assertEquals(4, count);
    }

    @Test
    public void notIn2() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", Collections.singletonList(1)));
        Assert.assertEquals(8, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", 1));
        Assert.assertEquals(8, count);
    }
    @Test
    public void notIn3() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", new ArrayList<>()));
        Assert.assertEquals(0, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().notIn("testInt", new int[]{}));
        Assert.assertEquals(0, count);
    }



    @Test
    public void sameIn() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt in ${testInt}")
                        .addParam("testInt", Arrays.asList(1, 2)));
        Assert.assertEquals(8, count);

    }

    @Test
    public void sameNotIn() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt not in ${testInt}")
                        .addParam("testInt", Arrays.asList(1, 2, 3)));
        Assert.assertEquals(4, count);
    }

    @Test
    public void sameInEmpty() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt in ${testInt}")
                        .addParam("testInt", Arrays.asList(1, 2, 3)));
        Assert.assertEquals(8, count);
    }

    @Test
    public void sameInEmpty2() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt in ${testInt}")
                        .addParam("testInt", Arrays.asList(3, 4)));
        Assert.assertEquals(0, count);
    }

    @Test
    public void sameInEmpty3() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt in ${testInt}")
                        .addParam("testInt", new ArrayList<>(0)));
        Assert.assertEquals(0, count);
    }

    @Test
    public void sameNotInEmpty2() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt not in ${testInt}")
                        .addParam("testInt", Arrays.asList(3, 4)));
        Assert.assertEquals(12, count);
    }

    @Test
    public void sameNotInEmpty3() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addWhere("testInt not in ${testInt}")
                        .addParam("testInt", new ArrayList<>(0)));
        Assert.assertEquals(0, count);
    }

    @Test
    public void notSameIn() {
        dataBasicService.save12NotSame();
        int count = dbEntityNotSameService.count(new DbEntityNotSame(),
                new Query().addWhere("test_int in ${testInt}")
                        .addParam("testInt", Arrays.asList(1, 2)));
        Assert.assertEquals(8, count);
    }


    /**
     * in 条件查询， in不在where中，在join中
     */
    @Test
    public void inJoin() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().addJoin(QueryJoinType.JOIN,
                        "DbEntitySame t2 on t.dbEntitySameId = t2.dbEntitySameId and t2.testInt in ${testInt}")
                        .addParam("testInt", Arrays.asList(0, 1)));
        Assert.assertEquals(8, count);
    }

}
