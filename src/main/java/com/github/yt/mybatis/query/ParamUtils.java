package com.github.yt.mybatis.query;

import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.util.EntityUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sheng
 */
public class ParamUtils {

    public static final String QUERY_OBJECT = "_queryObject_";
    public static final String ENTITY_CONDITION_CLASS = "_entityConditionClass_";
    public static final String IN_CONDITION = "_inCondition_";

    public static void setPageInfo(MybatisQuery<?> query) {
        if (query.takePageNo() == null) {
            query.makePageNo(1);
        }
        if (query.takePageSize() == null) {
            query.makePageSize(10);
        }
    }

    public static <T> Map<String, Object> getParamMap(T entityCondition, MybatisQuery<?> query, boolean withAlias) {
        org.springframework.util.Assert.notNull(entityCondition, "对象查询条件不能为空");
        Map<String, Object> param = new HashMap<>(16);
        // query对象传递
        param.put(QUERY_OBJECT, query);
        // 实体参数查询条件
        param.put(ENTITY_CONDITION_CLASS, entityCondition.getClass());

        List<Field> fieldList = EntityUtils.getTableFieldList(entityCondition.getClass());
        for (Field field : fieldList) {
            Object value = EntityUtils.getValue(entityCondition, field);
            if (value != null) {
                if (query == null) {
                    query = new Query();
                }
                String columnName;
                if (withAlias) {
                    columnName = DialectHandler.getDialect().getColumnNameWithTableAlas(field);
                } else {
                    columnName = DialectHandler.getDialect().getColumnName(field);
                }
                query.equal(columnName, value);
            }
        }

        // query中的参数
        if (query != null) {
            // addParam中的参数
            query.takeParam().forEach(param::put);
            // in
            Map<String, Object[]> inParamMap = new HashMap<>(16);
            query.takeInParamList().forEach(inCondition -> {
                String column = inCondition.takeParam().replaceAll("\\.", "__");
                for (int i = 0; i < inCondition.takeValues().size(); i++) {
                    inParamMap.put(column, inCondition.takeValues().toArray());
                }
                param.put(ParamUtils.IN_CONDITION, inParamMap);
            });
        }
        return param;
    }

}
