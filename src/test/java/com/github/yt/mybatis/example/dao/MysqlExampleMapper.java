package com.github.yt.mybatis.example.dao;

import com.github.yt.mybatis.example.entity.MysqlExample;
import com.github.yt.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * MysqlExample Mapper
 *
 */
@Mapper
@Repository
public interface MysqlExampleMapper extends BaseMapper<MysqlExample> {


}
