package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.query.MybatisQuery;

/**
 * @author limiao
 */
public abstract class BaseDialect {

    public abstract String limitSql(MybatisQuery query);

}
