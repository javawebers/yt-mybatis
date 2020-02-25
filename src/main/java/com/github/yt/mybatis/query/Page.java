package com.github.yt.mybatis.query;


import java.util.LinkedHashMap;
import java.util.List;

/**
 * 分页数据
 *
 * @author liujiasheng
 */
public class Page<T> extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 80238317179585389L;

    private String pageNoName = "pageNo";
    private String pageSizeName = "pageSize";
    private String pageTotalCountName = "pageTotalCount";
    private String pageDataName = "data";

    public Page() {
    }

    public Page<T> initKey(String pageNoName, String pageSizeName,
                        String pageTotalCountName, String pageDataName) {
        this.pageNoName = pageNoName;
        this.pageSizeName = pageSizeName;
        this.pageTotalCountName = pageTotalCountName;
        this.pageDataName = pageDataName;
        return this;
    }

    public Page<T> initValue(int pageNo, int pageSize, int totalCount, List<T> data) {
        return setPageNo(pageNo).
                setPageSize(pageSize).
                setTotalCount(totalCount).
                setData(data);
    }

    private Page<T> setPageNo(int pageNo) {
        this.put(pageNoName, pageNo);
        return this;
    }

    private Page<T> setPageSize(int pageSize) {
        this.put(pageSizeName, pageSize);
        return this;
    }

    private Page<T> setTotalCount(int totalCount) {
        this.put(pageTotalCountName, totalCount);
        return this;
    }

    private Page<T> setData(List<T> data) {
        this.put(pageDataName, data);
        return this;
    }

    public int getPageNo() {
        return (Integer) this.get(pageNoName);
    }

    public int getPageSize() {
        return (Integer) this.get(pageSizeName);
    }

    public int getTotalCount() {
        return (Integer) this.get(pageTotalCountName);
    }

    @SuppressWarnings("unchecked")
    public List<T> getData() {
        return (List<T>) this.get(pageDataName);
    }
}
