package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;

/**
 * 方言
 *
 * @author sheng
 */
public interface Dialect {

    /**
     * 获取表别名
     *
     * @return "t"
     */
    String getTableAlas();

    /**
     * 获取字段名称
     *
     * @param field field
     * @return "user_id"
     */
    String getColumnName(Field field);

    /**
     * 获取字段名称带表别名
     * "t.user_id"
     *
     * @param field field
     * @return "t.user_id"
     */
    String getColumnNameWithTableAlas(Field field);
}