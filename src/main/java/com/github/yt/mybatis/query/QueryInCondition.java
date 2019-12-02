package com.github.yt.mybatis.query;

import java.util.Collection;

public class QueryInCondition {
    private String param;
    private Collection values;

    public String takeParam() {
        return param;
    }

    public Collection takeValues() {
        return values;
    }

    public QueryInCondition(String param, Collection values) {
        this.param = param;
        this.values = values;
    }

}