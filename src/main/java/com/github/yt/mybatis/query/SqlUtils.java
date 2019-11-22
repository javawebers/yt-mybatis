package com.github.yt.mybatis.query;


import com.github.yt.commons.query.Query;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.DialectFactory;
import com.github.yt.mybatis.util.EntityUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {

    private static LinkedHashSet<String> getSelectColumnSet(Class<?> entityClass, final String aliasName) {
        LinkedHashSet<String> columnSet = new LinkedHashSet<>();
        List<Field> fieldList = EntityUtils.getTableFieldList(entityClass);
        fieldList.forEach(field -> {
            field.setAccessible(true);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && YtStringUtils.isNotEmpty(columnAnnotation.name())) {
                String annotationName = aliasName + "." + columnAnnotation.name();
                columnSet.add(annotationName + " as " + field.getName());
            } else {
                columnSet.add(aliasName + "." + field.getName());
            }
        });
        return columnSet;
    }

    public static String getUpdateSet(Query query){
        String set;
        set = YtStringUtils.join(query.takeUpdateColumnList().toArray(), ", ");
        return " set " + set + " ";
    }

    public static String getSelectAndFrom(Class entityClass, Query query) {
        String columns;
        if (query != null && query.takeSelectColumnList() != null && !query.takeSelectColumnList().isEmpty()) {
            columns = YtStringUtils.join(query.takeSelectColumnList().toArray(), ", ");
        } else {
            LinkedHashSet<String> columnSet = getSelectColumnSet(entityClass, "t");
            StringBuilder columnSb = new StringBuilder();
            int i = 0;
            for (String column : columnSet) {
                i++;
                columnSb.append(column);
                if (i < columnSet.size()) {
                    columnSb.append(", ");
                }
            }
            columns = columnSb.toString();
        }
        return "select " + columns + " from " + EntityUtils.getTableName(entityClass) + " t ";
    }

    public static String getSelectCountAndFrom(Class entityClass) {
        // TODO count(*),count(1),count(id)
        return "select count(1) from " + EntityUtils.getTableName(entityClass) + " t ";
    }


    public static String getJoinAndOnCondition(Query query) {
        if (query == null) {
            return "";
        }
        StringBuffer resultBuffer = new StringBuffer();
        query.takeJoinList().forEach(join -> {
            resultBuffer.append(join.takeJoinType().getValue() + join.takeTableNameAndOnConditions() + " ");
        });
        return resultBuffer.toString();
    }

    public static String getWhere(Object entityCondition, Query query) {
        return getWhere(entityCondition, query, " t.");
    }

    public static String getWhere(Object entityCondition, Query query, String alias) {
        StringBuffer resultBuffer = new StringBuffer(" where 1=1 ");
        if (entityCondition != null) {
            StringBuilder where = new StringBuilder();
            List<Field> fieldList = EntityUtils.getTableFieldList(entityCondition.getClass());
            for (Field field : fieldList) {
                Object value = EntityUtils.getValue(entityCondition, field);
                String columnName = EntityUtils.getFieldColumnName(field);
                if (value != null) {
                    where.append(" and ").append(alias).append(columnName).append(" = #{").append(ParamUtils.ENTITY_CONDITION).append(".").append(field.getName()).append("}");
                }
            }
            resultBuffer.append(where);
        }
        if (query != null) {
            // query.whereList
            // 拼接where查询条件
            if (query.takeWhereList() != null && !query.takeWhereList().isEmpty()) {
                String where = YtStringUtils.join(query.takeWhereList().toArray(), ") and (");
                resultBuffer.append(" and (").append(where).append(")");
            }
        }
        return resultBuffer.toString();
    }

    /**
     * 将sql中的 in 参数替换
     * #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
     * @param sql sql 语句
     * @param query query对象
     * @return 新的语句
     */
    public static StringBuffer replaceInParam(StringBuffer sql, Query query){
        if (query == null) {
            return sql;
        }
        if (query.takeInParamList().size() == 0) {
            return sql;
        }
        // query.inParamList
        // 替换查询条件中的in参数
        Map<String, Object> inParamListMap = new HashMap<>();
        query.takeInParamList().forEach(inCondition -> {
            String inSql;
            String column = inCondition.takeParam().replaceAll("\\.", "__");
            if (inCondition.takeValues() == null || inCondition.takeValues().isEmpty()) {
                inSql = "(null)";
            } else {
                // #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
                List<String> inParamList = new ArrayList<>();
                for (int i = 0; i < inCondition.takeValues().size(); i++) {
                    inParamList.add("#{" + ParamUtils.IN_CONDITION + "." + column + "[" + i + "]}");
                }
                inSql = "(" + YtStringUtils.join(inParamList.toArray(), ", ") + ")";
            }
            inParamListMap.put(column, inSql);
        });
        return new StringBuffer(format(sql.toString(), inParamListMap));
    }

    public static String getOrderBy(Query query) {
        if (query != null && query.takeOrderByList() != null && !query.takeOrderByList().isEmpty()) {
            return " ORDER BY " + YtStringUtils.join(query.takeOrderByList().toArray(), ", ") + " ";
        } else {
            return "";
        }
    }

    public static String getGroupBy(Query query) {
        if (query != null && YtStringUtils.isNotBlank(query.takeGroupBy())) {
            return " GROUP BY " + query.takeGroupBy() + " ";
        } else {
            return "";
        }
    }

    public static String getLimit(Query query) {
        return DialectFactory.create().limitSql(query);
    }

    /**
     * 替换字符串中${values} 变量为指定字符串
     * @param template 要替换的字符串
     * @param params 替换参数
     * @return 替换后的字符串
     */
    public static String format(String template, Map<String, Object> params){
        StringBuffer sb = new StringBuffer();
        String s = "\\$\\{\\s*\\w+\\s*\\}";
        Matcher m = Pattern.compile(s).matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1).trim());
            if (value != null) {
                m.appendReplacement(sb, value.toString());
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
