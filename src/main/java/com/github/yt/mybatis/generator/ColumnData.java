package com.github.yt.mybatis.generator;


public class ColumnData {

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getColumnNameContainEntity() {
		return columnNameContainEntity;
	}

	public void setColumnNameContainEntity(String columnNameContainEntity) {
		this.columnNameContainEntity = columnNameContainEntity;
	}

	public Boolean getPriKey() {
		return isPriKey;
	}

	public void setPriKey(Boolean priKey) {
		isPriKey = priKey;
	}

	public Long getColumnLength() {
		return columnLength;
	}

	public void setColumnLength(Long columnLength) {
		this.columnLength = columnLength;
	}

	public Boolean getNullable() {
		return isNullable;
	}

	public void setNullable(Boolean nullable) {
		isNullable = nullable;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public Boolean getBaseEntityColumn() {
		return isBaseEntityColumn;
	}

	public void setBaseEntityColumn(Boolean baseEntityColumn) {
		isBaseEntityColumn = baseEntityColumn;
	}
}
