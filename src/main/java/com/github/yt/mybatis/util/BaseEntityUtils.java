package com.github.yt.mybatis.util;

import com.github.yt.mybatis.entity.BaseEntityValue;
import com.github.yt.mybatis.entity.DefaultBaseEntityValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * BaseEntity 工具类
 * 静态获取登录人信息
 * @author liujiasheng
 */
@Configuration
public class BaseEntityUtils {

    private static BaseEntityValue getBaseEntityValue(){
        return  (BaseEntityValue)SpringContextUtils.getBean("baseEntityValue");
    }
    public static Object getFounderId(){
        return getBaseEntityValue().getFounderId();
    }
    public static String getFounderName(){
        return getBaseEntityValue().getFounderName();
    }

    public static Object getModifierId(){
        return getBaseEntityValue().getModifierId();
    }

    public static String getModifierName(){
        return getBaseEntityValue().getModifierName();
    }

    @Bean
    @ConditionalOnMissingBean
    public BaseEntityValue baseEntityValue() {
        return new DefaultBaseEntityValue();
    }
}
