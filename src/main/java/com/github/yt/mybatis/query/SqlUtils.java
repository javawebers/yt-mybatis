package com.github.yt.mybatis.query;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.util.EntityUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liujiasheng
 */
public class SqlUtils {
    public static void select(SQL sql, Class<?> entityClass, MybatisQuery<?> query) {
        List<String> columnList;
        if (query != null) {
            // 添加所有扩展字段
            columnList = new ArrayList<>(query.takeExtendSelectColumnList());
            // 是否排除所有 t 中的字段
            if (!query.takeExcludeAllSelectColumn()) {
                columnList.addAll(getEntitySelectColumn(entityClass, query.takeExcludeSelectColumnList()));
            }
        } else {
            columnList = getEntitySelectColumn(entityClass, null);
        }
        for (String column : columnList) {
            sql.SELECT(column);
        }
    }

    public static void from(SQL sql, Class<?> entityClass) {
        sql.FROM(DialectHandler.getDialect().getTableNameWithAlas(entityClass));
    }

    public static void join(SQL sql, MybatisQuery<?> query) {
        if (query == null) {
            return;
        }
        query.takeJoinList().forEach(queryJoin -> {
            switch (queryJoin.takeJoinType()) {
                case JOIN:
                    sql.JOIN(queryJoin.takeTableNameAndOnConditions());
                    break;
                case LEFT_JOIN:
                    sql.LEFT_OUTER_JOIN(queryJoin.takeTableNameAndOnConditions());
                    break;
                case RIGHT_JOIN:
                    sql.RIGHT_OUTER_JOIN(queryJoin.takeTableNameAndOnConditions());
                    break;
                default:
                    throw new NullArgumentException("连接类型不能为空");
            }
        });
    }

    public static void set(SQL sql, MybatisQuery<?> query) {
        if (query.takeUpdateColumnList() != null) {
            for (Object set : query.takeUpdateColumnList()) {
                sql.SET((String) set);
            }
        }
    }


    public static void where(SQL sql, MybatisQuery<?> query) {
        if (query != null) {
            // query.whereList
            // 拼接where查询条件
            if (query.takeWhereList() != null && !query.takeWhereList().isEmpty()) {
                for (Object where : query.takeWhereList()) {
                    sql.WHERE((String) where);
                }
            }
        }
    }

    public static void groupBy(SQL sql, MybatisQuery<?> query) {
        if (query != null && YtStringUtils.isNotBlank(query.takeGroupBy())) {
            sql.GROUP_BY(query.takeGroupBy());
        }
    }

    public static void orderBy(SQL sql, MybatisQuery<?> query) {
        if (query != null && query.takeOrderByList() != null && !query.takeOrderByList().isEmpty()) {
            for (Object orderBy : query.takeOrderByList()) {
                sql.ORDER_BY((String) orderBy);
            }
        }
    }

    public static String limitOffset(String sql, MybatisQuery<?> query) {
        if (query != null) {
            return DialectHandler.getDialect().limitOffset(sql, query.takeLimitFrom(), query.takeLimitSize());
        } else {
            return sql;
        }
    }


    private static List<String> getEntitySelectColumn(Class<?> entityClass, List<String> excludeSelectColumnList) {
        Set<String> excludeColumnSet = new HashSet<>();
        if (excludeSelectColumnList != null && excludeSelectColumnList.size() > 0) {
            excludeSelectColumnList.forEach(excludeSelectColumn -> {
                String[] excludeColumns = excludeSelectColumn.split(",");
                for (String excludeColumn : excludeColumns) {
                    excludeColumnSet.add(excludeColumn.trim());
                }
            });
        }
        return getSelectColumnList(entityClass, excludeColumnSet);
    }


    private static List<String> getSelectColumnList(Class<?> entityClass, Set<String> excludeColumnSet) {
        List<String> columnList = new ArrayList<>();
        List<Field> fieldList = EntityUtils.getTableFieldList(entityClass);
        fieldList.forEach(field -> {
            field.setAccessible(true);
            String fieldColumnName = EntityUtils.getFieldColumnName(field);
            String fieldAliasName = DialectHandler.getDialect().getColumnNameWithTableAlas(field);
            // 排除字段
            if (!excludeColumnSet.contains(fieldColumnName)) {
                if (!fieldColumnName.equals(field.getName())) {
                    fieldAliasName = fieldAliasName + " as " + DialectHandler.getDialect().getFieldName(field);
                }
                columnList.add(fieldAliasName);
            }
        });
        return columnList;
    }

    /**
     * 将sql中的 in 参数替换
     * #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
     *
     * @param sql   sql 语句
     * @param query query对象
     * @return 新的语句
     */
    public static String replaceInParam(SQL sql, MybatisQuery<?> query) {
        if (query == null) {
            return sql.toString();
        }
        if (query.takeInParamList().size() == 0) {
            return sql.toString();
        }
        // query.inParamList
        // 替换查询条件中的in参数
        Map<String, Object> inParamListMap = new HashMap<>(16);
        query.takeInParamList().forEach(queryInCondition -> {
            String inSql;
            String column = queryInCondition.takeParam().replaceAll("\\.", "__");
            if (queryInCondition.takeValues() == null || queryInCondition.takeValues().isEmpty()) {
                inSql = "(null)";
            } else {
                // #{_inCondition_.t__userId[0]}, #{_inCondition_.t__userId[1]}
                List<String> inParamList = new ArrayList<>();
                for (int i = 0; i < queryInCondition.takeValues().size(); i++) {
                    inParamList.add(DialectHandler.getDialect().getFieldParam(ParamUtils.IN_CONDITION + "." + column + "[" + i + "]"));
                }
                inSql = "(" + YtStringUtils.join(inParamList.toArray(), ", ") + ")";
            }
            inParamListMap.put(column, inSql);
        });
        return format(sql.toString(), inParamListMap);
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
        String s = "\\$\\{\\s*\\w+\\s*}";
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
