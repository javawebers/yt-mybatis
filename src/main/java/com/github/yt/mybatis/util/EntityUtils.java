package com.github.yt.mybatis.util;

import com.github.yt.commons.exception.BaseErrorException;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.entity.YtBaseEntityColumn;
import com.github.yt.mybatis.entity.YtColumnType;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 实体类工具类
 *
 * @author sheng
 */
public class EntityUtils {

    /**
     * 获取对象的属性值
     *
     * @param source 对象
     * @param field  属性
     * @return 属性值
     */
    public static Object getValue(Object source, Field field) {
        try {
            field.setAccessible(true);
            return field.get(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取对象的属性值
     *
     * @param source    对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getValue(Object source, String fieldName) {
        try {
            Field field = getField(source.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置对象属性值
     *
     * @param source 对象
     * @param field  属性
     * @param value  属性值
     */
    public static void setValue(Object source, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(source, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否包含某个属性
     *
     * @param clazz     类型
     * @param fieldName 字段名
     * @return 是否包含
     */
    public static boolean hasField(Class clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        if (clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)) {
            return false;
        }
        return hasField(clazz.getSuperclass(), fieldName);
    }

    /**
     * 获取字段
     *
     * @param clazz     类型
     * @param fieldName 字段名
     * @return 字段
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        if (clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)) {
            throw new BaseErrorException(YtMybatisExceptionEnum.CODE_94);
        } else {
            return getField(clazz.getSuperclass(), fieldName);
        }
    }

    /**
     * 获取对象的所有 table field
     *
     * @param entityClass 对象类型
     * @return 所有的 table 字段
     */
    public static List<Field> getTableFieldList(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
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

    /**
     * 获取 id 字段属性
     *
     * @param entityClass 对象类型
     * @return id 属性
     */
    public static Field getIdField(Class<?> entityClass) {
        for (Class<?> c = entityClass; c != Object.class; c = c.getSuperclass()) {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (null != field.getAnnotation(Id.class)) {
                    return field;
                }
            }
        }
        throw new RuntimeException("id 字段不存在，" + entityClass);
    }

    /**
     * 获取 id 字段的值
     *
     * @param entity 实体类
     * @return id 字段的值
     */
    public static Object getIdValue(Object entity) {
        Field idField = getIdField(entity.getClass());
        return getValue(entity, idField);
    }

    /**
     * 获取实体类表名
     *
     * @param entityClass 对象类型
     * @return 实体类表名
     */
    public static String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation == null || YtStringUtils.isEmpty(tableAnnotation.name())) {
            return entityClass.getSimpleName();
        }
        return tableAnnotation.name();
    }

    /**
     * 获取 field 上 @javax.persistence.Column 注解的 name ，如果没有返回 field 的 name
     *
     * @param field field
     * @return name
     */
    public static String getFieldColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null || YtStringUtils.isEmpty(column.name())) {
            return field.getName();
        }
        return column.name();
    }

    /**
     * 获取 key：id ， value：entity 的 map
     *
     * @param entityCollection 实体类集合
     * @param <T>              实体类泛型
     * @return map
     */
    public static <T> Map<String, T> getIdEntityMap(Collection<T> entityCollection) {
        Map<String, T> result;
        if (entityCollection != null && !entityCollection.isEmpty()) {
            result = new HashMap<>(entityCollection.size());
            for (T entity : entityCollection) {
                Field idField = getIdField(entity.getClass());
                result.put((String) getValue(entity, idField), entity);
            }
        } else{
            result = new HashMap<>(0);
        }
        return result;
    }

    /**
     * 获取 key：属性值 ， value：entity 的 map
     *
     * @param propertyName     属性名
     * @param entityCollection 实体类集合
     * @param <T>              实体类泛型
     * @return map
     */
    public static <T> Map<Object, T> propertyEntityMap(String propertyName, Collection<T> entityCollection) {
        Map<Object, T> result;
        if (entityCollection != null && !entityCollection.isEmpty()) {
            result = new HashMap<>(entityCollection.size());
            for (T entity : entityCollection) {
                Field propertyField = getField(entity.getClass(), propertyName);
                result.put(getValue(entity, propertyField), entity);
            }
        } else {
            result = new HashMap<>(0);
        }
        return result;
    }

    /**
     * 获取map，key：属性值，value：集合。集合的 property 值一样
     *
     * @param propertyName     属性
     * @param entityCollection 包含所有的集合
     * @param <T>              泛型
     * @return map
     */
    public static <T> Map<Object, Collection<T>> propertyCollectionMap(String propertyName, Collection<T> entityCollection) {
        Map<Object, Collection<T>> result;
        if (entityCollection != null && !entityCollection.isEmpty()) {
            result = new HashMap<>(entityCollection.size());
            for (T entity : entityCollection) {
                Field propertyField;
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
        } else {
            result = new HashMap<>(0);
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
                Collection<R> collection = (Collection<R>) collectionField.get(main);
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
     * 将 subCollection 中的对象设置到 mainCollection 中
     *
     * @param mainCollection  main 对象集合
     * @param subCollection   sub 对象集合
     * @param entityFieldName main对象中sub对象的属性
     * @param mainFieldName   main对象对应的字段名
     * @param subFieldName    sub对象对应的字段名
     * @param <T>             main对象泛型
     * @param <R>             sub对象泛型
     */
    public static <T, R> void setSubEntityToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                                   String entityFieldName, String mainFieldName, String subFieldName) {
        // 未测试，不爱写了，有空再写
//        if (mainCollection == null || mainCollection.isEmpty() || subCollection == null || subCollection.isEmpty()) {
//            return;
//        }
//        Map<Object, R> subPropertyMap = propertyEntityMap(subFieldName, subCollection);
//        mainCollection.forEach(main -> {
//            Object relationValue = getValue(main, mainFieldName);
//            if (subPropertyMap.containsKey(relationValue)) {
//                setValue(main, getField(main.getClass(), entityFieldName), subPropertyMap.get(relationValue));
//            }
//        });
    }

    /**
     * 将 subCollection 中的对象设置到 mainCollection 中
     *
     * @param mainCollection main 对象集合
     * @param subCollection  sub 对象集合
     * @param valueFieldName main对象中 要设置值的属性名
     * @param mainFieldName  main对象对应的字段名
     * @param subFieldName   sub对象对应的字段名
     * @param <T>            main对象泛型
     * @param <R>            sub对象泛型
     */
    public static <T, R> void setValueToEntity(Collection<T> mainCollection, Collection<R> subCollection,
                                               String valueFieldName, String mainFieldName, String subFieldName) {
        // TODO
        if (mainCollection == null || mainCollection.isEmpty() || subCollection == null || subCollection.isEmpty()) {
            return;
        }
    }

    /**
     * 将fromObject转为目标对象
     *
     * @param fromObject 源对象
     * @param toClass    目标对象类型类
     * @param converter  转换器
     * @param <S>        源对象类型
     * @param <T>        目标对象类型
     * @return 目标对象
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
        return convertObject(fromObject, toClass, new Converter() {
        });
    }

    /**
     * 将集合转换成一个目标对象集合，默认是值复制
     * 可自己扩展
     *
     * @param fromCollection 源对象集合
     * @param toClass        目标对象类
     * @param converter      转换器
     * @param <S>            source
     * @param <T>            target
     * @return 目标对象集合
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
        return convertList(fromCollection, toClass, new Converter() {
        });
    }

    /**
     * 转换器回调
     *
     * @author liujiasheng
     */
    public interface Converter<S, T> {
        /**
         * 转换
         *
         * @param source 源对象
         * @param target 目标对象
         */
        default void convert(S source, T target) {
            BeanUtils.copyProperties(source, target);
        }
    }

    /**
     * @param collection 集合
     * @param <T>        实体类泛型
     * @return 泛型类型
     */
    public static <T> Class<T> getEntityClass(Collection<T> collection) {
        for (T t : collection) {
            return (Class<T>) t.getClass();
        }
        throw new RuntimeException("获取集合泛型数据类型失败");
    }

    public static <T> Field getYtColumnField(Class<T> entityClass, YtColumnType type) {
        List<Field> tableFieldList = getTableFieldList(entityClass);
        // 判断是否包含 YtBaseEntityColumn 注解， 并且值与 type 相同
        for (Field tableField : tableFieldList) {
            // 排除Transient
            YtBaseEntityColumn ytBaseEntityColumn = tableField.getAnnotation(YtBaseEntityColumn.class);
            if (null == ytBaseEntityColumn) {
                continue;
            }
            if (type.equals(ytBaseEntityColumn.value())) {
                return tableField;
            }
        }
        return null;
    }

}
