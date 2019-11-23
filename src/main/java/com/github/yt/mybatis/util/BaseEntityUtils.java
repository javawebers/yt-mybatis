package com.github.yt.mybatis.util;

import com.github.yt.mybatis.entity.BaseEntityValue;
import com.github.yt.mybatis.entity.DefaultBaseEntityValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * BaseEntity 工具类
 * 静态获取登录人信息
 *
 * @author liujiasheng
 */
@Configuration
public class BaseEntityUtils {

    private static BaseEntityValue baseEntityValue;

    private static BaseEntityValue getBaseEntityValue() {
        if (baseEntityValue == null) {
            synchronized (BaseEntityUtils.class) {
                try {
                    // 业务中实现了，使用业务中的类，只能有一个实现
                    baseEntityValue = SpringContextUtils.getBean(BaseEntityValue.class);
                } catch (NoUniqueBeanDefinitionException e) {
                    throw e;
                } catch (NoSuchBeanDefinitionException e) {
                    // 业务中没有实现，使用默认的类，默认类中的方法都返回空
                    baseEntityValue = DefaultBaseEntityValue.getInstance();
                }
            }
        }
        return baseEntityValue;
    }

    public static Object getFounderId() {
        return getBaseEntityValue().getFounderId();
    }

    public static String getFounderName() {
        return getBaseEntityValue().getFounderName();
    }

    public static Object getModifierId() {
        return getBaseEntityValue().getModifierId();
    }

    public static String getModifierName() {
        return getBaseEntityValue().getModifierName();
    }

}
