package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;

@Table(name = "db_entity_not_same")
public class DbEntityNotSame extends com.github.yt.mybatis.business.entity.BaseEntity<DbEntityNotSame>{
	
	/** 
	 * id  
	 */
	@javax.persistence.Id
	@Column(name="db_entity_not_same_id")
	private String dbEntityNotSameId;
	/** 
	 * String类型  
	 */
	@Column(name="test_varchar")
	private String testVarchar;
	/** 
	 * int类型  
	 */
	@Column(name="test_int")
	private Integer testInt;
	/** 
	 * boolean类型  
	 */
	@Column(name="test_boolean")
	private Boolean testBoolean;
	/** 
	 *   
	 */
	@Column(name="founder_id")
	private String founderId;
	/** 
	 *   
	 */
	@Column(name="founder_name")
	private String founderName;
	/** 
	 *   
	 */
	@Column(name="create_time")
	private java.util.Date createTime;
	/** 
	 *   
	 */
	@Column(name="modifier_id")
	private String modifierId;
	/** 
	 *   
	 */
	@Column(name="modifier_name")
	private String modifierName;
	/** 
	 *   
	 */
	@Column(name="modify_time")
	private java.util.Date modifyTime;
	/** 
	 *   
	 */
	@Column(name="delete_flag")
	private Boolean deleteFlag;
	
	public String getDbEntityNotSameId() {
	    return this.dbEntityNotSameId;
	}
	
	public DbEntityNotSame setDbEntityNotSameId(String dbEntityNotSameId) {
		this.dbEntityNotSameId = dbEntityNotSameId;
		return this;
	}
	
	public String getTestVarchar() {
	    return this.testVarchar;
	}
	
	public DbEntityNotSame setTestVarchar(String testVarchar) {
		this.testVarchar = testVarchar;
		return this;
	}
	
	public Integer getTestInt() {
	    return this.testInt;
	}
	
	public DbEntityNotSame setTestInt(Integer testInt) {
		this.testInt = testInt;
		return this;
	}
	
	public Boolean getTestBoolean() {
	    return this.testBoolean;
	}
	
	public DbEntityNotSame setTestBoolean(Boolean testBoolean) {
		this.testBoolean = testBoolean;
		return this;
	}
	
	public String getFounderId() {
	    return this.founderId;
	}
	
	public DbEntityNotSame setFounderId(String founderId) {
		this.founderId = founderId;
		return this;
	}
	
	public String getFounderName() {
	    return this.founderName;
	}
	
	public DbEntityNotSame setFounderName(String founderName) {
		this.founderName = founderName;
		return this;
	}
	
	public java.util.Date getCreateTime() {
	    return this.createTime;
	}
	
	public DbEntityNotSame setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
		return this;
	}
	
	public String getModifierId() {
	    return this.modifierId;
	}
	
	public DbEntityNotSame setModifierId(String modifierId) {
		this.modifierId = modifierId;
		return this;
	}
	
	public String getModifierName() {
	    return this.modifierName;
	}
	
	public DbEntityNotSame setModifierName(String modifierName) {
		this.modifierName = modifierName;
		return this;
	}
	
	public java.util.Date getModifyTime() {
	    return this.modifyTime;
	}
	
	public DbEntityNotSame setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
		return this;
	}
	
	public Boolean getDeleteFlag() {
	    return this.deleteFlag;
	}
	
	public DbEntityNotSame setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
		return this;
	}
}
