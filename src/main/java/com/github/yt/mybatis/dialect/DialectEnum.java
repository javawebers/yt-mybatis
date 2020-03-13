package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.dialect.impl.MysqlDialect;
import com.github.yt.mybatis.dialect.impl.OracleDialect;
import com.github.yt.mybatis.dialect.impl.PostgresDialect;
import com.github.yt.mybatis.dialect.impl.SQLServerDialect;

/**
 * 方言的枚举类
 *
 * @author sheng
 */
public enum DialectEnum {
    /**
     * 方言枚举
     */
    MYSQL(MysqlDialect.class),
    ORACLE(OracleDialect.class),
    SQLSERVER(SQLServerDialect.class),
    POSTGRES(PostgresDialect.class),
    ;

    Class<? extends Dialect> dialect;

    DialectEnum(Class<? extends Dialect> dialect) {
        this.dialect = dialect;
    }

    public Class<? extends Dialect> getDialect() {
        return dialect;
    }
}
