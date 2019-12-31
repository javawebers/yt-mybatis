package com.github.yt.mybatis.example.dao;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;
import java.util.*;

public class IntIdProvider {

    public <T> String save(T entity) {
        String tableName = EntityUtils.getTableName(entity.getClass());
        List<Field> fieldList = EntityUtils.getTableFieldList(entity.getClass());
        // 下面三个变量存字段不为空的 column 值
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldColumnNameList = new ArrayList<>();
        List<String> dbFieldColumnNameList = new ArrayList<>();
        for (Field field : fieldList) {
            if (!fieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                if (EntityUtils.getValue(entity, field) != null) {
                    fieldColumnNameSet.add(EntityUtils.getFieldColumnName(field));
                    fieldColumnNameList.add(EntityUtils.getFieldColumnName(field));
                    dbFieldColumnNameList.add("`" + EntityUtils.getFieldColumnName(field) + "`");
                }
            }
        }
        StringBuffer valueParams = new StringBuffer();
        valueParams.append("(");
        for (int j = 0; j < fieldColumnNameList.size(); j++) {
            String fieldColumnName = fieldColumnNameList.get(j);
            valueParams.append("#{").append(fieldColumnName).append("}");
            if (j != fieldColumnNameList.size() - 1) {
                valueParams.append(", ");
            }
        }
        valueParams.append(")");

        StringBuffer sql = new StringBuffer();
        sql.append("<script>");
        // insert into
        sql.append("insert into `").append(tableName).append("` ");
        // fields
        sql.append(" (").append(YtStringUtils.join(dbFieldColumnNameList.toArray(), ", ")).append(") ");
        // values
        sql.append(" values ").append(valueParams);
        sql.append("</script>");
        System.out.println(sql.toString());
        return sql.toString();
    }

    public <T> String saveBatch(Map map) {
        Collection<T> entityCollection = (List<T>)map.get("collection");
        Class<T> entityClass = EntityUtils.getEntityClass(entityCollection);
        String tableName = EntityUtils.getTableName(entityClass);
        List<Field> fieldList = EntityUtils.getTableFieldList(entityClass);
        // 下面三个变量存字段不为空的 column 值
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldColumnNameList = new ArrayList<>();
        List<String> dbFieldColumnNameList = new ArrayList<>();
        for (T entity : entityCollection) {
            for (Field field : fieldList) {
                if (!fieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldColumnNameSet.add(EntityUtils.getFieldColumnName(field));
                        fieldColumnNameList.add(EntityUtils.getFieldColumnName(field));
                        dbFieldColumnNameList.add("`" + EntityUtils.getFieldColumnName(field) + "`");
                    }
                }
            }
        }
        StringBuffer valueParams = new StringBuffer();

        for (int i = 0; i < entityCollection.size(); i++) {
            valueParams.append("(");
            for (int j = 0; j < fieldColumnNameList.size(); j++) {
                String fieldColumnName = fieldColumnNameList.get(j);
                valueParams.append("#{list[").append(i).append("].").append(fieldColumnName).append("}");
                if (j != fieldColumnNameList.size() - 1) {
                    valueParams.append(", ");
                }
            }
            valueParams.append(")");
            if (i != entityCollection.size() - 1) {
                valueParams.append(", ");
            }
        }
        StringBuffer sql = new StringBuffer();
        // insert into
        sql.append("insert into `").append(tableName).append("` ");
        // fields
        sql.append(" (").append(YtStringUtils.join(dbFieldColumnNameList.toArray(), ", ")).append(") ");
        // values
        sql.append(" values ").append(valueParams);
        System.out.println(sql.toString());
        return sql.toString();
    }
}
