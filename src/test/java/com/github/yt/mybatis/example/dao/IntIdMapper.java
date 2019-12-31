package com.github.yt.mybatis.example.dao;

import com.github.yt.mybatis.example.entity.IntId;
import com.github.yt.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.Collection;

/**
 * IntId Mapper
 *
 */
@Mapper
public interface IntIdMapper extends BaseMapper<IntId> {

    @InsertProvider(type = IntIdProvider.class, method = "save")
    int save(IntId entity);

    @InsertProvider(type = IntIdProvider.class, method = "saveBatch")
    @Options(useGeneratedKeys = true, keyProperty = "intId")
    @Override
    int saveBatch(Collection<IntId> entityCollection);
}
