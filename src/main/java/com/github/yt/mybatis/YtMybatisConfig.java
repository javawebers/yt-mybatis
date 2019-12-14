package com.github.yt.mybatis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sheng
 */
@Configuration
public class YtMybatisConfig {
    public static String dialect;
    public static String resultClass;

    // 分页
    public static String pageNoName;
    public static String pageSizeName;
    public static String pageTotalCountName;
    public static String pageDataName;


    @Value("${yt.dialect:com.github.yt.mybatis.dialect.mysql.MysqlDialect}")
    public void setDialect(String dialect) {
        YtMybatisConfig.dialect = dialect;
    }

    @Value("${yt.result.class:com.github.yt.web.result.SimpleResultConfig}")
    public void setResultClass(String resultClass) {
        YtMybatisConfig.resultClass = resultClass;
    }

    @Value("${yt.page.pageNoName:pageNo}")
    public void setPageNoName(String pageNoName) {
        YtMybatisConfig.pageNoName = pageNoName;
    }

    @Value("${yt.page.pageSizeName:pageSize}")
    public void setPageSizeName(String pageSizeName) {
        YtMybatisConfig.pageSizeName = pageSizeName;
    }

    @Value("${yt.page.pageTotalCountName:totalCount}")
    public void setPageTotalCountName(String pageTotalCountName) {
        YtMybatisConfig.pageTotalCountName = pageTotalCountName;
    }

    @Value("${yt.page.pageDataName:data}")
    public void setPageDataName(String pageDataName) {
        YtMybatisConfig.pageDataName = pageDataName;
    }

}

