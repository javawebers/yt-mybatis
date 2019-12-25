package com.github.yt.mybatis.util;

import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author sheng
 */
public class JPAUtils {

    private static Logger logger = LoggerFactory.getLogger(JPAUtils.class);

    public static List<Field> getAllFields(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        return getAllFields(entityClass.getSuperclass(), new ArrayList<>(Arrays.asList(fields)));
    }

    private static List<Field> getAllFields(Class<?> entityClass, List<Field> fields) {
        if (Object.class.equals(entityClass)) {
            Iterator<Field> it = fields.iterator();
            while (it.hasNext()) {
                Field next = it.next();
                if ("serialVersionUID".equals(next.getName())) {
                    it.remove();
                    continue;
                }
                if (null != next.getAnnotation(Transient.class)) {
                    it.remove();
                }
            }
            return fields;
        }
        Collections.addAll(fields, entityClass.getDeclaredFields());
        return getAllFields(entityClass.getSuperclass(), fields);
    }

    public static Field getIdField(Class<?> entityClass) {
        for (Class<?> c = entityClass; c != Object.class; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (null != field.getAnnotation(Id.class)) {
                    return field;
                }
            }
        }
        throw new BaseErrorException(YtMybatisExceptionEnum.CODE_91, entityClass, entityClass.getName());
    }

    public static Object gtIdValue(Object po) {
        if (null == po) {
            return null;
        }
        return getValue(po, getIdField(po.getClass()));
    }

    public static Object getValue(Object po, String fieldName) {
        if (null == po || YtStringUtils.isEmpty(fieldName)) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_92);
        }
        try {
            Field field = getField(po.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(po);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class clazz, String fieldName) {
        if (clazz == Object.class) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_93, fieldName);
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return getField(clazz.getSuperclass(), fieldName);
    }

    public static void setValue(Object source, String fieldName, Object value) {
        if (null == source || YtStringUtils.isEmpty(fieldName)) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_92);
        }
        try {
            Field field = getField(source.getClass(), fieldName);
            field.setAccessible(true);
            field.set(source, value);
        } catch (Exception e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_99, e);
        }

    }

    public static Object getValue(Object source, Field field) {
        try {
            field.setAccessible(true);
            return field.get(source);
        } catch (Exception e) {
            logger.error("JPAUtils getValue 异常！", e);
            return null;
        }
    }

    public static <T> Map<String, Object> getIdPropertyMap(String propertyName, Collection<T> entityCollection) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (entityCollection != null) {
            for (T entity : entityCollection) {
                try {
                    Field idField = getIdField(entity.getClass());
                    idField.setAccessible(true);
                    Object propertyObj = getValue(entity, entity.getClass().getDeclaredField(propertyName));
                    result.put((String) idField.get(entity), propertyObj);
                } catch (Exception e) {
                    logger.error("JPAUtils getValue 异常！", e);
                    return null;
                }
            }
        }
        return result;
    }

    public static <T> Map<String, T> getIdEntityMap(Collection<T> entityCollection) {
        Map<String, T> result = new LinkedHashMap<>();
        if (entityCollection != null) {
            for (T entity : entityCollection) {
                try {
                    Field idField = getIdField(entity.getClass());
                    result.put((String) getValue(entity, idField), entity);
                } catch (BaseErrorException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static <T> Map<Object, T> propertyEntityMap(String propertyName, Collection<T> entityCollection) {
        Map<Object, T> result = new LinkedHashMap<>();
        if (entityCollection != null) {
            for (T entity : entityCollection) {
                try {
                    Field propertyField = entity.getClass().getDeclaredField(propertyName);
                    result.put(getValue(entity, propertyField), entity);
                } catch (NoSuchFieldException e) {
                    logger.error("JPAUtils getValue 异常！", e);
                    return null;
                }
            }
        }
        return result;
    }

    public static String getAnnotationColumnName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        if (columnAnnotation == null || YtStringUtils.isEmpty(columnAnnotation.name())) {
            return field.getName();
        }
        return columnAnnotation.name();
    }

    public static String getSearchAnnotationColumnName(Field field) {
        return getSearchAnnotationColumnName(field, "");
    }

    public static String getSearchAnnotationColumnName(Field field, String aliasName) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        if (columnAnnotation == null || YtStringUtils.isEmpty(columnAnnotation.name())) {
            return aliasName + field.getName();
        }
        return aliasName + columnAnnotation.name() + " as " + field.getName();
    }

    public static String getSelectSql(Class<?> entityClass) {
        return getSelectSql(entityClass, "");
    }

    public static String getSelectSql(Class<?> entityClass, String aliasName) {
        StringBuilder selectSql = new StringBuilder();
        for (Field field : JPAUtils.getAllFields(entityClass)) {
            field.setAccessible(true);
            String fieldName = JPAUtils.getSearchAnnotationColumnName(field, aliasName);
            if (YtStringUtils.isNotEmpty(fieldName)) {
                selectSql.append(",").append(fieldName);
            }
        }
        selectSql = new StringBuilder(selectSql.toString().replaceFirst(",", ""));
        if (YtStringUtils.isEmpty(selectSql.toString())) {
            return aliasName + "*";
        }
        return selectSql.toString();
    }
}
