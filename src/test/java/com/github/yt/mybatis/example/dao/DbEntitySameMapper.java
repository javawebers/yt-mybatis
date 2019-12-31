package com.github.yt.mybatis.example.dao;

import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * DbEntitySame Mapper
 *
 */
@Mapper
@Repository
public interface DbEntitySameMapper extends BaseMapper<DbEntitySame> {


}
