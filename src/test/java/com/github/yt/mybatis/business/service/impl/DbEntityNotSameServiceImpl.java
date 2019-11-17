package com.github.yt.mybatis.business.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.business.dao.DbEntityNotSameMapper;
import com.github.yt.mybatis.business.entity.DbEntityNotSame;
import com.github.yt.mybatis.business.service.DbEntityNotSameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbEntityNotSameService")
@Transactional
public class DbEntityNotSameServiceImpl extends BaseService<DbEntityNotSame> implements DbEntityNotSameService {

	@Autowired
    private DbEntityNotSameMapper mapper;

}
