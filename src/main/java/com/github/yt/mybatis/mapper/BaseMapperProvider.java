package com.github.yt.mybatis.mapper;

import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.SqlUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Collection;
import java.util.Map;

/**
 * @author liujiasheng
 */
public class BaseMapperProvider {

    public <T> String saveBatch(Map<String, Collection<T>> map) {
        Collection<T> entityCollection = map.get("collection");
        return DialectHandler.getDialect().getInsertSql(entityCollection);
    }

    public String delete(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        sql.DELETE_FROM(DialectHandler.getDialect().getTableName(entityCondition.getClass()));
        SqlUtils.where(sql, entityCondition, query, false);
        return SqlUtils.replaceInParam(sql, query);
    }

    public <T> String update(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        sql.UPDATE(DialectHandler.getDialect().getTableNameWithAlas(entityCondition.getClass()));
        SqlUtils.set(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        return SqlUtils.replaceInParam(sql, query);
    }

    public String findList(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        SqlUtils.select(sql, entityCondition.getClass(), query);
        SqlUtils.from(sql, entityCondition.getClass());
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        SqlUtils.groupBy(sql, query);
        SqlUtils.orderBy(sql, query);
        String sqlTemp = SqlUtils.replaceInParam(sql, query);
        return SqlUtils.limitOffset(sqlTemp, query);
    }

    public String count(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        sql.SELECT("count(*)");
        SqlUtils.from(sql, entityCondition.getClass());
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, true);
        SqlUtils.groupBy(sql, query);
        return SqlUtils.replaceInParam(sql, query);
    }


}
