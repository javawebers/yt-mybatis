package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.YtMybatisConfig;

/**
 * 方言处理类
 *
 * @author sheng
 */
public class DialectHandler {

    private DialectHandler() {
    }

    private volatile static Dialect dialect;

    /**
     * 获取方言
     *
     * @return 方言实例
     */
    public static Dialect getDialect() {
        if (dialect == null) {
            synchronized (DialectHandler.class) {
                if (dialect == null) {
                    try {
                        dialect = YtMybatisConfig.dialectClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("实例化方言类异常", e);
                    }
                }
            }
        }
        return dialect;
    }
}
