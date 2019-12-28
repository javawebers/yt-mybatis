package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.mybatis.dialect.impl.MysqlDialect;
import com.github.yt.mybatis.dialect.impl.OracleDialect;

/**
 * 方言处理类
 */
public class DialectHandler {

    private DialectHandler() {
    }

    private static final DialectHandler LOCK = new DialectHandler();
    private static Dialect dialect;

    /**
     * 获取方言
     *
     * @return 方言实例
     */
    public static Dialect getDialect() {
        if (dialect == null) {
            synchronized (LOCK) {
                try {
                    dialect = YtMybatisConfig.dialectEnum.getDialect().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("实例化方言类异常", e);
                }
            }
        }
        return dialect;
    }
}
