package com.github.yt.mybatis.business.dao;

import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.mapper.BaseMapper;
import com.github.yt.mybatis.mapper.BaseMapperProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * DbEntitySame Mapper
 *
 */
@Mapper
public interface DbEntitySameMapper extends BaseMapper<DbEntitySame> {
//    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
//    @ResultMap("cascadeResultMap")
//    DbEntitySame find(Map<String, Object> param);
//    @ResultMap("cascadeResultMap")
//    @SelectProvider(type = BaseMapperProvider.class, method = "findList")
//    List<DbEntitySame> findList(Map<String, Object> param);


}
