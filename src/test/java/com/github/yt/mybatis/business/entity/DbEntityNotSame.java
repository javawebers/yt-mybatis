package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.business.entity.BusinessBaseEntity;

@Table(name = "db_entity_not_same")
public class DbEntityNotSame extends BusinessBaseEntity<DbEntityNotSame>{
	
	/** 
	 * id  
	 */
	@javax.persistence.Id
	@Column(name="db_entity_not_same_id")
	private String dbEntityNotSameId;
	/** 
	 * boolean类型  
	 */
	@Column(name="test_boolean")
	private Boolean testBoolean;
	/** 
	 * int类型  
	 */
	@Column(name="test_int")
	private Integer testInt;
	/** 
	 * String类型  
	 */
	@Column(name="test_varchar")
	private String testVarchar;
	
	public String getDbEntityNotSameId() {
	    return this.dbEntityNotSameId;
	}
	
	public DbEntityNotSame setDbEntityNotSameId(String dbEntityNotSameId) {
		this.dbEntityNotSameId = dbEntityNotSameId;
		return this;
	}
	
	public Boolean getTestBoolean() {
	    return this.testBoolean;
	}
	
	public DbEntityNotSame setTestBoolean(Boolean testBoolean) {
		this.testBoolean = testBoolean;
		return this;
	}
	
	public Integer getTestInt() {
	    return this.testInt;
	}
	
	public DbEntityNotSame setTestInt(Integer testInt) {
		this.testInt = testInt;
		return this;
	}
	
	public String getTestVarchar() {
	    return this.testVarchar;
	}
	
	public DbEntityNotSame setTestVarchar(String testVarchar) {
		this.testVarchar = testVarchar;
		return this;
	}
}
