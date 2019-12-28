package com.github.yt.mybatis.mapper;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.SqlUtils;
import com.github.yt.mybatis.util.EntityUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author liujiasheng
 */
public class BaseMapperProvider {

    public <T> String saveBatch(Map<String, Collection<T>> map) {
        Collection<T> entityCollection = map.get("collection");

        String tableName = null;
        List<Field> tableFieldList = null;
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldNameList = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        List<List<String>> valuesList = new ArrayList<>();

        // 收集所有有值的字段，没有值的字段不拼接 sql
        for (T entity : entityCollection) {
            if (YtStringUtils.isBlank(tableName)) {
                tableName = EntityUtils.getTableName(entity.getClass());
                tableFieldList = EntityUtils.getTableFieldList(entity.getClass());
            }
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
            valuesList.add(values);
        }

        // mysql
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        for (String column : columnNameList) {
            sql.INTO_COLUMNS(column);
        }
        for (List<String> values : valuesList) {
            for (String value : values) {
                sql.INTO_VALUES(value);
            }
            sql.ADD_ROW();
        }
        return sql.toString();
    }

    public String delete(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        sql.DELETE_FROM(DialectHandler.getDialect().getTableName(entityCondition.getClass()));
        SqlUtils.where(sql, entityCondition, query, false);
        return SqlUtils.replaceInParam(sql, query);
    }

    public <T> String update(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        sql.UPDATE(DialectHandler.getDialect().getTableNameWithAlas(entityCondition.getClass()));
        SqlUtils.set(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        return SqlUtils.replaceInParam(sql, query);
    }

    public String findList(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        SqlUtils.select(sql, entityCondition.getClass(), query);
        SqlUtils.from(sql, entityCondition.getClass());
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        SqlUtils.groupBy(sql, query);
        SqlUtils.orderBy(sql, query);
        SqlUtils.limitOffset(sql, query);
        return SqlUtils.replaceInParam(sql, query);
    }

    public String count(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        sql.SELECT("count(*)");
        SqlUtils.from(sql, entityCondition.getClass());
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        SqlUtils.groupBy(sql, query);
        return SqlUtils.replaceInParam(sql, query);
    }


}
