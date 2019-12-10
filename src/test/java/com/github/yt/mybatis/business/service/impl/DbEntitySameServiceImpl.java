package com.github.yt.mybatis.business.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.business.dao.DbEntitySameMapper;
import com.github.yt.mybatis.business.entity.DbEntitySame;
import com.github.yt.mybatis.business.service.DbEntitySameService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbEntitySameService")
@Transactional
public class DbEntitySameServiceImpl extends BaseService<DbEntitySame> implements DbEntitySameService {

    @Resource
    private DbEntitySameMapper mapper;

}
