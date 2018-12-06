package com.github.yt.mybatis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YtMybatisConfig {
    public static String baseEntityValueClass;
    public static String dialect;
    public static String resultClass;

    // 分页
    public static String pageNoName;
    public static String pageSizeName;
    public static String pageTotalCountName;
    public static String pageDataName;

    // swagger
    public static boolean showSwagger;

    // 请求日志
    // 是否记录请求日志
    public static boolean requestLog;
    // 如果记录日志是否记录post等的body内容，记录body内容在单元测试时会有问题，取不到body的值
    public static boolean requestLogBody;

    @Value("${yt.swagger.show:false}")
    public void setShowSwagger(boolean showSwagger) {
        YtMybatisConfig.showSwagger = showSwagger;
    }

    @Value("${yt.entity.baseEntityValue:com.github.yt.mybatis.domain.DefaultBaseEntityValue}")
    public void setBaseEntityValueClass(String baseEntityValueClass) {
        YtMybatisConfig.baseEntityValueClass = baseEntityValueClass;
    }

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
    @Value("${yt.request.requestLog:true}")
    public void setRequestLog(boolean requestLog) {
        YtMybatisConfig.requestLog = requestLog;
    }
    @Value("${yt.request.requestLogBody:false}")
    public void setRequestLogBody(boolean requestLogBody) {
        YtMybatisConfig.requestLogBody = requestLogBody;
    }
}

