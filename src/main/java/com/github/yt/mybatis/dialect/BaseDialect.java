package com.github.yt.mybatis.dialect;

import com.github.yt.commons.query.Query;

/**
 * @author limiao
 */
public abstract class BaseDialect {

    public abstract String limitSql(Query query);

}
