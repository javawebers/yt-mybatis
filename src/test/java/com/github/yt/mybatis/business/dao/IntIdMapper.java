package com.github.yt.mybatis.business.dao;

import com.github.yt.mybatis.business.entity.IntId;
import com.github.yt.mybatis.mapper.BaseMapper;
import com.github.yt.mybatis.mapper.BaseMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.Map;

/**
 * IntId Mapper
 *
 */
@Mapper
public interface IntIdMapper extends BaseMapper<IntId> {

    @InsertProvider(type = IntIdProvider.class, method = "saveBatch")
    @Options(useGeneratedKeys = true, keyProperty = "entityCollection.intId")
    int saveBatch(Map<String, Object> param);

    @InsertProvider(type = IntIdProvider.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "intId")
    int save(IntId entity);

}
