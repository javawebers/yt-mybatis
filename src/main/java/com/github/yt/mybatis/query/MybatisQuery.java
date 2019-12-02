package com.github.yt.mybatis.query;

import com.github.yt.commons.query.PageQuery;

import java.util.List;
import java.util.Map;

/**
 * mybatis 查询条件
 */
public interface MybatisQuery<T extends MybatisQuery> extends PageQuery<T> {

    // apis

    /**
     * 更新基本字段,如 modifyTime, modifierId, modifierName
     * @param updateBaseColumn true/false
     * @return this
     */
    T updateBaseColumn(boolean updateBaseColumn);

    T addParam(String paramName, Object paramValue);

    T addSelectColumn(String selectColumn);

    T addUpdate(String updateColumn);

    T addWhere(String where);

    T addOrderBy(String columns);

    T addGroupBy(String columns);

    T limit(int from, int size);

    T addJoin(QueryJoinType joinType, String tableNameAndOnConditions);

    ////// take 参数
    Map<String, Object> takeParam();

    boolean takeUpdateBaseColumn();

    List<QueryInCondition> takeInParamList();

    List<String> takeUpdateColumnList();

    List<String> takeSelectColumnList();

    List<String> takeWhereList();

    List<String> takeOrderByList();

    String takeGroupBy();

    List<QueryJoin> takeJoinList();

    Integer takeLimitFrom();

    Integer takeLimitSize();

}
