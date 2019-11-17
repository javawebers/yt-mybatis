package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;

@Table(name = "DbEntitySame")
public class DbEntitySame extends com.github.yt.mybatis.entity.BaseEntity<DbEntitySame>{
	
	/** 
	 * id  
	 */
	@javax.persistence.Id
	private String dbEntitySameId;
	/** 
	 * String类型  
	 */
	private String testVarchar;
	/** 
	 * int类型  
	 */
	private Integer testInt;
	/** 
	 * boolean类型  
	 */
	private Boolean testBoolean;
	
	public String getDbEntitySameId() {
	    return this.dbEntitySameId;
	}
	
	public DbEntitySame setDbEntitySameId(String dbEntitySameId) {
		this.dbEntitySameId = dbEntitySameId;
		return this;
	}
	
	public String getTestVarchar() {
	    return this.testVarchar;
	}
	
	public DbEntitySame setTestVarchar(String testVarchar) {
		this.testVarchar = testVarchar;
		return this;
	}
	
	public Integer getTestInt() {
	    return this.testInt;
	}
	
	public DbEntitySame setTestInt(Integer testInt) {
		this.testInt = testInt;
		return this;
	}
	
	public Boolean getTestBoolean() {
	    return this.testBoolean;
	}
	
	public DbEntitySame setTestBoolean(Boolean testBoolean) {
		this.testBoolean = testBoolean;
		return this;
	}
}
