package com.github.yt.mybatis.entity;

/**
 * entity 默认值注入接口
 * @author liujiasheng
 */
public interface BaseEntityValue {

    /**
     * 获取操作人id
     *
     * @return 操作人id
     */
    Object getFounderId();
    /**
     * 获取操作人name
     *
     * @return 操作人name
     */
    String getFounderName();
    /**
     * 获取修改人id
     *
     * @return 修改人id
     */
    Object getModifierId();
    /**
     * 获取修改人name
     *
     * @return 修改人name
     */
    String getModifierName();
}
