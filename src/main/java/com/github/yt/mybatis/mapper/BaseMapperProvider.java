package com.github.yt.mybatis.mapper;

import com.github.yt.commons.query.Query;
import com.github.yt.mybatis.domain.BaseEntity;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.query.SqlUtils;
import com.github.yt.mybatis.utils.BaseEntityUtils;
import com.github.yt.mybatis.utils.EntityUtils;
import com.github.yt.mybatis.utils.YtStringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BaseMapperProvider {

    public String get(Map paramMap) {
        Class entityClass = (Class) paramMap.get("entityClass");
        Field idField = EntityUtils.getIdField(entityClass);

        Query query = new Query();
        query.addWhere("t." + idField.getName() + " = #{id}");
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectAndFrom(entityClass, null);
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(null, query);
        return mybatisSql;
    }

    private String findSql(Map paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectAndFrom(entityCondition.getClass(), query);
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(entityCondition, query);
        return mybatisSql;
    }

    public String findList(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = findSql(paramMap);
        mybatisSql += SqlUtils.getGroupBy(query);
        mybatisSql += SqlUtils.getOrderBy(query);
        mybatisSql += SqlUtils.getLimit(query);
        return mybatisSql;

    }

    public String logicDelete(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        StringBuffer sql = new StringBuffer();
        // update
        sql.append("update ").append(EntityUtils.getTableName(entityCondition.getClass())).append(" t ");
        // join
        sql.append(SqlUtils.getJoinAndOnCondition(query));
        // set
        query.addUpdate("t.deleteFlag = true");
        setUpdateBaseColumn(paramMap);
        sql.append(SqlUtils.getUpdateSet(query));
        // where
        query.addWhere("t.deleteFlag = false");
        sql.append(SqlUtils.getWhere(entityCondition, query));
        return sql.toString();
    }

    public String delete(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        StringBuffer sql = new StringBuffer();
        // delete
        sql.append("delete from ").append(EntityUtils.getTableName(entityCondition.getClass()));
        // where
        sql.append(SqlUtils.getWhere(entityCondition, query, ""));
        return sql.toString();
    }

    public String deleteById(Map paramMap) {
        Class entityClass = (Class) paramMap.get("entityClass");
        Field idField = EntityUtils.getIdField(entityClass);
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ").append(EntityUtils.getTableName(entityClass));
        sql.append(" where ").append(idField.getName()).append(" = #{id}");
        return sql.toString();
    }

    private void setUpdateBaseColumn(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        if (entityCondition instanceof BaseEntity && query.takeUpdateBaseColumn()) {
            String modifierId = BaseEntityUtils.getModifierId();
            String modifierName = BaseEntityUtils.getModifierName();
            Date modifyTime = new Date();
            paramMap.put("_modifierId_", modifierId);
            paramMap.put("_modifierName_", modifierName);
            paramMap.put("_modifyDateTime_", modifyTime);
            query.addUpdate("t.modifierId = #{_modifierId_}, t.modifierName = #{_modifierName_}, t.modifyDateTime = #{_modifyDateTime_}");
        }
    }

    public String updateByCondition(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        StringBuffer sql = new StringBuffer();
        // update
        sql.append("update ").append(EntityUtils.getTableName(entityCondition.getClass())).append(" t ");
        // join
        sql.append(SqlUtils.getJoinAndOnCondition(query));
        // set
        setUpdateBaseColumn(paramMap);
        sql.append(SqlUtils.getUpdateSet(query));

        // where
        sql.append(SqlUtils.getWhere(entityCondition, query));
        return sql.toString();
    }

    public String count(Map paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += SqlUtils.getSelectCountAndFrom(entityCondition.getClass());
        mybatisSql += SqlUtils.getJoinAndOnCondition(query);
        mybatisSql += SqlUtils.getWhere(entityCondition, query);
        mybatisSql += SqlUtils.getGroupBy(query);
        return mybatisSql;
    }

    public String findPageList(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        String mybatisSql = "";
        mybatisSql += findList(paramMap);
        return mybatisSql;
    }

    public <T> String saveBatch(Map paramMap) {
        Collection<T> entityCollection = (Collection<T>) paramMap.get("entityCollection");
        String tableName = null;
        Set<String> fieldNameSet = new HashSet<>();
        List<String> fieldNameList = new ArrayList<>();
        List<String> dbFieldNameList = new ArrayList<>();
        for (T entity : entityCollection) {
            if (YtStringUtils.isBlank(tableName)) {
                tableName = EntityUtils.getTableName(entity.getClass());
            }
            for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
                if (!fieldNameSet.contains(field.getName())) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldNameSet.add(field.getName());
                        fieldNameList.add(field.getName());
                        dbFieldNameList.add("`" + field.getName() + "`" );
                    }
                }
            }
        }
        StringBuffer valueParams = new StringBuffer();
        for (int i = 0; i < entityCollection.size(); i++) {
            valueParams.append("(");
            for (int j = 0; j < fieldNameList.size(); j++) {
                String fieldName = fieldNameList.get(j);
                valueParams.append("#{").append(fieldName).append("__").append(i).append("__}");
                if (j != fieldNameList.size() - 1) {
                    valueParams.append(", ");
                }
            }
            valueParams.append(")");
            if (i != entityCollection.size() - 1) {
                valueParams.append(", ");
            }
        }
        StringBuffer sql = new StringBuffer();
        // insert into
        sql.append("insert into `").append(tableName).append("` ");
        // fields

        sql.append(" (").append(YtStringUtils.join(dbFieldNameList.toArray(), ", ")).append(") ");
        // values
        sql.append(" values ").append(valueParams);
        return sql.toString();
    }

    // 设置修改人信息
    private <T> void setModifier(T entity){
        if (!(entity instanceof BaseEntity)) {
            return;
        }
        BaseEntity baseEntity = (BaseEntity)entity;
        if (YtStringUtils.isEmpty(baseEntity.getModifierId())) {
            baseEntity.setModifierId(BaseEntityUtils.getModifierId());
        }
        if (YtStringUtils.isEmpty(baseEntity.getModifierName())) {
            baseEntity.setModifierName(BaseEntityUtils.getModifierName());
        }
        if (baseEntity.getModifyDateTime() == null) {
            baseEntity.setModifyDateTime(new Date());
        }
    }

    /**
     * 生成更新语句
     * @param entity
     * @param isUpdateNullField
     * @param selectedFieldNames 为空更新所有字段，不为空则更新指定字段。
     * @param <T>
     * @return
     */
    private <T> String update(T entity, boolean isUpdateNullField, String... selectedFieldNames) {
        // 判断是否更新指定字段
        boolean isUpdateSelectedField = false;
        Set<String> selectedFieldNameSet = null;
        if (selectedFieldNames != null && selectedFieldNames.length > 0) {
            isUpdateSelectedField = true;
            selectedFieldNameSet = new HashSet<>(Arrays.asList(selectedFieldNames));
            selectedFieldNameSet.add("modifierId");
            selectedFieldNameSet.add("modifierName");
            selectedFieldNameSet.add("modifyDateTime");
        }
        // 设置修改人信息
        setModifier(entity);

        Field idField = null;
        String sql = "";

        Table tableAnnotation = entity.getClass().getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        List<String> fieldParamList = new ArrayList<>();
        for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
            field.setAccessible(true);
            //处理主键
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                idField = field;
                continue;
            }
            if (!isUpdateNullField) {
                if (EntityUtils.getValue(entity, field.getName()) == null) {
                    continue;
                }
            }
            if (isUpdateSelectedField) {
                if(!selectedFieldNameSet.contains(field.getName())){
                    continue;
                }
            }
            fieldParamList.add("`" + field.getName() + "` = #{entity." + field.getName() + "}");
        }
        String update = "update `" + tableName + "`";
        String set = " set " + YtStringUtils.join(fieldParamList.toArray(), ", ");
        String where = " where `" + idField.getName() + "` = #{entity." + idField.getName() + "}";
        sql += update;
        sql += set;
        sql += where;
        return sql;

    }

    public <T> String update(Map paramMap) {
        T entity = (T)paramMap.get("entity");
        String[] fieldNames = (String[])paramMap.get("fieldNames");
        return update(entity, true, fieldNames);
    }

    public <T> String updateNotNull(Map paramMap) {
        T entity = (T)paramMap.get("entity");
        String[] fieldNames = (String[])paramMap.get("fieldNames");
        return update(entity, false, fieldNames);
    }

}
