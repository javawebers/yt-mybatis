package com.github.yt.mybatis.example.service;

import com.github.yt.mybatis.service.IBaseService;
import com.github.yt.mybatis.example.entity.IntId;

import java.util.List;

/**
 * int 主键服务层
 *
 */
public interface IntIdService extends IBaseService<IntId> {
    @Override
    int save(IntId entity);

    int saveBatch2(List<IntId> entityCollection);
}
