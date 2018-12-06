package com.github.yt.mybatis.utils;

import com.github.yt.mybatis.YtMybatisConfig;
import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.domain.BaseEntityValue;
import com.github.yt.mybatis.domain.DefaultBaseEntityValue;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * BaseEntity 工具类
 * 静态获取登录人信息
 * @author liujiasheng
 */
@Component
@Configurable
public class BaseEntityUtils {

    private static BaseEntityValue getBaseEntityValue(){
        try {
            return  (BaseEntityValue)SpringContextUtils.getBean(Class.forName(YtMybatisConfig.baseEntityValueClass));
        } catch (ClassNotFoundException e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_99, e);
        }
    }
    public static String getFounderId(){
        return getBaseEntityValue().getFounderId();
    }
    public static String getFounderName(){
        return getBaseEntityValue().getFounderName();
    }

    public static String getModifierId(){
        return getBaseEntityValue().getModifierId();
    }

    public static String getModifierName(){
        return getBaseEntityValue().getModifierName();
    }

    @Bean
    public DefaultBaseEntityValue defaultBaseEntityValue() {
        return new DefaultBaseEntityValue();
    }
}
