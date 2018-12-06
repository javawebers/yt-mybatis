package com.github.yt.mybatis.domain;

/**
 * domain默认值注入接口
 * @author liujiasheng
 */
public interface BaseEntityValue {

    /**
     * 获取操作人id
     *
     * @return 操作人id
     */
    String getFounderId();
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
    String getModifierId();
    /**
     * 获取修改人name
     *
     * @return 修改人name
     */
    String getModifierName();
}
