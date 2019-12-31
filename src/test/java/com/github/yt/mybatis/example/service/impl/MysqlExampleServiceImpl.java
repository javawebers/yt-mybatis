package com.github.yt.mybatis.example.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.example.dao.MysqlExampleMapper;
import com.github.yt.mybatis.example.entity.MysqlExample;
import com.github.yt.mybatis.example.service.MysqlExampleService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mysqlExampleService")
@Transactional
public class MysqlExampleServiceImpl extends BaseService<MysqlExample> implements MysqlExampleService {

    @Resource
    private MysqlExampleMapper mapper;

}
