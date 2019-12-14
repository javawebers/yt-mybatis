package com.github.yt.mybatis.generator;


import java.util.List;

/**
 * @author sheng
 */
public class ColumnData {
    private String tableName;
    private String className;
    private String columnName;
    private String fieldName;
    private String dataType;
    private String columnComment;
    private String columnNameContainEntity;
    private Boolean isPriKey;
    private Long columnLength;
    private Boolean isNullable;
    private String columnDefault;
    private Boolean isBaseEntityColumn;
    private String columnType;

    /**
     * 枚举类名
     */
    private String enumClassName;
    /**
     * 枚举类名 枚举类的值列表
     */
    private List<EnumColumnData> enumColumnDataList;


    public String getTableName() {
        return tableName;
    }

    public ColumnData setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public ColumnData setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getEnumClassName() {
        return enumClassName;
    }

    public ColumnData setEnumClassName(String enumClassName) {
        this.enumClassName = enumClassName;
        return this;
    }

    public List<EnumColumnData> getEnumColumnDataList() {
        return enumColumnDataList;
    }

    public ColumnData setEnumColumnDataList(List<EnumColumnData> enumColumnDataList) {
        this.enumColumnDataList = enumColumnDataList;
        return this;
    }

    public String getColumnType() {
        return columnType;
    }

    public ColumnData setColumnType(String columnType) {
        this.columnType = columnType;
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public ColumnData setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ColumnData setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public ColumnData setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public ColumnData setColumnComment(String columnComment) {
        this.columnComment = columnComment;
        return this;
    }

    public String getColumnNameContainEntity() {
        return columnNameContainEntity;
    }

    public ColumnData setColumnNameContainEntity(String columnNameContainEntity) {
        this.columnNameContainEntity = columnNameContainEntity;
        return this;
    }

    public Boolean getPriKey() {
        return isPriKey;
    }

    public ColumnData setPriKey(Boolean priKey) {
        isPriKey = priKey;
        return this;
    }

    public Long getColumnLength() {
        return columnLength;
    }

    public ColumnData setColumnLength(Long columnLength) {
        this.columnLength = columnLength;
        return this;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public ColumnData setNullable(Boolean nullable) {
        isNullable = nullable;
        return this;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public ColumnData setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
        return this;
    }

    public Boolean getBaseEntityColumn() {
        return isBaseEntityColumn;
    }

    public ColumnData setBaseEntityColumn(Boolean baseEntityColumn) {
        isBaseEntityColumn = baseEntityColumn;
        return this;
    }
}
