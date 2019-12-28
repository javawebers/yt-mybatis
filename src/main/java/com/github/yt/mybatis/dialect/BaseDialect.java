package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;

/**
 * 基础实现
 *
 * @author sheng
 */
public abstract class BaseDialect implements Dialect {

    /**
     * 表的别名
     * "t"
     */
    protected final String TABLE_ALIAS = "t";

    protected final String POINT = ".";

    protected final String AS = "as";

    @Override
    public String getTableName(Class<?> entityClass) {
        return EntityUtils.getTableName(entityClass);
    }

    @Override
    public String getTableNameWithAlas(Class<?> entityClass) {
        return getTableName(entityClass) + " " + AS + " " + getTableAlas();
    }

    @Override
    public String getTableAlas() {
        return TABLE_ALIAS;
    }

    @Override
    public String getColumnName(Field field) {
        return EntityUtils.getFieldColumnName(field);
    }


    @Override
    public String getColumnNameWithTableAlas(Field field) {
        return getTableAlas() + POINT + getColumnName(field);
    }


}
