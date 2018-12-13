package com.github.yt.mybatis.dialect;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.util.SpringContextUtils;

/**
 * @author limiao
 */
public class DialectFactory {

    private static BaseDialect baseDialect;

    public static BaseDialect create() {
        if (baseDialect != null) {
            return baseDialect;
        }
        BaseDialect object;
        try {
            object = (BaseDialect) SpringContextUtils.getBean(Class.forName(YtMybatisConfig.dialect));
        } catch (ClassNotFoundException e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_99, e);
        }
        return object;
    }
}
