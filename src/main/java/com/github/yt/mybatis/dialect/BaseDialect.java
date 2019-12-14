package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.query.MybatisQuery;

/**
 * @author limiao
 */
public abstract class BaseDialect {

    /**
     * 分页拼接
     * @param query
     * @return
     */
    public abstract String limitSql(MybatisQuery query);

}
