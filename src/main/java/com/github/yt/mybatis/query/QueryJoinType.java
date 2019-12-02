package com.github.yt.mybatis.query;

public enum QueryJoinType {
    JOIN(" JOIN "),
    LEFT_JOIN(" LEFT JOIN "),
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
