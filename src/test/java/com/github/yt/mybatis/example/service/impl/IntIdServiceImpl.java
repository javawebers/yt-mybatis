package com.github.yt.mybatis.example.service.impl;

import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.example.dao.IntIdMapper;
import com.github.yt.mybatis.example.entity.IntId;
import com.github.yt.mybatis.example.service.IntIdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("intIdService")
@Transactional
public class IntIdServiceImpl extends BaseService<IntId> implements IntIdService {

	@Autowired
    private IntIdMapper mapper;

    @Override
    public int save(IntId entity) {
        return mapper.save(entity);
    }

    @Override
    public int saveBatch2(List<IntId> entityCollection) {
        return mapper.saveBatch(entityCollection);
    }
}
