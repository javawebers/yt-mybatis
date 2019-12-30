package com.github.yt.mybatis.service;

import com.github.yt.commons.exception.Assert;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.entity.YtColumnType;
import com.github.yt.mybatis.mapper.BaseMapper;
import com.github.yt.mybatis.query.*;
import com.github.yt.mybatis.util.BaseEntityHandler;
import com.github.yt.mybatis.util.EntityUtils;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 通用service实现
 *
 * @param <T> 实体类泛型类型
 * @author liujiasheng
 */
public abstract class BaseService<T> implements IBaseService<T> {
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private static final String ENTITY_MUST_NOT_BE_NULL = "The given entity must not be null!";

    private BaseMapper<?> mapper;

    @Override
    public <M extends BaseMapper<T>> M getMapper() {
        // 如果子类中没有getMapper方法会调用baseService中的getMapper方法，在这个方法中直接获取mapper属性
        if (mapper == null) {
            Field mapperField = EntityUtils.getField(this.getClass(), "mapper");
            mapper = (M) EntityUtils.getValue(this, mapperField);
            // 没有覆盖getMapper方法，也没有mapper字段，抛出异常
            Assert.notNull(mapper, YtMybatisExceptionEnum.CODE_85);
        }
        return (M) mapper;
    }

    @Override
    public int save(T entity) {
        List<T> entityList = Collections.singletonList(entity);
        return saveBatch(entityList, false);
    }

    @Override
    public int saveBatch(Collection<T> entityCollection) {
        return saveBatch(entityCollection, true);
    }

    /**
     * 所有的保存都走这里
     */
    private int saveBatch(Collection<T> entityCollection, boolean batch) {
        if (entityCollection == null || entityCollection.size() == 0) {
            return 0;
        }
        setEntityId(entityCollection, batch);
        setCreatorInfo(entityCollection);
        setDeleteFlag(entityCollection);
        return getMapper().saveBatch(entityCollection);
    }


    @Override
    public int update(T entity, String... fieldColumnNames) {
        org.springframework.util.Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
        org.springframework.util.Assert.notNull(EntityUtils.getIdValue(entity), ID_MUST_NOT_BE_NULL);
        return update(entity, true, fieldColumnNames);
    }

    @Override
    public int updateForSelective(T entity, String... fieldColumnNames) {
        org.springframework.util.Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
        org.springframework.util.Assert.notNull(EntityUtils.getIdValue(entity), ID_MUST_NOT_BE_NULL);
        return update(entity, false, fieldColumnNames);
    }

    private int update(T entity, boolean isUpdateNullField, String... selectedFieldColumnNames) {
        T entityCondition;
        try {
            entityCondition = (T) entity.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 判断是否更新指定字段
        Set<String> selectedFieldColumnNameSet = getSelectedFieldColumnNameSet(selectedFieldColumnNames, entity);
        Query query = new Query();
        setModifier(entity);

        Field idField = EntityUtils.getIdField(entity.getClass());
        for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
            field.setAccessible(true);
            //处理主键
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                continue;
            }
            if (!isUpdateNullField) {
                if (EntityUtils.getValue(entity, field) == null) {
                    continue;
                }
            }
            if (selectedFieldColumnNameSet != null && selectedFieldColumnNameSet.size() > 0) {
                if (!selectedFieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    continue;
                }
            }

            query.addUpdate(DialectHandler.getDialect().getColumnNameWithTableAlas(field) + " = " + DialectHandler.getDialect().getFieldParam(field, field.getName()));
            query.addParam("" + field.getName(), EntityUtils.getValue(entity, field));
        }

        query.addWhere(DialectHandler.getDialect().getColumnNameWithTableAlas(idField) + " = #{_id_}");
        query.addParam("_id_", EntityUtils.getValue(entity, idField));
        return getMapper().update(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public int updateByCondition(T entityCondition, MybatisQuery<?> query) {
        setUpdateBaseColumn(entityCondition.getClass(), query);
        return getMapper().update(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public int logicDeleteOne(Class<T> entityClass, Serializable id) {
        int num = logicDelete(entityClass, id);
        if (num != 1) {
            throw new EmptyResultDataAccessException("逻辑删除的数据不为1条，删除了:" + num, 1);
        }
        return num;
    }

    @Override
    public int logicDelete(Class<T> entityClass, Serializable id) {
        org.springframework.util.Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Field idField = EntityUtils.getIdField(entityClass);
        T entityCondition;
        try {
            entityCondition = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Query query = new Query();
        query.addWhere(DialectHandler.getDialect().getColumnNameWithTableAlas(idField) + " = #{id}");
        query.addParam("id", id);
        int num = this.logicDelete(entityCondition, query);
        Assert.le(num, 1, YtMybatisExceptionEnum.CODE_77);
        return num;
    }

    @Override
    public int logicDelete(T entityCondition, MybatisQuery<?> query) {

        setUpdateDeleteFlag(entityCondition, query);
        setUpdateBaseColumn(entityCondition.getClass(), query);

        return getMapper().update(ParamUtils.getParamMap(entityCondition, query));
    }


    @Override
    public int logicDelete(T entityCondition) {
        return logicDelete(entityCondition, new Query());
    }

    @Override
    public int deleteOne(Class<T> entityClass, Serializable id) {
        int num = delete(entityClass, id);
        if (num != 1) {
            throw new EmptyResultDataAccessException("删除的数据不为1条，删除了:" + num, 1);
        }
        return num;
    }

    @Override
    public int delete(Class<T> entityClass, Serializable id) {
        org.springframework.util.Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Field idField = EntityUtils.getIdField(entityClass);
        T entityCondition;
        try {
            entityCondition = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Query query = new Query();
        query.addWhere(DialectHandler.getDialect().getColumnName(idField) + " = #{id}");
        query.addParam("id", id);
        int num = this.delete(entityCondition, query);
        Assert.le(num, 1, YtMybatisExceptionEnum.CODE_76);
        return num;
    }

    @Override
    public int delete(T entityCondition) {
        return delete(entityCondition, new Query());
    }

    @Override
    public int delete(T entityCondition, MybatisQuery<?> query) {
        return getMapper().delete(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public T get(Class<T> entityClass, Serializable id) {
        org.springframework.util.Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Field idField = EntityUtils.getIdField(entityClass);
        T entityCondition;
        try {
            entityCondition = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        EntityUtils.setValue(entityCondition, idField, id);
        return find(entityCondition);
    }

    @Override
    public T getOne(Class<T> clazz, @NotNull Serializable id) {
        T entity = get(clazz, id);
        if (entity == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
    }

    @Override
    public T find(T entityCondition) {
        return find(entityCondition, new Query());
    }

    @Override
    public T findOne(T entityCondition) {
        T entity = find(entityCondition);
        if (entity == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
    }

    @Override
    public T findOne(T entityCondition, MybatisQuery<?> query) {
        T entity = find(entityCondition, query);
        if (entity == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return entity;
    }

    @Override
    public T find(T entityCondition, MybatisQuery<?> query) {
        if (query.takeLimitFrom() == null) {
            query.limit(0, 2);
        }
        return getMapper().find(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public List<T> findList(T entityCondition) {
        return findList(entityCondition, new Query());
    }

    @Override
    public List<T> findList(T entityCondition, MybatisQuery<?> query) {
        return getMapper().findList(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public int count(T entityCondition) {
        return count(entityCondition, new Query());
    }

    @Override
    public int count(T entityCondition, MybatisQuery<?> query) {
        return getMapper().count(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public Page<T> findPage(T entityCondition, MybatisQuery<?> query) {
        // 设置页数页码
        ParamUtils.setPageInfo(query);
        Map<String, Object> paramMap = ParamUtils.getParamMap(entityCondition, query);
        Page<T> page;
        int count = getMapper().count(paramMap);
        if (count == 0) {
            page = PageUtils.createPage(query.takePageNo(), query.takePageSize(), count, new ArrayList<>());
        } else {
            query.limit((query.takePageNo() - 1) * query.takePageSize(), query.takePageSize());
            List<T> entityList = getMapper().findList(paramMap);
            page = PageUtils.createPage(query.takePageNo(), query.takePageSize(), count, entityList);
        }
        return page;
    }

    /**
     * 生成一个 uuid 作为主键，去除其中的"-"
     */
    private static String generateUuidIdValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * id 为 String 类型，自动生成 uuid id
     * id 为空直接返回
     *
     * @param entityCollection 实体集合
     * @param batch            是否批量保存
     */
    private void setEntityId(Collection<T> entityCollection, boolean batch) {
        // 设置id字段的值
        Class<T> entityClass = EntityUtils.getEntityClass(entityCollection);
        Field idField;
        try {
            idField = EntityUtils.getIdField(entityClass);
        } catch (Exception e) {
            return;
        }
        if (idField.getType() != String.class) {
            return;
        }

        int i = 0;
        String generateIdValue = generateUuidIdValue();
        for (T entity : entityCollection) {
            String idValue = (String) EntityUtils.getValue(entity, idField);
            if (idValue == null) {
                if (batch) {
                    idValue = generateIdValue + "_" + i;
                } else {
                    idValue = generateIdValue;
                }
                try {
                    idField.set(entity, idValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }
    }


    /**
     * 设置创建人信息
     *
     * @param entityCollection 实体类集合
     */
    private void setCreatorInfo(Collection<T> entityCollection) {
        Class<T> entityClass = EntityUtils.getEntityClass(entityCollection);
        Field founderIdField = EntityUtils.getYtColumnField(entityClass, YtColumnType.FOUNDER_ID);
        Field founderNameField = EntityUtils.getYtColumnField(entityClass, YtColumnType.FOUNDER_NAME);
        Field createTimeField = EntityUtils.getYtColumnField(entityClass, YtColumnType.CREATE_TIME);

        Object founderId = null;
        String founderName = null;
        Date createTime = null;
        if (founderIdField != null) {
            founderId = BaseEntityHandler.getBaseEntityValue().getFounderId();
        }
        if (founderNameField != null) {
            founderName = BaseEntityHandler.getBaseEntityValue().getFounderName();
        }
        if (createTimeField != null) {
            createTime = new Date();
        }

        for (T entity : entityCollection) {
            if (founderIdField != null && founderId != null) {
                Object trueFounderId = EntityUtils.getValue(entity, founderIdField);
                if (trueFounderId == null) {
                    EntityUtils.setValue(entity, founderIdField, founderId);
                }
            }
            if (founderNameField != null && founderName != null) {
                Object trueFounderName = EntityUtils.getValue(entity, founderNameField);
                if (trueFounderName == null) {
                    EntityUtils.setValue(entity, founderNameField, founderName);
                }
            }
            if (createTimeField != null) {
                Object trueCreateTime = EntityUtils.getValue(entity, createTimeField);
                if (trueCreateTime == null) {
                    EntityUtils.setValue(entity, createTimeField, createTime);
                }
            }
        }
    }

    /**
     * 设置 deleteFlag 默认 false
     *
     * @param entityCollection 实体类集合
     */
    private void setDeleteFlag(Collection<T> entityCollection) {
        Class<T> entityClass = EntityUtils.getEntityClass(entityCollection);
        Field deleteFlagField = EntityUtils.getYtColumnField(entityClass, YtColumnType.DELETE_FLAG);
        for (T entity : entityCollection) {
            if (deleteFlagField != null) {
                Object deleteFlag = EntityUtils.getValue(entity, deleteFlagField);
                if (deleteFlag == null) {
                    EntityUtils.setValue(entity, deleteFlagField, false);
                }
            }
        }
    }

    /**
     * 设置修改人信息
     *
     * @param entity 实体类
     */
    private void setModifier(T entity) {
        Field modifierIdField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_ID);
        Field modifierNameField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_NAME);
        Field modifyTimeField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFY_TIME);
        if (modifierIdField != null) {
            Object modifierId = BaseEntityHandler.getBaseEntityValue().getModifierId();
            Object trueModifierId = EntityUtils.getValue(entity, modifierIdField);
            if (trueModifierId == null) {
                EntityUtils.setValue(entity, modifierIdField, modifierId);
            }
        }
        if (modifierNameField != null) {
            String modifierName = BaseEntityHandler.getBaseEntityValue().getModifierName();
            Object trueModifierName = EntityUtils.getValue(entity, modifierNameField);
            if (trueModifierName == null) {
                EntityUtils.setValue(entity, modifierNameField, modifierName);
            }
        }
        if (modifyTimeField != null) {
            Date modifyTime = new Date();
            Object trueModifyTime = EntityUtils.getValue(entity, modifyTimeField);
            if (trueModifyTime == null) {
                EntityUtils.setValue(entity, modifyTimeField, modifyTime);
            }
        }
    }

    private void setUpdateBaseColumn(Class<?> entityClass, MybatisQuery<?> query) {
        if (query.takeUpdateBaseColumn()) {
            Field modifierIdField = EntityUtils.getYtColumnField(entityClass, YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entityClass, YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entityClass, YtColumnType.MODIFY_TIME);
            if (modifierIdField != null) {
                query.addParam("_modifierId_", BaseEntityHandler.getBaseEntityValue().getModifierId());
                query.addUpdate(DialectHandler.getDialect().getColumnNameWithTableAlas(modifierIdField)
                        + " = "
                        + DialectHandler.getDialect().getFieldParam(modifierIdField, "_modifierId_"));
            }
            if (modifierNameField != null) {
                query.addParam("_modifierName_", BaseEntityHandler.getBaseEntityValue().getModifierName());
                query.addUpdate(DialectHandler.getDialect().getColumnNameWithTableAlas(modifierNameField)
                        + " = "
                        + DialectHandler.getDialect().getFieldParam(modifierIdField, "_modifierName_"));
            }
            if (modifyTimeField != null) {
                query.addParam("_modifyTime_", new Date());
                query.addUpdate(DialectHandler.getDialect().getColumnNameWithTableAlas(modifyTimeField)
                        + " = "
                        + DialectHandler.getDialect().getFieldParam(modifierIdField, "_modifyTime_"));
            }
        }
    }

    private void setUpdateDeleteFlag(T entityCondition, MybatisQuery<?> query) {
        Field deleteFlagField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.DELETE_FLAG);
        org.springframework.util.Assert.notNull(deleteFlagField, "逻辑删除字段不存在");

        String deleteFlagColumn = DialectHandler.getDialect().getColumnNameWithTableAlas(deleteFlagField);
        query.addUpdate(deleteFlagColumn + " = #{deleteFlagUpdate}");
        query.addWhere(deleteFlagColumn + " = #{deleteFlagWhere}");
        query.addParam("deleteFlagUpdate", true);
        query.addParam("deleteFlagWhere", false);
    }


    private Set<String> getSelectedFieldColumnNameSet(String[] selectedFieldColumnNames, T entity) {
        Set<String> selectedFieldColumnNameSet = null;
        if (selectedFieldColumnNames != null && selectedFieldColumnNames.length > 0) {
            selectedFieldColumnNameSet = new HashSet<>(Arrays.asList(selectedFieldColumnNames));
            Field modifierIdField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFY_TIME);
            if (modifierIdField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifierIdField));
            }
            if (modifierNameField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifierNameField));
            }
            if (modifyTimeField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifyTimeField));
            }
        }
        return selectedFieldColumnNameSet;
    }

}
