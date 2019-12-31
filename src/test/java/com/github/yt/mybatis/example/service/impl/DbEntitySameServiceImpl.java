package com.github.yt.mybatis.example.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.example.dao.DbEntitySameMapper;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.example.service.DbEntitySameService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbEntitySameService")
@Transactional
public class DbEntitySameServiceImpl extends BaseService<DbEntitySame> implements DbEntitySameService {

    @Resource
    private DbEntitySameMapper mapper;

}
