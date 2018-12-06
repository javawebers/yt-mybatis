package com.github.yt.mybatis;

import com.github.yt.mybatis.dialect.mysql.MysqlDialect;
import com.github.yt.mybatis.utils.BaseEntityUtils;
import com.github.yt.mybatis.utils.SpringContextUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SpringContextUtils.class,
        YtMybatisConfig.class,
        BaseEntityUtils.class,
        MysqlDialect.class,

})
public @interface EnableYtMybatis {
}
