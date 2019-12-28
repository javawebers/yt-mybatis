package com.github.yt.mybatis.mapper;

import com.github.yt.commons.exception.BaseAccidentException;
import com.github.yt.commons.util.YtStringUtils;
import com.github.yt.mybatis.YtMybatisExceptionEnum;
import com.github.yt.mybatis.dialect.DialectHandler;
import com.github.yt.mybatis.entity.YtColumnType;
import com.github.yt.mybatis.query.ParamUtils;
import com.github.yt.mybatis.query.Query;
import com.github.yt.mybatis.query.SqlUtils;
import com.github.yt.mybatis.util.BaseEntityUtils;
import com.github.yt.mybatis.util.EntityUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author liujiasheng
 */
public class BaseMapperProvider {


    public String get(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        SqlUtils.select(sql, entityCondition.getClass(), query);
        SqlUtils.from(sql, entityCondition.getClass(), query);
        SqlUtils.where(sql, entityCondition, query, "t.");
        return sql.toString();
    }

    public String findList(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        SqlUtils.select(sql, entityCondition.getClass(), query);
        SqlUtils.from(sql, entityCondition.getClass(), query);
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, "t.");
        SqlUtils.groupBy(sql, query);
        SqlUtils.orderBy(sql, query);
        SqlUtils.limitOffset(sql, query);
        return SqlUtils.replaceInParam(sql, query);
    }


    public String count(Map<String, Object> paramMap) {
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);

        SQL sql = new SQL();
        sql.SELECT("count(*)");
        SqlUtils.from(sql, entityCondition.getClass(), query);
        SqlUtils.join(sql, query);
        SqlUtils.where(sql, entityCondition, query, "t.");
        SqlUtils.groupBy(sql, query);
        return SqlUtils.replaceInParam(sql, query);
    }

    public String logicDelete(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        // update
        sql.UPDATE(EntityUtils.getTableName(entityCondition.getClass()) + " t");
        // join
        SqlUtils.join(sql, query);
        // set
        SqlUtils.set(sql, query);
        SqlUtils.where(sql, entityCondition, query, "t.");
        return SqlUtils.replaceInParam(sql, query);
    }

    public String delete(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        sql.DELETE_FROM(EntityUtils.getTableName(entityCondition.getClass()));
        SqlUtils.where(sql, entityCondition, query, null);
        return SqlUtils.replaceInParam(sql, query);
    }

    public String updateByCondition(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        // update
        sql.UPDATE(EntityUtils.getTableName(entityCondition.getClass()) + " t");
        // join
        SqlUtils.join(sql, query);
        // set
        SqlUtils.set(sql, query);
        // where
        SqlUtils.where(sql, entityCondition, query, "t.");
        return SqlUtils.replaceInParam(sql, query);
    }

    public <T> String updateNew(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        SQL sql = new SQL();
        sql.UPDATE(EntityUtils.getTableName(entityCondition.getClass()) + " t");
        SqlUtils.set(sql, query);
        SqlUtils.where(sql, entityCondition, query, "t.");
        return sql.toString();
    }

    /**
     * 生成更新语句
     *
     * @param selectedFieldColumnNames 为空更新所有字段，不为空则更新指定字段。
     */
    private <T> String update(T entity, boolean isUpdateNullField, String... selectedFieldColumnNames) {
        // 判断是否更新指定字段
        Set<String> selectedFieldColumnNameSet = getSelectedFieldColumnNameSet(selectedFieldColumnNames, entity);

        // 设置修改人信息
        setModifier(entity);

        Field idField = EntityUtils.getIdField(entity.getClass());
        Table tableAnnotation = entity.getClass().getAnnotation(Table.class);
        String tableName = tableAnnotation.name();
        List<String> fieldParamList = new ArrayList<>();
        for (Field field : EntityUtils.getTableFieldList(entity.getClass())) {
            field.setAccessible(true);
            //处理主键
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                continue;
            }
            if (!isUpdateNullField) {
                if (EntityUtils.getValue(entity, field) == null) {
                    continue;
                }
            }
            if (selectedFieldColumnNameSet != null && selectedFieldColumnNameSet.size() > 0) {
                if (!selectedFieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    continue;
                }
            }
            fieldParamList.add("`" + EntityUtils.getFieldColumnName(field) + "` = #{entity." + field.getName() + "}");
        }

        SQL sql = new SQL();
        sql.UPDATE(tableName);
        for (String fieldParam : fieldParamList) {
            sql.SET(fieldParam);
        }
        sql.WHERE("`" + EntityUtils.getFieldColumnName(idField) + "` = #{entity." + idField.getName() + "}");
        return sql.toString();
    }

    public <T> String update(Map<String, Object> paramMap) {
        T entity = (T) paramMap.get("entity");
        String[] fieldColumnNames = (String[]) paramMap.get("fieldColumnNames");
        return update(entity, true, fieldColumnNames);
    }

    public <T> String updateNotNull(Map<String, Object> paramMap) {
        T entity = (T) paramMap.get("entity");
        String[] fieldColumnNames = (String[]) paramMap.get("fieldColumnNames");
        return update(entity, false, fieldColumnNames);
    }

    public <T> String saveBatch(Map<String, Object> map) {
        Collection<T> entityCollection = (Collection<T>) map.get("collection");

        String tableName = null;
        List<Field> tableFieldList = null;
        Set<String> fieldColumnNameSet = new HashSet<>();
        List<String> fieldNameList = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        List<List<String>> valuesList = new ArrayList<>();

        // 收集所有有值的字段，没有值的字段不拼接 sql
        for (T entity : entityCollection) {
            if (YtStringUtils.isBlank(tableName)) {
                tableName = EntityUtils.getTableName(entity.getClass());
                tableFieldList = EntityUtils.getTableFieldList(entity.getClass());
            }
            for (Field field : tableFieldList) {
                if (!fieldColumnNameSet.contains(EntityUtils.getFieldColumnName(field))) {
                    if (EntityUtils.getValue(entity, field) != null) {
                        fieldColumnNameSet.add(EntityUtils.getFieldColumnName(field));
                        fieldNameList.add(field.getName());
                        columnNameList.add(DialectHandler.getDialect().getColumnName(field));
                    }
                }
            }
        }
        for (int i = 0; i < entityCollection.size(); i++) {
            List<String> values = new ArrayList<>();
            for (String fieldColumnName : fieldNameList) {
                values.add("#{collection[" + i + "]." + fieldColumnName + "}");
            }
            valuesList.add(values);
        }

        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        for (String column : columnNameList) {
            sql.INTO_COLUMNS(column);
        }
        for (List<String> values : valuesList) {
            for (String value:values) {
                sql.INTO_VALUES(value);
            }
            sql.ADD_ROW();
        }
        return sql.toString();
    }

    private <T> Set<String> getSelectedFieldColumnNameSet(String[] selectedFieldColumnNames, T entity) {
        Set<String> selectedFieldColumnNameSet = null;
        if (selectedFieldColumnNames != null && selectedFieldColumnNames.length > 0) {
            selectedFieldColumnNameSet = new HashSet<>(Arrays.asList(selectedFieldColumnNames));
            Field modifierIdField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entity.getClass(), YtColumnType.MODIFY_TIME);
            if (modifierIdField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifierIdField));
            }
            if (modifierNameField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifierNameField));
            }
            if (modifyTimeField != null) {
                selectedFieldColumnNameSet.add(EntityUtils.getFieldColumnName(modifyTimeField));
            }
        }
        return selectedFieldColumnNameSet;
    }

    /**
     * 设置修改人信息
     *
     * @param entity 实体类
     */
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

    private void setUpdateBaseColumn(Map<String, Object> paramMap) {
        Query query = (Query) paramMap.get(ParamUtils.QUERY_OBJECT);
        Object entityCondition = paramMap.get(ParamUtils.ENTITY_CONDITION);

        if (query.takeUpdateBaseColumn()) {
            Field modifierIdField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_ID);
            Field modifierNameField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFIER_NAME);
            Field modifyTimeField = EntityUtils.getYtColumnField(entityCondition.getClass(), YtColumnType.MODIFY_TIME);
            if (modifierIdField != null) {
                String modifierIdColumn = EntityUtils.getFieldColumnName(modifierIdField);
                paramMap.put("_modifierId_", BaseEntityUtils.getModifierId());
                query.addUpdate("t." + modifierIdColumn + " = #{_modifierId_}");
            }
            if (modifierNameField != null) {
                String modifierNameColumn = EntityUtils.getFieldColumnName(modifierNameField);
                paramMap.put("_modifierName_", BaseEntityUtils.getModifierName());
                query.addUpdate("t." + modifierNameColumn + " = #{_modifierName_}");
            }
            if (modifyTimeField != null) {
                String modifyTimeColumn = EntityUtils.getFieldColumnName(modifyTimeField);
                paramMap.put("_modifyTime_", new Date());
                query.addUpdate("t." + modifyTimeColumn + " = #{_modifyTime_}");
            }
        }
    }

}
