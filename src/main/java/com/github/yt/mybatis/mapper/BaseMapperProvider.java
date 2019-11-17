package com.github.yt.mybatis.mapper;

import com.github.yt.commons.exception.BaseAccidentException;
import com.github.yt.commons.query.Query;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.entity.YtColumnType;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.query.SqlUtils;
import com.github.yt.mybatis.util.BaseEntityUtils;
import com.github.yt.mybatis.util.EntityUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

public class BaseMapperProvider {

    public String get(Map paramMap) {
        Class entityClass = (Class) paramMap.get("entityClass");
        Field idField = EntityUtils.getIdField(entityClass);

        Query query = new Query();
        query.addWhere("t." + EntityUtils.getFieldColumnName(idField) + " = #{id}");
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

        Field deleteFlagField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.DELETE_FLAG);
        if (deleteFlagField == null) {
            throw new BaseAccidentException(YtMybatisExceptionEnum.CODE_78);
        }
        String deleteFlagColumn = EntityUtils.getFieldColumnName(deleteFlagField);

        StringBuffer sql = new StringBuffer();
        // update
        sql.append("update ").append(EntityUtils.getTableName(entityCondition.getClass())).append(" t ");
        // join
        sql.append(SqlUtils.getJoinAndOnCondition(query));
        // set
        query.addUpdate("t." + deleteFlagColumn + " = true");
        setUpdateBaseColumn(paramMap);
        sql.append(SqlUtils.getUpdateSet(query));
        // where
        query.addWhere("t." + deleteFlagColumn + " = false");
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
        sql.append(" where ").append(EntityUtils.getFieldColumnName(idField)).append(" = #{id}");
        return sql.toString();
    }

    private void setUpdateBaseColumn(Map paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        if (query.takeUpdateBaseColumn()) {
            Field modifierIdField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFY_TIME);
            Object modifierId = null;
            String modifierName = null;
            Date modifyTime = null;
            if (modifierIdField != null) {
                modifierId = BaseEntityUtils.getModifierId();
                String modifierIdColumn = EntityUtils.getFieldColumnName(modifierIdField);
                paramMap.put("_modifierId_", modifierId);
                query.addUpdate("t." + modifierIdColumn + " = #{_modifierId_}");
            }
            if (modifierNameField != null) {
                modifierName = BaseEntityUtils.getModifierName();
                String modifierNameColumn = EntityUtils.getFieldColumnName(modifierNameField);
                paramMap.put("_modifierName_", modifierName);
                query.addUpdate("t." + modifierNameColumn + " = #{_modifierName_}");
            }
            if (modifyTimeField != null) {
                modifyTime = new Date();
                String modifyTimeFColumn = EntityUtils.getFieldColumnName(modifyTimeField);
                paramMap.put("_modifyTime_", modifyTime);
                query.addUpdate("t." + modifyTimeFColumn + " = #{_modifyTime_}");
            }
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
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldColumnNameList = new ArrayList<>();
        List<String> dbFieldColumnNameList = new ArrayList<>();
        for (T entity : entityCollection) {
            if (YtStringUtils.isBlank(tableName)) {
                tableName = EntityUtils.getTableName(entity.getClass());
            }
            for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
                if (!fieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldColumnNameSet.add(EntityUtils.getFieldColumnName(field));
                        fieldColumnNameList.add(EntityUtils.getFieldColumnName(field));
                        dbFieldColumnNameList.add("`" + EntityUtils.getFieldColumnName(field) + "`");
                    }
                }
            }
        }
        StringBuffer valueParams = new StringBuffer();
        for (int i = 0; i < entityCollection.size(); i++) {
            valueParams.append("(");
            for (int j = 0; j < fieldColumnNameList.size(); j++) {
                String fieldColumnName = fieldColumnNameList.get(j);
                valueParams.append("#{").append(fieldColumnName).append("__").append(i).append("__}");
                if (j != fieldColumnNameList.size() - 1) {
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
        sql.append(" (").append(YtStringUtils.join(dbFieldColumnNameList.toArray(), ", ")).append(") ");
        // values
        sql.append(" values ").append(valueParams);
        return sql.toString();
    }

    // 设置修改人信息
    private <T> void setModifier(T entity) {
        Field modifierIdField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_ID);
        Field modifierNameField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_NAME);
        Field modifyTimeField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFY_TIME);
        if (modifierIdField != null) {
            Object modifierId = BaseEntityUtils.getModifierId();
            Object trueModifierId = EntityUtils.getValue(entity, modifierIdField);
            if (trueModifierId == null) {
                EntityUtils.setValue(entity, modifierIdField, modifierId);
            }
        }
        if (modifierNameField != null) {
            String modifierName = BaseEntityUtils.getModifierName();
            Object trueModifierName = EntityUtils.getValue(entity, modifierNameField);
            if (trueModifierName == null) {
                EntityUtils.setValue(entity, modifierNameField, modifierName);
            }
        }
        if (modifyTimeField != null) {
            Date modifyTime = new Date();
            Object trueModifyTime = EntityUtils.getValue(entity, modifyTimeField);
            if (trueModifyTime == null) {
                EntityUtils.setValue(entity, modifyTimeField, modifyTime);
            }
        }
    }

    /**
     * 生成更新语句
     *
     * @param entity
     * @param isUpdateNullField
     * @param selectedFieldColumnNames 为空更新所有字段，不为空则更新指定字段。
     * @param <T>
     * @return
     */
    private <T> String update(T entity, boolean isUpdateNullField, String... selectedFieldColumnNames) {
        // 判断是否更新指定字段
        boolean isUpdateSelectedField = false;
        Set<String> selectedFieldColumnNameSet = null;
        if (selectedFieldColumnNames != null && selectedFieldColumnNames.length > 0) {
            isUpdateSelectedField = true;
            selectedFieldColumnNameSet = new HashSet<>(Arrays.asList(selectedFieldColumnNames));
            selectedFieldColumnNameSet.add("modifierId");
            selectedFieldColumnNameSet.add("modifierName");
            selectedFieldColumnNameSet.add("modifyTime");
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
                if (EntityUtils.getValue(entity, field) == null) {
                    continue;
                }
            }
            if (isUpdateSelectedField) {
                if (!selectedFieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    continue;
                }
            }
            fieldParamList.add("`" + EntityUtils.getFieldColumnName(field) + "` = #{entity." + field.getName() + "}");
        }
        String update = "update `" + tableName + "`";
        String set = " set " + YtStringUtils.join(fieldParamList.toArray(), ", ");
        String where = " where `" + EntityUtils.getFieldColumnName(idField) + "` = #{entity." + idField.getName() + "}";
        sql += update;
        sql += set;
        sql += where;
        return sql;

    }

    public <T> String update(Map paramMap) {
        T entity = (T) paramMap.get("entity");
        String[] fieldColumnNames = (String[]) paramMap.get("fieldColumnNames");
        return update(entity, true, fieldColumnNames);
    }

    public <T> String updateNotNull(Map paramMap) {
        T entity = (T) paramMap.get("entity");
        String[] fieldColumnNames = (String[]) paramMap.get("fieldColumnNames");
        return update(entity, false, fieldColumnNames);
    }

}
