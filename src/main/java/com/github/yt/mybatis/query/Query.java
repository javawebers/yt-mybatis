package com.github.yt.mybatis.query;

import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.dialect.DialectHandler;

import javax.lang.model.element.Element;
import java.lang.reflect.Array;
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
    final protected List<String> extendSelectColumnList = new ArrayList<>();
    final protected List<String> excludeSelectColumnList = new ArrayList<>();
    protected boolean excludeAllSelectColumn = false;

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
            addInParam(paramName, (Collection<?>) paramValue);
        } else {
            param.put(paramName, paramValue);
        }
        return this;
    }

    private void addInParam(String paramName, Collection<?> paramValue) {
        inParamList.add(new QueryInCondition(paramName, paramValue));
    }

    @Override
    public Query addExtendSelectColumn(String extendSelectColumn) {
        extendSelectColumnList.add(extendSelectColumn);
        return this;
    }

    @Override
    public Query addExcludeSelectColumn(String excludeSelectColumn) {
        excludeSelectColumnList.add(excludeSelectColumn);
        return this;
    }

    @Override
    public Query excludeAllSelectColumn() {
        excludeAllSelectColumn = true;
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

    @Override
    public Query gt(String columnName, Object value) {
        if (value != null) {
            String randomColumnName = "_gt_" + generateRandomColumn(columnName);
            this.addWhere(columnName + " > " + DialectHandler.getDialect().getFieldParam(randomColumnName));
            this.addParam(randomColumnName, value);
        }
        return this;
    }

    @Override
    public Query ge(String columnName, Object value) {
        if (value != null) {
            String randomColumnName = "_ge_" + generateRandomColumn(columnName);
            this.addWhere(columnName + " >= " + DialectHandler.getDialect().getFieldParam(randomColumnName));
            this.addParam(randomColumnName, value);
        }
        return this;
    }

    @Override
    public Query lt(String columnName, Object value) {
        if (value != null) {
            String randomColumnName = "_lt_" + generateRandomColumn(columnName);
            this.addWhere(columnName + " < " + DialectHandler.getDialect().getFieldParam(randomColumnName));
            this.addParam(randomColumnName, value);
        }
        return this;
    }

    @Override
    public Query le(String columnName, Object value) {
        if (value != null) {
            String randomColumnName = "_le_" + generateRandomColumn(columnName);
            this.addWhere(columnName + " <= " + DialectHandler.getDialect().getFieldParam(randomColumnName));
            this.addParam(randomColumnName, value);
        }
        return this;
    }

    /**
     * 获取一个字段随机字符串，再末尾拼接uuid
     *
     * @param columnName 字段名，可能存在"."，将 "."转为 "_"
     * @return 字段随机字符串
     */
    private static String generateRandomColumn(String columnName) {
        return columnName.replace(".", "_") + "_" + UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public Query like(String columnName, String value) {
        return this.like(columnName, value, QueryLikeType.MIDDLE);
    }

    @Override
    public Query like(String columnName, String value, QueryLikeType likeType) {
        if (YtStringUtils.isNotEmpty(value)) {
            String randomColumnName = "_like_" + generateRandomColumn(columnName);
            this.addWhere(columnName + " like " + DialectHandler.getDialect().getLikeParam(randomColumnName, likeType));
            this.addParam(randomColumnName, value);
        }
        return this;
    }

    @Override
    public Query equal(String columnName, Object value) {
        String randomColumnName = "_equal_" + generateRandomColumn(columnName);
        this.addWhere(columnName + " = " + DialectHandler.getDialect().getFieldParam(randomColumnName));
        this.addParam(randomColumnName, value);
        return this;
    }

    @Override
    public Query notEqual(String columnName, Object value) {
        String randomColumnName = "_notEqual_" + generateRandomColumn(columnName);
        this.addWhere(columnName + " != " + DialectHandler.getDialect().getFieldParam(randomColumnName));
        this.addParam(randomColumnName, value);
        return this;
    }

    @Override
    public Query in(String columnName, Object firstValue, Object... moreValues) {
        String randomColumnName = "_in_" + generateRandomColumn(columnName);
        this.addWhere(columnName + " IN ${" + randomColumnName + "}");
        this.addParam(randomColumnName, convertToCollection(firstValue, moreValues));
        return this;
    }

    @Override
    public Query notIn(String columnName, Object firstValue, Object... moreValues) {
        String randomColumnName = "_notIn_" + generateRandomColumn(columnName);
        this.addWhere(columnName + " NOT IN ${" + randomColumnName + "}");
        this.addParam(randomColumnName, convertToCollection(firstValue, moreValues));
        return this;
    }

    @Override
    public Query update(String columnName, Object value) {
        String randomColumnName = "_update_" + generateRandomColumn(columnName);
        this.addUpdate(columnName + " = " + DialectHandler.getDialect().getFieldParam(randomColumnName));
        this.addParam(randomColumnName, value);
        return this;
    }

    private Object convertToCollection(Object firstValue, Object... moreValues) {
        if (firstValue instanceof Collection) {
            return firstValue;
        } else if (firstValue.getClass().isArray()) {
            int length = Array.getLength(firstValue);
            Object[] os = new Object[length];
            for (int i = 0; i < os.length; i++) {
                os[i] = Array.get(firstValue, i);
            }
            return Arrays.asList(os);
        } else {
            List<Object> list = new ArrayList<>();
            list.add(firstValue);
            list.addAll(Arrays.asList(moreValues));
            return list;
        }
    }

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
    public List<String> takeExtendSelectColumnList() {
        return extendSelectColumnList;
    }

    @Override
    public List<String> takeExcludeSelectColumnList() {
        return excludeSelectColumnList;
    }

    @Override
    public boolean takeExcludeAllSelectColumn() {
        return excludeAllSelectColumn;
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
