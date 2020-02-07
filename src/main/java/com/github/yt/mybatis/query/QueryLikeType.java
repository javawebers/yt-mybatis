package com.github.yt.mybatis.query;

/**
 * 查询的 like 类型
 *
 * @author sheng
 */
public enum QueryLikeType {
    /**
     * 以 keyword 开头
     * keyword%
     */
    LEFT,

    /**
     * 以 keyword 结尾
     * %keyword
     */
    RIGHT,

    /**
     * keyword 在中间
     * %keyword%
     */
    MIDDLE
}
