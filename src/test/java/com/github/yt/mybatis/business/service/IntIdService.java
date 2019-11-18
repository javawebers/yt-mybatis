package com.github.yt.mybatis.business.service;

import com.github.yt.mybatis.service.IBaseService;
import com.github.yt.mybatis.business.dao.IntIdMapper;
import com.github.yt.mybatis.business.entity.IntId;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * int 主键服务层
 *
 */
public interface IntIdService extends IBaseService<IntId> {
    @Override
    int save(IntId entity);
}
