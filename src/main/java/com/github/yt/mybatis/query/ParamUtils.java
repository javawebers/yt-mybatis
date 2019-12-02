package com.github.yt.mybatis.query;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    public static final String QUERY_OBJECT = "_queryObject_";
    public static final String ENTITY_CONDITION = "_entityCondition_";
    public static final String IN_CONDITION = "_inCondition_";
    public static final String NOT_IN_CONDITION = "_notInCondition_";


    public static <T> T setPageInfo(MybatisQuery query) {
        if (query.takePageNo() == null) {
            query.makePageNo(1);
        }
        if (query.takePageSize() == null) {
            query.makePageSize(20);
        }
        return (T)query;
    }

    public static <T> Map<String, Object> getParamMap(T entityCondition, MybatisQuery query) {
        Map<String, Object> param = new HashMap<>();
        // query对象传递
        param.put(QUERY_OBJECT, query);
        // 实体参数查询条件
        param.put(ENTITY_CONDITION, entityCondition);
        // query中的参数
        if (query != null) {
            // addParam中的参数
            query.takeParam().forEach((paramName, paramValue) -> {
                param.put((String) paramName, paramValue);
            });
            // in
            Map<String, Object[]> inParamMap = new HashMap<>();
            query.takeInParamList().forEach(inCondition -> {
                String column = ((QueryInCondition) inCondition).takeParam().replaceAll("\\.", "__");
                for (int i = 0; i < ((QueryInCondition) inCondition).takeValues().size(); i++) {
                    inParamMap.put(column, ((QueryInCondition) inCondition).takeValues().toArray());
                }
                param.put(ParamUtils.IN_CONDITION, inParamMap);
            });
        }
        return param;
    }

}
