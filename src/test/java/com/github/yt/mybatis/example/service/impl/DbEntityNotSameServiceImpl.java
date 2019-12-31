package com.github.yt.mybatis.example.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.example.dao.DbEntityNotSameMapper;
import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.service.DbEntityNotSameService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbEntityNotSameService")
@Transactional
public class DbEntityNotSameServiceImpl extends BaseService<DbEntityNotSame> implements DbEntityNotSameService {

    @Resource
    private DbEntityNotSameMapper mapper;

}
