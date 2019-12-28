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
    protected final String TABLE_ALAS = "t";

    protected final String POINT = ".";


    @Override
    public String getTableAlas() {
        return TABLE_ALAS;
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
