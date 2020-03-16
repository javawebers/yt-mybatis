package com.github.yt.mybatis.dialect.impl;

import com.github.yt.mybatis.dialect.BaseDialect;
import com.github.yt.mybatis.query.QueryLikeType;

/**
 * sql server 实现
 *
 * @author sheng
 */
public class SQLServerDialect extends BaseDialect {

    @Override
    public String limitOffset(String sql, Integer limitFrom, Integer limitSize) {
        if (limitFrom != null && limitSize != null) {
//            return sql + " Offset " + limitFrom + " Row Fetch Next " + limitSize + " Rows Only ";
            return sql;
        } else {
            return sql;
        }
    }

}
