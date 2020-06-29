package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DataBasicService;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;
import com.github.yt.mybatis.example.service.DbEntitySameService;
import com.github.yt.mybatis.query.Page;
import com.github.yt.mybatis.query.Query;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class BaseServiceQueryPageTests extends AbstractTestNGSpringContextTests {

    @Resource
    private DataBasicService dataBasicService;

    @Resource
    private YtMybatisConfig ytMybatisConfig;

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
    public void findPagePropertiesName() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(2).makePageSize(2));
        int pageNo = dbPage.getPageNo();
        int pageSize = dbPage.getPageSize();
        int totalCount = dbPage.getTotalCount();
        List<DbEntitySame> pageData = dbPage.getData();

        Assert.assertTrue(dbPage.containsKey(ytMybatisConfig.getPage().getPageNoName()));
        Assert.assertTrue(dbPage.containsKey(ytMybatisConfig.getPage().getPageSizeName()));
        Assert.assertTrue(dbPage.containsKey(ytMybatisConfig.getPage().getPageTotalCountName()));
        Assert.assertTrue(dbPage.containsKey(ytMybatisConfig.getPage().getPageDataName()));
        Assert.assertEquals(dbPage.getPageNo(), 2);

        Assert.assertEquals(dbPage.get(ytMybatisConfig.getPage().getPageNoName()), 2);
        Assert.assertEquals(dbPage.get(ytMybatisConfig.getPage().getPageSizeName()), 2);
        Assert.assertEquals(dbPage.get(ytMybatisConfig.getPage().getPageTotalCountName()), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 2);
    }


    @Test
    public void findPage1() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(1).makePageSize(11));

        Assert.assertEquals(dbPage.getPageNo(), 1);
        Assert.assertEquals(dbPage.getPageSize(), 11);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 11);
    }


    @Test
    public void findPage11() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new Query().makePageNo(1).makePageSize(11));

        Assert.assertEquals(dbPage.getPageNo(), 1);
        Assert.assertEquals(dbPage.getPageSize(), 11);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 11);
    }


    @Test
    public void findPage2() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(1).makePageSize(12));

        Assert.assertEquals(dbPage.getPageNo(), 1);
        Assert.assertEquals(dbPage.getPageSize(), 12);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 12);
    }

    @Test
    public void findPage3() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(1).makePageSize(13));

        Assert.assertEquals(dbPage.getPageNo(), 1);
        Assert.assertEquals(dbPage.getPageSize(), 13);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 12);
    }

    @Test
    public void findPage4() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(2).makePageSize(10));
        Assert.assertEquals(dbPage.getPageNo(), 2);
        Assert.assertEquals(dbPage.getPageSize(), 10);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 2);
    }


    @Test
    public void findPage5() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(2).makePageSize(5));
        Assert.assertEquals(dbPage.getPageNo(), 2);
        Assert.assertEquals(dbPage.getPageSize(), 5);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 5);
    }


    @Test
    public void findPage6() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(3).makePageSize(10));
        Assert.assertEquals(dbPage.getPageNo(), 3);
        Assert.assertEquals(dbPage.getPageSize(), 10);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 0);
    }

    @Test
    public void findPage7() {
        List<DbEntitySame> list = dataBasicService.save12Same();
        Page<DbEntitySame> dbPage = dbEntitySameService.findPage(new DbEntitySame()
                , new Query().makePageNo(3).makePageSize(6));
        Assert.assertEquals(dbPage.getPageNo(), 3);
        Assert.assertEquals(dbPage.getPageSize(), 6);
        Assert.assertEquals(dbPage.getTotalCount(), 12);
        Assert.assertEquals(((List<?>) dbPage.get(ytMybatisConfig.getPage().getPageDataName())).size(), 0);
    }


}
