package com.github.yt.mybatis.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用mapper接口
 *
 * @param <T> 实体类泛型类型
 * @author liujiasheng
 */
public interface BaseMapper<T> {

    @InsertProvider(type = BaseMapperProvider.class, method = "saveBatch")
    int saveBatch(Collection<T> entityCollection);

    @DeleteProvider(type = BaseMapperProvider.class, method = "delete")
    int delete(Map<String, Object> paramMap);

    @UpdateProvider(type = BaseMapperProvider.class, method = "update")
    int update(Map<String, Object> paramMap);

    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
    T find(Map<String, Object> param);

    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
    List<T> findList(Map<String, Object> param);

    @SelectProvider(type = BaseMapperProvider.class, method = "count")
    int count(Map<String, Object> param);

}
