package com.github.yt.mybatis;

import com.github.yt.mybatis.dialect.mysql.MysqlDialect;
import com.github.yt.mybatis.exception.DatabaseExceptionConverter;
import com.github.yt.mybatis.util.BaseEntityUtils;
import com.github.yt.mybatis.util.SpringContextUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启mybatis无xml crud功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SpringContextUtils.class,
        YtMybatisConfig.class,
        BaseEntityUtils.class,
        MysqlDialect.class,
        DatabaseExceptionConverter.class,
})
public @interface EnableYtMybatis {
}
