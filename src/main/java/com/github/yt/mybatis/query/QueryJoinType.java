package com.github.yt.mybatis.query;

/**
 * 查询的连接 join 类型
 *
 * @author sheng
 */
public enum QueryJoinType {
    /**
     * 关联查询
     */
    JOIN,

    /**
     * 左关联
     */
    LEFT_JOIN,

    /**
     * 右关联
     */
    RIGHT_JOIN,
    ;


}
