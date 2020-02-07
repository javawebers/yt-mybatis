package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.QueryJoinType;
import com.github.yt.mybatis.query.QueryLikeType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceQueryLikeTests extends AbstractTestNGSpringContextTests {

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
    public void sameLeftLike() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar_", QueryLikeType.LEFT));
        Assert.assertEquals(12, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar_0", QueryLikeType.LEFT));
        Assert.assertEquals(3, count);
    }

    @Test
    public void sameLeftLikeNo() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char_", QueryLikeType.LEFT));
        Assert.assertEquals(0, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char_0", QueryLikeType.LEFT));
        Assert.assertEquals(0, count);
    }

    @Test
    public void sameRightLike() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char_0", QueryLikeType.RIGHT));
        Assert.assertEquals(3, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar_0", QueryLikeType.RIGHT));
        Assert.assertEquals(3, count);
    }

    @Test
    public void sameRightLikeNo() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar", QueryLikeType.RIGHT));
        Assert.assertEquals(0, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char", QueryLikeType.RIGHT));
        Assert.assertEquals(0, count);
    }


    @Test
    public void sameMiddleLike() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char"));
        Assert.assertEquals(12, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar"));
        Assert.assertEquals(12, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "varchar_0"));
        Assert.assertEquals(3, count);

        count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "char_0"));
        Assert.assertEquals(3, count);
    }

    @Test
    public void sameMiddleLikeNo() {
        dataBasicService.save12Same();
        int count = dbEntitySameService.count(new DbEntitySame(),
                new Query().like("t.testVarchar", "aaa"));
        Assert.assertEquals(0, count);
    }

}
