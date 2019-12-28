package com.github.yt.mybatis.service;

import com.github.yt.commons.exception.Assert;
import com.github.yt.commons.exception.BaseAccidentException;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.entity.YtColumnType;
import com.github.yt.mybatis.mapper.BaseMapper;
import com.github.yt.mybatis.query.*;
import com.github.yt.mybatis.util.BaseEntityUtils;
import com.github.yt.mybatis.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

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
    private static Logger logger = LoggerFactory.getLogger(BaseService.class);
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
        return getMapper().update(entity, fieldColumnNames);
    }

    @Override
    public int updateForSelective(T entity, String... fieldColumnNames) {
        org.springframework.util.Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL);
        org.springframework.util.Assert.notNull(EntityUtils.getIdValue(entity), ID_MUST_NOT_BE_NULL);
        return getMapper().updateNotNull(entity, fieldColumnNames);
    }

    @Override
    public int updateByCondition(T entityCondition, MybatisQuery<?> query) {
        setUpdateBaseColumn(entityCondition, query);
        return getMapper().updateByCondition(ParamUtils.getParamMap(entityCondition, query));
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
        setUpdateBaseColumn(entityCondition, query);

        return getMapper().logicDelete(ParamUtils.getParamMap(entityCondition, query));
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
        Query query = new Query();
        query.addWhere(DialectHandler.getDialect().getColumnName(idField) + " = #{id}");
        query.addParam("id", id);
        return getMapper().get(ParamUtils.getParamMap(entityCondition, query));
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
            founderId = BaseEntityUtils.getFounderId();
        }
        if (founderNameField != null) {
            founderName = BaseEntityUtils.getFounderName();
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

    private void setUpdateBaseColumn(Object entityCondition, MybatisQuery<?> query) {
        if (query.takeUpdateBaseColumn()) {
            Field modifierIdField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFY_TIME);
            if (modifierIdField != null) {
                String modifierIdColumn = EntityUtils.getFieldColumnName(modifierIdField);
                query.addParam("_modifierId_", BaseEntityUtils.getModifierId());
                query.addUpdate("t." + modifierIdColumn + " = #{_modifierId_}");
            }
            if (modifierNameField != null) {
                String modifierNameColumn = EntityUtils.getFieldColumnName(modifierNameField);
                query.addParam("_modifierName_", BaseEntityUtils.getModifierName());
                query.addUpdate("t." + modifierNameColumn + " = #{_modifierName_}");
            }
            if (modifyTimeField != null) {
                String modifyTimeColumn = EntityUtils.getFieldColumnName(modifyTimeField);
                query.addParam("_modifyTime_", new Date());
                query.addUpdate("t." + modifyTimeColumn + " = #{_modifyTime_}");
            }
        }
    }

    private void setUpdateDeleteFlag(T entityCondition, MybatisQuery<?> query) {
        Field deleteFlagField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.DELETE_FLAG);
        if (deleteFlagField == null) {
            throw new BaseAccidentException(YtMybatisExceptionEnum.CODE_78);
        }
        String deleteFlagColumn = EntityUtils.getFieldColumnName(deleteFlagField);
        query.addUpdate("t." + deleteFlagColumn + " = true");
        query.addWhere("t." + deleteFlagColumn + " = false");
    }
}
