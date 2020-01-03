package com.github.yt.mybatis.example.entity;

import com.github.yt.mybatis.entity.BaseEntityValue;

/**
 * entity 操作人信息默认实现
 * @author liujiasheng
 */
public class BusinessBaseEntityValue implements BaseEntityValue {
    // TODO 业务实现，可从登录信息中获取真实的用户信息。下面示例写死
    @Override
    public String getFounderId() {
        return "founderId222";
    }

    @Override
    public String getFounderName() {
        return "founderName222";
    }

    @Override
    public String getModifierId() {
        return "ModifierId222";
    }

    @Override
    public String getModifierName() {
        return null;
    }
}
