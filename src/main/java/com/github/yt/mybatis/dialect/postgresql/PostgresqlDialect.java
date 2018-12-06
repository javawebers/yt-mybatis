package com.github.yt.mybatis.dialect.postgresql;

import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.commons.query.Query;
import org.springframework.stereotype.Component;

/**
 * @author limiao
 */
@Component
public class PostgresqlDialect extends BaseDialect {

    @Override
    public String limitSql(Query query) {
        return " limit " + query.takeLimitSize() + " offset " + query.takeLimitFrom() + " ";
    }

}
