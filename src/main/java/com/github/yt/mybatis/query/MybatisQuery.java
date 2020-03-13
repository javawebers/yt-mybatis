package com.github.yt.mybatis.query;

import com.github.yt.commons.query.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * mybatis 查询条件
 *
 * @author sheng
 */
public interface MybatisQuery<T extends MybatisQuery<?>> extends PageQuery<T> {

    // apis

    /**
     * 添加扩展的查询字段
     *
     * @param extendSelectColumn 扩展 select 的 column ,如: "count(num) as countNum"
     * @return this
     */
    T addExtendSelectColumn(String extendSelectColumn);

    /**
     * 添加排除的查询字段
     * 如: 忽略敏感字段, 业务中不需要的字段等
     * 如: create_time
     *
     * @param excludeSelectColumn 排除的查询字段
     * @return this
     */
    T addExcludeSelectColumn(String excludeSelectColumn);

    /**
     * 排除所有默认的表字段, 结合 addSelectColumn 使用,只查询业务中用到的字段
     *
     * @return this
     */
    T excludeAllSelectColumn();

    /**
     * 更新基本字段,如 modifyTime, modifierId, modifierName
     *
     * @param updateBaseColumn true/false,默认true
     * @return this
     */
    T updateBaseColumn(boolean updateBaseColumn);

    /**
     * 添加 mybatis 的参数 #{name}
     *
     * @param paramName  参数名 name
     * @param paramValue 参数值 张三
     * @return this
     */
    T addParam(String paramName, Object paramValue);

    /**
     * 添加更新的字段
     * 多个更新字段 "," 分割
     *
     * @param updateColumn 更新的字段 "name = '张三'"
     * @return this
     */
    T addUpdate(String updateColumn);

    /**
     * 添加查询条件
     * 多个查询条件之间 "and" 拼接
     *
     * @param where 查询条件, username = #{username}
     * @return this
     */
    T addWhere(String where);

    /**
     * 添加排序字段
     *
     * @param columns 排序字段
     * @return this
     */
    T addOrderBy(String columns);

    /**
     * 添加分组字段
     *
     * @param columns 分组字段
     * @return this
     */
    T addGroupBy(String columns);

    /**
     * limit
     *
     * @param limit 开始条数，包含，从 0 开始
     * @param size  查询条数
     * @return this
     */
    T limit(int limit, int size);

    /**
     * 添加 join 查询
     *
     * @param joinType                 joinType
     * @param tableNameAndOnConditions 表名和连接条件, 如: "t_user t2 on t.user_id = t2.userId"
     * @return this
     */
    T addJoin(QueryJoinType joinType, String tableNameAndOnConditions);

    /**
     * 常用操作，大于
     * 当值为空时不进行拼接
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T gt(String columnName, Object value);

    /**
     * 常用操作，大于等于
     * 当值为空时不进行拼接
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T ge(String columnName, Object value);

    /**
     * 常用操作，小于
     * 当值为空时不进行拼接
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T lt(String columnName, Object value);

    /**
     * 常用操作，小于等于
     * 当值为空时不进行拼接
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T le(String columnName, Object value);

    /**
     * 常用操作，模糊查询
     * 默认为 %value%
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T like(String columnName, String value);

    /**
     * 常用操作，模糊查询
     *
     * @param columnName 字段
     * @param value      值
     * @param likeType   模糊查询类型
     * @return this
     */
    T like(String columnName, String value, QueryLikeType likeType);

    /**
     * 常用操作，等于
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T equal(String columnName, Object value);

    /**
     * 常用操作，不等于
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T notEqual(String columnName, Object value);

    /**
     * 常用操作，in
     *
     * @param columnName 字段
     * @param firstValue 第一个值。可以是集合、数组、其他 Object 类型
     * @param moreValues 其他值。当第一个值为集合或者数组时，这个值忽略掉
     * @return this
     */
    T in(String columnName, Object firstValue, Object... moreValues);

    /**
     * 常用操作，not in
     *
     * @param columnName 字段
     * @param firstValue 第一个值。可以是集合、数组、其他 Object 类型
     * @param moreValues 其他值。当第一个值为集合或者数组时，这个值忽略掉
     * @return this
     */
    T notIn(String columnName, Object firstValue, Object... moreValues);

    /**
     * 常用操作，更新
     *
     * @param columnName 字段
     * @param value      值
     * @return this
     */
    T update(String columnName, Object value);

    /**
     * 获取参数
     *
     * @return Param
     */
    Map<String, Object> takeParam();

    /**
     * 是否更新基础字段
     *
     * @return UpdateBaseColumn
     */
    boolean takeUpdateBaseColumn();

    /**
     * in 查询参数集合
     *
     * @return InParamList
     */
    List<QueryInCondition> takeInParamList();

    /**
     * 更新字段集合
     *
     * @return UpdateColumnList
     */
    List<String> takeUpdateColumnList();

    /**
     * 扩展字段集合
     *
     * @return ExtendSelectColumnList
     */
    List<String> takeExtendSelectColumnList();

    /**
     * 排除字段集合
     *
     * @return ExcludeSelectColumnList
     */
    List<String> takeExcludeSelectColumnList();

    /**
     * 是否排除所有字段
     *
     * @return ExcludeAllSelectColumn
     */
    boolean takeExcludeAllSelectColumn();

    /**
     * 获取查询条件集合
     *
     * @return WhereList
     */
    List<String> takeWhereList();

    /**
     * 获取 order by 集合
     *
     * @return OrderByList
     */
    List<String> takeOrderByList();

    /**
     * 获取 group by
     *
     * @return GroupBy
     */
    String takeGroupBy();

    /**
     * 获取 join 集合
     *
     * @return JoinList
     */
    List<QueryJoin> takeJoinList();

    /**
     * 获取 limit 开始
     *
     * @return LimitFrom
     */
    Integer takeLimitFrom();

    /**
     * 获取 limit 条数
     *
     * @return LimitSize
     */
    Integer takeLimitSize();

}
