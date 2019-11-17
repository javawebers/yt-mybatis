package com.github.yt.mybatis.business.entity;


import com.github.yt.mybatis.entity.BaseEntityValue;
import org.springframework.stereotype.Component;

/**
 * entity 操作人信息默认实现
 * @author liujiasheng
 */
@Component("baseEntityValue")
public class BusinessBaseEntityValue implements BaseEntityValue {

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
