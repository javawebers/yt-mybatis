package com.github.yt.mybatis.util;

import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class EntityUtils {

    public static Object getValue(Object source, Field field) {
        try {
            field.setAccessible(true);
            return field.get(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getValue(Object po, String fieldName) {
        try {
            Field field = getField(po.getClass(), fieldName);
            field.setAccessible(true);  //设置私有属性范围
            return field.get(po);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasField(Class clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        if (clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)){
            return false;
        }
        return hasField(clazz.getSuperclass(), fieldName);
    }

    public static Field getField(Class clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return getField(clazz.getSuperclass(), fieldName);

    }

    public static List<Field> getTableFieldList(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();//获得属性
        return getTableFieldList(entityClass.getSuperclass(), new ArrayList<>(Arrays.asList(fields)));
    }

    private static List<Field> getTableFieldList(Class<?> entityClass, List<Field> fieldList) {
        if (Object.class.equals(entityClass)) {
            List<Field> newFieldList = new ArrayList<>();

            for (Field field : fieldList) {
                // 排除Transient
                if (null != field.getAnnotation(Transient.class)) {
                    continue;
                }
                // 静态和final的排除
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                newFieldList.add(field);
            }
            return newFieldList;
        }
        Collections.addAll(fieldList, entityClass.getDeclaredFields());
        return getTableFieldList(entityClass.getSuperclass(), fieldList);
    }

    public static Field getIdField(Class<?> entityClass) {
        for (Class<?> c = entityClass; c != Object.class; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();//获得属性
            for (Field field : fields) {
                if (null != field.getAnnotation(Id.class)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static Object getIdValue(Object entity) {
        Field idField = getIdField(entity.getClass());
        return getValue(entity, idField);
    }


    public static String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation == null || YtStringUtils.isEmpty(tableAnnotation.name())) {
            return entityClass.getSimpleName();
        }
        return tableAnnotation.name();
    }


    public static <T> Map<String, T> getIdEntityMap(Collection<T> entityCollection) {
        Map<String, T> result = new HashMap<>();
        if (entityCollection != null && !entityCollection.isEmpty()) {
            for (T entity : entityCollection) {
                Field idField = getIdField(entity.getClass());
                result.put((String) getValue(entity, idField), entity);
            }
        }
        return result;
    }

    public static <T> Map<Object, T> propertyEntityMap(String propertyName, Collection<T> entityCollection) {
        Map<Object, T> result = new HashMap<>();
        if (entityCollection != null && !entityCollection.isEmpty()) {
            for (T entity : entityCollection) {
                Field propertyField = null;
                try {
                    propertyField = entity.getClass().getDeclaredField(propertyName);
                } catch (NoSuchFieldException e) {
                    throw new BaseErrorException(YtMybatisExceptionEnum.CODE_94, e);
                }
                result.put(getValue(entity, propertyField), entity);
            }
        }
        return result;
    }

    public static <T> Map<Object, Collection<T>> propertyCollectionMap(String propertyName, Collection<T> entityCollection) {
        Map<Object, Collection<T>> result = new HashMap<>();
        if (entityCollection != null && !entityCollection.isEmpty()) {
            for (T entity : entityCollection) {
                Field propertyField = null;
                try {
                    propertyField = entity.getClass().getDeclaredField(propertyName);
                } catch (NoSuchFieldException e) {
                    throw new BaseErrorException(YtMybatisExceptionEnum.CODE_94, e);
                }
                Object propertyValue = getValue(entity, propertyField);
                if (!result.containsKey(propertyValue)) {
                    result.put(propertyValue, new ArrayList<>());
                }
                result.get(propertyValue).add(entity);
            }
        }
        return result;
    }


    private static <T, R> void setListToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                               Field collectionField, Field mainField, Field relationField) throws IllegalAccessException {
        collectionField.setAccessible(true);
        mainField.setAccessible(true);
        relationField.setAccessible(true);
        for (T main : mainCollection) {
            if (collectionField.get(main) == null) {
                if (List.class.isAssignableFrom(collectionField.getType())) {
                    collectionField.set(main, new ArrayList<>());
                } else if (Set.class.isAssignableFrom(collectionField.getType())) {
                    collectionField.set(main, new HashSet<>());
                } else {
                    throw new BaseErrorException(YtMybatisExceptionEnum.CODE_95, collectionField.getName());
                }
            }
        }
        Map<Object, T> mainMap = propertyEntityMap(mainField.getName(), mainCollection);
        Map<Object, Collection<R>> relationCollectionMap = propertyCollectionMap(relationField.getName(), subCollection);
        for (Map.Entry<Object, Collection<R>> entry : relationCollectionMap.entrySet()) {
            T main = mainMap.get(entry.getKey());
            if (main != null) {
                Collection collection = (Collection) collectionField.get(main);
                collection.addAll(entry.getValue());
            }
        }
    }

    public static <T, R> void setListToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                              String collectionProperty, String mainProperty, String relationProperty) {
        if (mainCollection == null || mainCollection.isEmpty() || subCollection == null || subCollection.isEmpty()) {
            return;
        }
        try {
            Field collectionField = null;
            Field mainField = null;
            Field relationField = null;

            for (T main : mainCollection) {
                collectionField = getField(main.getClass(), collectionProperty);
                if (YtStringUtils.isBlank(mainProperty)) {
                    mainField = getIdField(main.getClass());
                    mainProperty = mainField.getName();
                } else {
                    mainField = getField(main.getClass(), mainProperty);
                }
                break;
            }
            if (YtStringUtils.isBlank(relationProperty)) {
                relationProperty = mainProperty;
            }
            for (R sub : subCollection) {
                relationField = getField(sub.getClass(), relationProperty);
                break;
            }
            setListToEntity(mainCollection, subCollection, collectionField, mainField, relationField);
        } catch (Exception e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_96, e);
        }

    }


    public static <T, R> void setListToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                              String collectionProperty, String relationProperty) {
        setListToEntity(mainCollection, subCollection, collectionProperty, null, relationProperty);
    }

    public static <T, R> void setListToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                              String collectionProperty) {
        setListToEntity(mainCollection, subCollection, collectionProperty, null, null);
    }

    /**
     * 将fromObject转为目标对象
     * @param fromObject
     * @param toClass
     * @param converter
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T convertObject(S fromObject, Class<T> toClass, Converter converter) {
        try {
            T resultObject = toClass.newInstance();
            converter.convert(fromObject, resultObject);
            return resultObject;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_99, e);
        }
    }
    public static <S, T> T convertObject(S fromObject, Class<T> toClass) {
        return convertObject(fromObject, toClass, new Converter() {});
    }
    /**
     * 将集合转换成一个目标对象集合，默认是值复制
     * 可自己扩展
     * @param fromCollection
     * @param toClass
     * @param converter
     * @param <S> source
     * @param <T> target
     * @return
     */
    public static <S, T> List<T> convertList(List<S> fromCollection, Class<T> toClass, Converter converter) {
        List<T> result = new ArrayList<>();
        if (fromCollection == null || fromCollection.isEmpty()) {
            return result;
        }
        for (S s : fromCollection) {
            try {
                T resultObject = toClass.newInstance();
                converter.convert(s, resultObject);
                result.add(resultObject);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new BaseErrorException(YtMybatisExceptionEnum.CODE_99, e);
            }
        }
        return result;
    }

    public static <S, T> List<T> convertList(List<S> fromCollection, Class<T> toClass) {
        return convertList(fromCollection, toClass, new Converter() {});
    }
    /**
     * 转换器回调
     * @author liujiasheng
     */
    public interface Converter<S, T> {
        /**
         * 转换
         * @param source
         * @param target
         */
        default void convert(S source, T target) {
            BeanUtils.copyProperties(source, target);
        }
    }
}
