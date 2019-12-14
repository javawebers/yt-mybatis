package com.github.yt.mybatis.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author  SpringContextUtils
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringContextUtils() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    // TODO
//    public static void refresh() {
//        ((AbstractRefreshableWebApplicationContext) applicationContext).refresh();
//    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> Map<String, T> getBeans(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static Boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    public static String getProperty(String key) {
        return PropertyConfigurer.getProperty(key);
    }
}
