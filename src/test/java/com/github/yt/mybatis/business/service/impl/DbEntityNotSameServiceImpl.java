package com.github.yt.mybatis.business.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.business.dao.DbEntityNotSameMapper;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbEntityNotSameService")
@Transactional
public class DbEntityNotSameServiceImpl extends BaseService<DbEntityNotSame> implements DbEntityNotSameService {

    @Resource
    private DbEntityNotSameMapper mapper;

}
