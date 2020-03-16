package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.query.QueryLikeType;
import com.github.yt.mybatis.util.EntityUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.*;

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

    @Override
    public String getTableName(Class<?> entityClass) {
        return EntityUtils.getTableName(entityClass);
    }

    @Override
    public String getTableNameWithAlas(Class<?> entityClass) {
        return getTableName(entityClass) + " " + getTableAlas();
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
    public String getFieldName(Field field) {
        return field.getName();
    }

    @Override
    public String getColumnNameWithTableAlas(Field field) {
        return getTableAlas() + POINT + getColumnName(field);
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

    @Override
    public String getInsertSql(Collection<?> entityCollection) {

        Class<?> entityClass = EntityUtils.getEntityClass(entityCollection);
        List<Field> tableFieldList = EntityUtils.getTableFieldList(entityClass);
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldNameList = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        List<List<String>> valueNamesList = new ArrayList<>();

        // 收集所有有值的字段，没有值的字段不拼接 sql
        for (Object entity : entityCollection) {
            for (Field field : tableFieldList) {
                if (!fieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldColumnNameSet.add(EntityUtils.getFieldColumnName(field));
                        fieldNameList.add(field.getName());
                        columnNameList.add(DialectHandler.getDialect().getColumnName(field));
                    }
                }
            }
        }
        for (int i = 0; i < entityCollection.size(); i++) {
            List<String> values = new ArrayList<>();
            for (String fieldColumnName : fieldNameList) {
                values.add("#{collection[" + i + "]." + fieldColumnName + "}");
            }
            valueNamesList.add(values);
        }

        // mysql
        SQL sql = new SQL();
        sql.INSERT_INTO(getTableName(entityClass));
        for (String column : columnNameList) {
            sql.INTO_COLUMNS(column);
        }
        for (List<String> values : valueNamesList) {
            for (String value : values) {
                sql.INTO_VALUES(value);
            }
            sql.ADD_ROW();
        }
        return sql.toString();
    }

    @Override
    public String limitOffset(String sql, Integer limitFrom, Integer limitSize) {
        if (limitFrom != null && limitSize != null) {
            return sql + " LIMIT " + limitSize + " OFFSET " + limitFrom;
        } else {
            return sql;
        }
    }

    @Override
    public String getFieldParam(String paramName) {
        return "#{" + paramName + "}";
    }

}
