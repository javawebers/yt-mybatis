package com.github.yt.mybatis.entity;

import java.lang.annotation.*;

/**
 * 用户修饰 BaseEntity 的字段的
 * 包括：创建人、创建时间、修改人、修改时间、deleteFlag
 * @author sheng
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YtBaseEntityColumn {
    /**
     * 字段类型
     * @return 字段类型
     */
    YtColumnType value();
}
