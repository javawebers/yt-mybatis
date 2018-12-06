package com.github.yt.mybatis.query;

import com.github.yt.commons.query.Page;
import com.github.yt.mybatis.YtMybatisConfig;

import java.util.List;

/**
 * 分页工具类
 * @author liujiasheng
 */
public class PageUtils {


    /**
     * 根据老分页生成新分页
     * @param oldPage
     * @param resultList
     * @param <R>
     * @param <O>
     * @return
     */
    public static <R, O> Page createPage(Page<O> oldPage, List<R> resultList) {
        return createPage(oldPage.getPageNo(), oldPage.getPageSize(), oldPage.getTotalCount(),resultList);
    }

    /**
     * 创建一个分页数据
     * @param pageNo
     * @param pageSize
     * @param totalCount
     * @param data
     * @param <R>
     * @return
     */
    public static <R> Page createPage(int pageNo, int pageSize, long totalCount, List<R> data) {
        return new Page<R>()
                .initKey(YtMybatisConfig.pageNoName, YtMybatisConfig.pageSizeName, YtMybatisConfig.pageTotalCountName, YtMybatisConfig.pageDataName)
                .initValue(pageNo, pageSize, totalCount, data);
    }


}
