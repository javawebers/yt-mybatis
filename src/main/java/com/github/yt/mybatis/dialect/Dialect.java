package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.query.QueryLikeType;

import java.lang.reflect.Field;
import java.util.Collection;

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
     * 获取表别名
     *
     * @param entityClass 实体类
     * @return "user"
     */
    String getTableName(Class<?> entityClass);

    /**
     * 获取表别名
     *
     * @param entityClass 实体类
     * @return "user as t"
     */
    String getTableNameWithAlas(Class<?> entityClass);

    /**
     * 获取字段名称
     *
     * @param field field
     * @return oracle:"user_id",mysql:"`use_id`"
     */
    String getColumnName(Field field);

    /**
     * 获取属性名称
     *
     * @param field field
     * @return oracle:"userId",mysql:"`useId`"
     */
    String getFieldName(Field field);

    /**
     * 获取字段名称带表别名
     * "t.user_id"
     *
     * @param field field
     * @return "t.user_id"
     */
    String getColumnNameWithTableAlas(Field field);

    /**
     * 获取 insert sql
     *
     * @param entityCollection entityCollection
     * @return insert sql
     */
    String getInsertSql(Collection<?> entityCollection);

    /**
     * 分页支持
     *
     * @param sql       sql
     * @param limitFrom limitFrom
     * @param limitSize limitSize
     * @return new sql
     */
    String limitOffset(String sql, Integer limitFrom, Integer limitSize);

    /**
     * 获取FieldParam
     *
     * @param paramName paramName
     * @return 获取FieldParam
     */
    String getFieldParam(String paramName);

    /**
     * 获取 like param
     * mysql: CONCAT('%', #{keyword}, '%')
     *
     * @param paramName paramName
     * @param likeType  likeType
     * @return like str
     */
    String getLikeParam(String paramName, QueryLikeType likeType);

}
