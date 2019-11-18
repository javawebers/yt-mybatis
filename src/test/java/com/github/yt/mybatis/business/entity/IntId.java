package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.entity.BaseEntity;

@Table(name = "IntId")
public class IntId extends BaseEntity<IntId>{
	
	/** 
	 * id  
	 */
	@javax.persistence.Id
	private String intId;
	/** 
	 * String类型  
	 */
	private String testVarchar;
	
	public String getIntId() {
	    return this.intId;
	}
	
	public IntId setIntId(String intId) {
		this.intId = intId;
		return this;
	}
	
	public String getTestVarchar() {
	    return this.testVarchar;
	}
	
	public IntId setTestVarchar(String testVarchar) {
		this.testVarchar = testVarchar;
		return this;
	}
}
