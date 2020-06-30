package com.github.yt.mybatis;

import com.github.yt.mybatis.dialect.Dialect;
import com.github.yt.mybatis.dialect.impl.MysqlDialect;
import com.github.yt.mybatis.entity.BaseEntityValue;
import com.github.yt.mybatis.entity.DefaultBaseEntityValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author sheng
 */
@Component
@ConfigurationProperties("yt")
public class YtMybatisConfig implements Serializable {

    /**
     *
     */
    private Entity entity = new Entity();
    private Mybatis mybatis = new Mybatis();
    private Page page = new Page();

    public Entity getEntity() {
        return entity;
    }

    public YtMybatisConfig setEntity(Entity entity) {
        this.entity = entity;
        return this;
    }

    public Mybatis getMybatis() {
        return mybatis;
    }

    public YtMybatisConfig setMybatis(Mybatis mybatis) {
        this.mybatis = mybatis;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public YtMybatisConfig setPage(Page page) {
        this.page = page;
        return this;
    }

    public static class Entity {
        /**
         * base entity 自动设置值实现类
         */
        private Class<? extends BaseEntityValue> baseEntityValue = DefaultBaseEntityValue.class;

        public Class<? extends BaseEntityValue> getBaseEntityValue() {
            return baseEntityValue;
        }

        public Entity setBaseEntityValue(Class<? extends BaseEntityValue> baseEntityValue) {
            this.baseEntityValue = baseEntityValue;
            return this;
        }
    }

    public static class Mybatis {
        /**
         * 数据库方言
         * 在 com.github.yt.mybatis.dialect.impl 包中，默认为 MysqlDialect
         */
        private Class<? extends Dialect> dialect = MysqlDialect.class;

        public Class<? extends Dialect> getDialect() {
            return dialect;
        }

        public Mybatis setDialect(Class<? extends Dialect> dialect) {
            this.dialect = dialect;
            return this;
        }
    }

    public static class Page {
        /**
         * 页码
         */
        private String pageNoName = "pageNo";
        /**
         * 每页记录数
         */
        private String pageSizeName = "pageSize";
        /**
         * 总条数
         */
        private String pageTotalCountName = "totalCount";
        /**
         * 数据字段
         */
        private String pageDataName = "data";

        public String getPageNoName() {
            return pageNoName;
        }

        public Page setPageNoName(String pageNoName) {
            this.pageNoName = pageNoName;
            return this;
        }

        public String getPageSizeName() {
            return pageSizeName;
        }

        public Page setPageSizeName(String pageSizeName) {
            this.pageSizeName = pageSizeName;
            return this;
        }

        public String getPageTotalCountName() {
            return pageTotalCountName;
        }

        public Page setPageTotalCountName(String pageTotalCountName) {
            this.pageTotalCountName = pageTotalCountName;
            return this;
        }

        public String getPageDataName() {
            return pageDataName;
        }

        public Page setPageDataName(String pageDataName) {
            this.pageDataName = pageDataName;
            return this;
        }
    }

}

