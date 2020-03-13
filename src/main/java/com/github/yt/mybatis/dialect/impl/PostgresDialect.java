package com.github.yt.mybatis.dialect.impl;

import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.mybatis.query.QueryLikeType;
import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;

/**
 * Postgres 实现
 *
 * @author sheng
 */
public class PostgresDialect extends BaseDialect {

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
