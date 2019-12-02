package com.github.yt.mybatis.dialect.mysql;

import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.mybatis.query.MybatisQuery;
import org.springframework.stereotype.Component;

/**
 * @author limiao
 */
@Component
public class MysqlDialect extends BaseDialect {

    @Override
    public String limitSql(MybatisQuery query) {
        if (query.takeLimitFrom() != null) {
            return " limit " + query.takeLimitFrom() + ", " + query.takeLimitSize() + " ";
        } else {
            return "";
        }
    }


}
