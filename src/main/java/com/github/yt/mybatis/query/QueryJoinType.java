package com.github.yt.mybatis.query;

/**
 * @author sheng
 */
public enum QueryJoinType {
    /**
     * 关联查询
     */
    JOIN(" JOIN "),

    /**
     * 左关联
     */
    LEFT_JOIN(" LEFT JOIN "),

    /**
     * 右关联
     */
    RIGHT_JOIN(" RIGHT JOIN "),
    ;
    String value;

    QueryJoinType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
