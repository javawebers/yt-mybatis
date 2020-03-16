package com.github.yt.mybatis.dialect.impl;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.query.QueryLikeType;
import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * mysql 实现
 *
 * @author sheng
 */
public class OracleDialect extends BaseDialect {

    @Override
    public String getInsertSql(Collection<?> entityCollection) {

        Class<?> entityClass = EntityUtils.getEntityClass(entityCollection);
        List<Field> tableFieldList = EntityUtils.getTableFieldList(entityClass);

        String tableName = getTableName(entityClass);
        StringBuilder stringBuffer = new StringBuilder("INSERT ");
        if (entityCollection.size() > 1) {
            stringBuffer.append("All ");
        }

        List<?> entityList = new ArrayList<>(entityCollection);
        // 收集所有有值的字段，没有值的字段不拼接 sql
        for (int i = 0; i < entityList.size(); i++) {
            Object entity = entityList.get(i);
            List<String> columnNameList = new ArrayList<>();
            List<String> valueNameList = new ArrayList<>();
            for (Field field : tableFieldList) {
                if (EntityUtils.getValue(entity, field) != null) {
                    columnNameList.add(DialectHandler.getDialect().getColumnName(field));
                    valueNameList.add("#{collection[" + i + "]." + field.getName() + "}");
                }
            }
            stringBuffer.append("\n\t INTO ").append(tableName);
            stringBuffer.append(" (").append(YtStringUtils.join(columnNameList, ", ")).append(") ");
            stringBuffer.append(" VALUES ");
            stringBuffer.append(" (").append(YtStringUtils.join(valueNameList, ", ")).append(") ");
        }
        if (entityCollection.size() > 1) {
            stringBuffer.append("SELECT * FROM dual");
        }
        return stringBuffer.toString();
    }

    @Override
    public String limitOffset(String sql, Integer limitFrom, Integer limitSize) {
        if (limitFrom != null && limitSize != null) {
            return "SELECT * FROM (SELECT t_limit_offset_end_.*, ROWNUM AS t_end_row_no_ FROM (" +
                    sql +
                    ") t_limit_offset_end_ WHERE ROWNUM <= " + (limitFrom + limitSize) + ") t_limit_offset_begin_ WHERE t_limit_offset_begin_.t_end_row_no_ > " + limitFrom;
        } else {
            return sql;
        }
    }

    @Override
    public String getFieldParam(String paramName) {
        return "#{" + paramName + ", jdbcType=VARCHAR}";
    }

    @Override
    public String getLikeParam(String paramName, QueryLikeType likeType) {
        // "'%'||#{keyword}||'%'"
        String fieldParam = getFieldParam(paramName);
        StringBuilder result = new StringBuilder();
        if (likeType == QueryLikeType.MIDDLE || likeType == QueryLikeType.RIGHT) {
            result.append("'%'||");
        }
        result.append(fieldParam);
        if (likeType == QueryLikeType.MIDDLE || likeType == QueryLikeType.LEFT) {
            result.append("||'%'");
        }
        return result.toString();
    }
}
