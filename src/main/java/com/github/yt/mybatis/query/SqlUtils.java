package com.github.yt.mybatis.query;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.DialectFactory;
import com.github.yt.mybatis.util.EntityUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {

    private static List<String> getSelectColumnList(Class<?> entityClass, Set<String> excludeColumnSet, final String aliasName) {
        List<String> columnList = new ArrayList<>();
        List<Field> fieldList = EntityUtils.getTableFieldList(entityClass);
        fieldList.forEach(field -> {
            field.setAccessible(true);
            String fieldColumnName = EntityUtils.getFieldColumnName(field);
            String fieldName = field.getName();
            String fieldAliasName = aliasName + "." + fieldColumnName;
            // 排除字段
            if (!excludeColumnSet.contains(fieldColumnName) && !excludeColumnSet.contains(fieldAliasName)) {
                if (!fieldColumnName.equals(fieldName)) {
                    fieldAliasName = fieldAliasName + " as " + fieldName;
                }
                columnList.add(fieldAliasName);
            }
        });
        return columnList;
    }

    public static String getUpdateSet(MybatisQuery query) {
        String set;
        set = YtStringUtils.join(query.takeUpdateColumnList().toArray(), ", ");
        return " set " + set + " ";
    }

    /**
     * 获取 select ... from ... 子句
     * <p>
     * takeExtendSelectColumnList 扩展的查询字段
     * takeExcludeSelectColumnList 排除的查询字段
     * takeExcludeAllSelectColumn 排除所有的查询字段
     *
     * @param entityClass entityClass
     * @param query       query
     * @return select
     */
    public static String getSelectAndFrom(Class entityClass, MybatisQuery query) {
        List<String> columnList;
        if (query != null) {
            // 添加所有扩展字段
            columnList = new ArrayList<String>(query.takeExtendSelectColumnList());
            // 是否排除所有 t 中的字段
            if (!query.takeExcludeAllSelectColumn()) {
                columnList.addAll(getEntitySelectColumn(entityClass, query.takeExcludeSelectColumnList()));
            }
        } else {
            columnList = getEntitySelectColumn(entityClass, null);
        }

        return "select " + YtStringUtils.join(columnList.toArray(), ", ")
                + " from " + EntityUtils.getTableName(entityClass) + " t ";
    }

    private static List<String> getEntitySelectColumn(Class entityClass, List<String> excludeSelectColumnList) {
        Set<String> excludeColumnSet = new HashSet<>();
        if (excludeSelectColumnList != null && excludeSelectColumnList.size() > 0) {
            excludeSelectColumnList.forEach(excludeSelectColumn -> {
                String[] excludeColumns = excludeSelectColumn.split(",");
                for (String excludeColumn : excludeColumns) {
                    excludeColumnSet.add(excludeColumn.trim());
                }
            });
        }
        List<String> columnList = getSelectColumnList(entityClass, excludeColumnSet, "t");
        return columnList;
    }

    public static String getSelectCountAndFrom(Class entityClass) {
        return "select count(*) from " + EntityUtils.getTableName(entityClass) + " t ";
    }


    public static String getJoinAndOnCondition(MybatisQuery query) {
        if (query == null) {
            return "";
        }
        StringBuffer resultBuffer = new StringBuffer();
        query.takeJoinList().forEach(join -> {
            QueryJoin queryJoin = (QueryJoin) join;

            resultBuffer.append(queryJoin.takeJoinType().getValue() + queryJoin.takeTableNameAndOnConditions() + " ");
        });
        return resultBuffer.toString();
    }

    public static String getWhere(Object entityCondition, MybatisQuery query) {
        return getWhere(entityCondition, query, " t.");
    }

    public static String getWhere(Object entityCondition, MybatisQuery query, String alias) {
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
     *
     * @param sql   sql 语句
     * @param query query对象
     * @return 新的语句
     */
    public static StringBuffer replaceInParam(StringBuffer sql, MybatisQuery query) {
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
            QueryInCondition queryInCondition = (QueryInCondition) inCondition;
            String inSql;
            String column = queryInCondition.takeParam().replaceAll("\\.", "__");
            if (queryInCondition.takeValues() == null || queryInCondition.takeValues().isEmpty()) {
                inSql = "(null)";
            } else {
                // #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
                List<String> inParamList = new ArrayList<>();
                for (int i = 0; i < queryInCondition.takeValues().size(); i++) {
                    inParamList.add("#{" + ParamUtils.IN_CONDITION + "." + column + "[" + i + "]}");
                }
                inSql = "(" + YtStringUtils.join(inParamList.toArray(), ", ") + ")";
            }
            inParamListMap.put(column, inSql);
        });
        return new StringBuffer(format(sql.toString(), inParamListMap));
    }

    public static String getOrderBy(MybatisQuery query) {
        if (query != null && query.takeOrderByList() != null && !query.takeOrderByList().isEmpty()) {
            return " ORDER BY " + YtStringUtils.join(query.takeOrderByList().toArray(), ", ") + " ";
        } else {
            return "";
        }
    }

    public static String getGroupBy(MybatisQuery query) {
        if (query != null && YtStringUtils.isNotBlank(query.takeGroupBy())) {
            return " GROUP BY " + query.takeGroupBy() + " ";
        } else {
            return "";
        }
    }

    public static String getLimit(MybatisQuery query) {
        return DialectFactory.create().limitSql(query);
    }

    /**
     * 替换字符串中${values} 变量为指定字符串
     *
     * @param template 要替换的字符串
     * @param params   替换参数
     * @return 替换后的字符串
     */
    public static String format(String template, Map<String, Object> params) {
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
