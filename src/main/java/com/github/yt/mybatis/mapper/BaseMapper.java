package com.github.yt.mybatis.mapper;

import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用mapper接口
 * @author liujiasheng
 * @param <T> 实体类泛型类型
 */
public interface BaseMapper<T> {

    @InsertProvider(type = BaseMapperProvider.class, method = "saveBatch")
    int saveBatch(Collection<T> entityCollection);

    @UpdateProvider(type = BaseMapperProvider.class, method = "update")
    int update(@Param("entity") T entity, @Param("fieldColumnNames") String... fieldColumnNames);

    @UpdateProvider(type = BaseMapperProvider.class, method = "updateNotNull")
    int updateNotNull(@Param("entity") T entity, @Param("fieldColumnNames") String... fieldColumnNames);

    @UpdateProvider(type = BaseMapperProvider.class, method = "updateNew")
    int updateNew(Map<String, Object> paramMap);

    @UpdateProvider(type = BaseMapperProvider.class, method = "updateByCondition")
    int updateByCondition(Map<String, Object> paramMap);

    @UpdateProvider(type = BaseMapperProvider.class, method = "logicDelete")
    int logicDelete(Map<String, Object> param);

    @DeleteProvider(type = BaseMapperProvider.class, method = "delete")
    int delete(Map<String, Object> paramMap);

    @SelectProvider(type = BaseMapperProvider.class, method = "get")
    T get(Map<String, Object> paramMap);

    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
    T find(Map<String, Object> param);

    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
    List<T> findList(Map<String, Object> param);

    @SelectProvider(type = BaseMapperProvider.class, method = "count")
    int count(Map<String, Object> param);

}
