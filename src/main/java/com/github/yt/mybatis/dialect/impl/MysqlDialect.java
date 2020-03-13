package com.github.yt.mybatis.dialect.impl;

import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.mybatis.query.QueryLikeType;
import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;

/**
 * mysql 实现
 *
 * @author sheng
 */
public class MysqlDialect extends BaseDialect {

    /**
     * mysql 的转义符
     */
    private static final String ESCAPE = "`";


    @Override
    public String getTableName(Class<?> entityClass) {
        return ESCAPE + super.getTableName(entityClass) + ESCAPE;
    }

    @Override
    public String getTableAlas() {
        return ESCAPE + super.getTableAlas() + ESCAPE;
    }

    @Override
    public String getColumnName(Field field) {
        return ESCAPE + EntityUtils.getFieldColumnName(field) + ESCAPE;
    }

    @Override
    public String getFieldName(Field field) {
        return ESCAPE + field.getName() + ESCAPE;
    }


    @Override
    public String getLikeParam(String paramName, QueryLikeType likeType) {
        // "CONCAT('%',#{keyword},'%')"
        String fieldParam = getFieldParam(paramName);
        StringBuilder result = new StringBuilder("CONCAT(");
        if (likeType == QueryLikeType.MIDDLE || likeType == QueryLikeType.RIGHT) {
            result.append("'%', ");
        }
        result.append(fieldParam);
        if (likeType == QueryLikeType.MIDDLE || likeType == QueryLikeType.LEFT) {
            result.append(", '%'");
        }
        result.append(")");
        return result.toString();
    }
}
