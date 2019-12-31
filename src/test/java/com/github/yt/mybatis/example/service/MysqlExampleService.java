package com.github.yt.mybatis.example.service;

import com.github.yt.mybatis.service.IBaseService;
import com.github.yt.mybatis.example.dao.MysqlExampleMapper;
import com.github.yt.mybatis.example.entity.MysqlExample;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 示例服务层
 *
 */
public interface MysqlExampleService extends IBaseService<MysqlExample> {


}
