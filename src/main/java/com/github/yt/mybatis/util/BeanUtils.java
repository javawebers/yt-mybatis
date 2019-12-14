package com.github.yt.mybatis.util;


import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;

import java.lang.reflect.Field;


/**
 * @author sheng
 */
public class BeanUtils {

    /**
     * 将所有对象的属性及值都放到map中
     *
     * @param objs 实体列表
     * @return 实体转map
     */
    public static ChainMap<String, Object> getValueMap(Object... objs) {
        try {
            ChainMap<String, Object> map = new ChainMap<>();
            for (Object obj : objs) {
                if (null == obj) {
                    continue;
                }
                for (Class<?> c = obj.getClass(); Object.class != c; c = c.getSuperclass()) {
                    for (Field field : c.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (null == value) {
                            continue;
                        }
                        if (field.getType().isAssignableFrom(String.class) && YtStringUtils.isEmpty((String) value)) {
                            continue;
                        }
                        map.put(field.getName(), value);
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_90, e);
        }

    }
}
