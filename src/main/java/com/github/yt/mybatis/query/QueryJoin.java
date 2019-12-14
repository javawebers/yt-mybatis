package com.github.yt.mybatis.query;


/**
 * @author sheng
 */
public class QueryJoin {
    private QueryJoinType joinType;
    private String tableNameAndOnConditions;

    public QueryJoinType takeJoinType() {
        return joinType;
    }

    public String takeTableNameAndOnConditions() {
        return tableNameAndOnConditions;
    }

    public QueryJoin(QueryJoinType joinType, String tableNameAndOnConditions) {
        this.joinType = joinType;
        this.tableNameAndOnConditions = tableNameAndOnConditions;
    }
}