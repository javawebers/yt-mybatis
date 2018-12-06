package com.github.yt.mybatis.service;

import com.github.yt.commons.exception.Assert;
import com.github.yt.commons.query.Page;
import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.domain.BaseEntity;
import com.github.yt.mybatis.mapper.BaseMapper;
import com.github.yt.mybatis.query.PageUtils;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.utils.BaseEntityUtils;
import com.github.yt.mybatis.utils.EntityUtils;
import com.github.yt.mybatis.utils.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 通用service实现
 * @author liujiasheng
 * @param <T>
 */
public abstract class BaseService<T> implements IBaseService<T> {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseService.class);

    private BaseMapper mapper;
    // 如果子类中没有getMapper方法会调用baseService中的getMapper方法，在这个方法中直接获取mapper属性
    @Override
    public  <M extends BaseMapper<T>>M getMapper(){
        if (mapper == null) {
            Field mapperField = EntityUtils.getField(this.getClass(), "mapper");
            mapper = (M)EntityUtils.getValue(this, mapperField);
        }
        // 没有覆盖getMapper方法，也没有mapper字段，抛出异常
        Assert.notNull(mapper, YtMybatisExceptionEnum.CODE_85);
        return (M)mapper;
    }

    @Override
	public int save(T entity) {
        List<T> entityList = new ArrayList<>();
        entityList.add(entity);
        return saveBatch(entityList, false);
    }

    @Override
    public int saveBatch(Collection<T> entityCollection) {
        return saveBatch(entityCollection, true);
	}

    /**
     * 所有的保存都走这里
     * @param entityCollection
     * @param batch
     * @return
     */
	private int saveBatch(Collection<T> entityCollection, boolean batch) {
        if (entityCollection == null || entityCollection.isEmpty()) {
            return 0;
        }
        String creatorId = BaseEntityUtils.getFounderId();
        String creatorName = BaseEntityUtils.getFounderName();
        Date createTime = new Date();
        // 设置creator信息
        for (T entity : entityCollection) {
            if (!(entity instanceof BaseEntity)) {
                break;
            }
            BaseEntity baseEntity = (BaseEntity)entity;
            if (StringUtils.isEmpty(baseEntity.getFounderId())) {
                baseEntity.setFounderId(creatorId);
            }
            if (StringUtils.isEmpty(baseEntity.getFounderName())) {
                baseEntity.setFounderName(creatorName);
            }
            if (baseEntity.getCreateDateTime() == null) {
                baseEntity.setCreateDateTime(createTime);
            }
        }

        Date now = new Date();
        Field idField = null;
        List<Field> fieldList = null;
        int i = 0;
//        String generateIdValue = now.getTime() + "_" + UUID.randomUUID().toString().replace("-", "");
        String generateIdValue = generateIdValue();
        // 设置id字段的值
        for (T entity : entityCollection) {
            fieldList = EntityUtils.getTableFieldList(entity.getClass());

            if (idField == null) {
                idField = EntityUtils.getIdField(entity.getClass());
            }
            // id
            Object idValue = EntityUtils.getValue(entity, idField);
            if (idField != null && idValue == null) {
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
        i = 0;
        Map<String, Object> param = new HashMap<>();
        for (T entity : entityCollection) {
            for (Field field : fieldList) {
                param.put(field.getName() + "__" + i + "__", EntityUtils.getValue(entity, field));
            }
            i++;
        }
        param.put("entityCollection", entityCollection);
        // 设置id，创建人，修改人等信息
        return getMapper().saveBatch(param);
    }

    @Override
    public int update(T entity) {
		return getMapper().update(entity);
	}

    @Override
    public int updateForSelective(T entity) {
		return getMapper().updateNotNull(entity);
	}

    @Override
    public int updateByCondition(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return 0;
        }
        return getMapper().updateByCondition(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public int delete(T entity) {
        // TODO
        return 0;
    }

    @Override
    public int delete(Class<T> entityClass, Serializable id) {
        // TODO
        return 0;
    }

    @Override
    public int logicDelete(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return 0;
        }
        return getMapper().logicDelete(ParamUtils.getParamMap(entityCondition, query));
    }

    @Override
    public int logicDelete(T entityCondition) {
        return logicDelete(entityCondition, new Query());
    }

    @Override
    public T get(Class<T> entityClass, Serializable id) {
	    if (id == null) {
	        return null;
        }
        return getMapper().get(entityClass, id);
    }
    @Override
    public T find(T entityCondition) {
        return find(entityCondition, new Query());
    }
    @Override
    public T find(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return null;
        }
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
    public List<T> findList(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return new ArrayList<>();
        }
        return getMapper().findList(ParamUtils.getParamMap(entityCondition, query));
    }
    public int count(T entityCondition) {
        return count(entityCondition, new Query());
    }
    public int count(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return 0;
        }
        return getMapper().count(ParamUtils.getParamMap(entityCondition, query));
    }
    @Override
    public Page<T> findPage(T entityCondition, Query query) {
        if (isInEmpty(query)) {
            return PageUtils.createPage(query.takePageNo(), query.takePageSize(), 0, new ArrayList<>());
        }
	    // 设置页数页码
        ParamUtils.setPageInfo(query);
        Map<String, Object> paramMap = ParamUtils.getParamMap(entityCondition, query);
        Page<T> page;
        int count = getMapper().count(paramMap);
        if (count == 0) {
            page = PageUtils.createPage(query.takePageNo(), query.takePageSize(), count, new ArrayList<>());
        } else {
            query.limit((query.takePageNo() - 1) * query.takePageSize(), query.takePageSize());
            List<T> entityList = getMapper().findPageList(paramMap);
            page = PageUtils.createPage(query.takePageNo(), query.takePageSize(), count, entityList);
        }
		return page;
	}


	private boolean isInEmpty(Query query){
	    // TODO
        return false;
//        if(query == null) {
//            return false;
//        }
//        for (Query.InCondition inCondition : query.inList) {
//            if (CollectionUtils.isEmpty(inCondition.values)) {
//                logger.info("query中存在in为空，不进行数据库操作！");
//                return true;
//            }
//        }
//        return false;
    }

    public static String generateIdValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
