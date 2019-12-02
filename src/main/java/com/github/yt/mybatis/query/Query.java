package com.github.yt.mybatis.query;

import com.github.yt.commons.query.PageQuery;

import java.util.*;

/**
 * 查询条件类
 *
 * @author liujiasheng
 */
public class Query implements MybatisQuery<Query> {

    protected Integer pageNo;
    protected Integer pageSize;

    protected boolean updateBaseColumn = true;

    final protected List<String> updateColumnList = new ArrayList<>();
    final protected List<String> selectColumnList = new ArrayList<>();
    final protected List<String> whereList = new ArrayList<>();

    final protected List<QueryInCondition> inParamList = new ArrayList<>();
    final protected List<String> orderByList = new ArrayList<>();
    protected String groupBy = "";
    protected Integer limitFrom;
    protected Integer limitSize;
    final protected List<QueryJoin> joinList = new ArrayList<>();
    final protected Map<String, Object> param = new HashMap<>();

    @Override
    public Query updateBaseColumn(boolean updateBaseColumn) {
        this.updateBaseColumn = updateBaseColumn;
        return this;
    }

    @Override
    public boolean takeUpdateBaseColumn() {
        return updateBaseColumn;
    }

    @Override
    public Query addParam(String paramName, Object paramValue) {
        if (paramValue instanceof Collection) {
            addInParam(paramName, (Collection) paramValue);
        } else {
            param.put(paramName, paramValue);
        }
        return this;
    }

    private Query addInParam(String paramName, Collection paramValue) {
        inParamList.add(new QueryInCondition(paramName, paramValue));
        return this;
    }


    @Override
    public Query addSelectColumn(String selectColumn) {
        selectColumnList.add(selectColumn);
        return this;
    }

    @Override
    public Query addUpdate(String updateColumn) {
        updateColumnList.add(updateColumn);
        return this;
    }


    @Override
    public Query addWhere(String where) {
        whereList.add(where);
        return this;
    }

    @Override
    public Query addOrderBy(String columns) {
        orderByList.add(columns);
        return this;
    }

    @Override
    public Query addGroupBy(String columns) {
        groupBy = columns;
        return this;
    }

    @Override
    public Query limit(int from, int size) {
        limitFrom = from;
        limitSize = size;
        return this;
    }


    @Override
    public Query addJoin(QueryJoinType joinType,
                         String tableNameAndOnConditions) {
        joinList.add(new QueryJoin(joinType, tableNameAndOnConditions));
        return this;
    }

    ////// take 参数
    @Override
    public Map<String, Object> takeParam() {
        return param;
    }

    @Override
    public List<QueryInCondition> takeInParamList() {
        return inParamList;
    }

    @Override
    public List<String> takeUpdateColumnList() {
        return updateColumnList;
    }

    @Override
    public List<String> takeSelectColumnList() {
        return selectColumnList;
    }

    @Override
    public List<String> takeWhereList() {
        return whereList;
    }


    @Override
    public List<String> takeOrderByList() {
        return orderByList;
    }

    @Override
    public String takeGroupBy() {
        return groupBy;
    }

    @Override
    public List<QueryJoin> takeJoinList() {
        return joinList;
    }


    ///////////////////////////////
    @Override
    public Query makePageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    @Override
    public Query makePageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public Integer takePageNo() {
        return pageNo;
    }

    @Override
    public Integer takePageSize() {
        return pageSize;
    }

    @Override
    public Integer takeLimitFrom() {
        return limitFrom;
    }

    @Override
    public Integer takeLimitSize() {
        return limitSize;
    }

}
