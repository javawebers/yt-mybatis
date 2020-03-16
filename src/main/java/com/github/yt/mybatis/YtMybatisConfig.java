package com.github.yt.mybatis;

import com.github.yt.mybatis.dialect.Dialect;
import com.github.yt.mybatis.entity.BaseEntityValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sheng
 */
@Configuration
public class YtMybatisConfig {

    /**
     * 分页参数
     */
    public static String pageNoName;
    public static String pageSizeName;
    public static String pageTotalCountName;
    public static String pageDataName;

    /**
     * 方言
     */
    public static Class<? extends Dialect> dialectClass;

    /**
     * BaseEntityValue 实现类
     */
    public static Class<? extends BaseEntityValue> baseEntityValueClass;

    @Value("${yt.entity.baseEntityValue:com.github.yt.mybatis.entity.DefaultBaseEntityValue}")
    public void setDialectEnum(Class<? extends BaseEntityValue> baseEntityValueClass) {
        YtMybatisConfig.baseEntityValueClass = baseEntityValueClass;
    }

    @Value("${yt.mybatis.dialect:com.github.yt.mybatis.dialect.impl.MysqlDialect}")
    public void setDialectClass(Class<? extends Dialect> dialectClass) {
        YtMybatisConfig.dialectClass = dialectClass;
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

