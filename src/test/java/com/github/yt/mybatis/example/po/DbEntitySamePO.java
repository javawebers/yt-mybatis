package com.github.yt.mybatis.example.po;

import javax.persistence.Table;

import com.github.yt.mybatis.entity.BaseEntity;

import java.io.Serializable;

@Table(name = "DbEntitySame")
public class DbEntitySamePO<T extends DbEntitySamePO<T>> extends BaseEntity<T> implements Serializable {

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
    /** 
     * enum 类型，MALE:男 ，FEMALE:女  
     */
    private DbEntitySameTestEnumEnum testEnum;
    /** 
     * enum 类型，MALE:男 ，FEMALE:女，OTHER:其他  
     */
    private DbEntitySameTestEnum2Enum testEnum2;
    
    public String getDbEntitySameId() {
        return this.dbEntitySameId;
    }
    
    public T setDbEntitySameId(String dbEntitySameId) {
        this.dbEntitySameId = dbEntitySameId;
        return (T)this;
    }
    
    public String getTestVarchar() {
        return this.testVarchar;
    }
    
    public T setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
        return (T)this;
    }
    
    public Integer getTestInt() {
        return this.testInt;
    }
    
    public T setTestInt(Integer testInt) {
        this.testInt = testInt;
        return (T)this;
    }
    
    public Boolean getTestBoolean() {
        return this.testBoolean;
    }
    
    public T setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
        return (T)this;
    }
    
    public DbEntitySameTestEnumEnum getTestEnum() {
        return this.testEnum;
    }
    
    public T setTestEnum(DbEntitySameTestEnumEnum testEnum) {
        this.testEnum = testEnum;
        return (T)this;
    }
    
    public DbEntitySameTestEnum2Enum getTestEnum2() {
        return this.testEnum2;
    }
    
    public T setTestEnum2(DbEntitySameTestEnum2Enum testEnum2) {
        this.testEnum2 = testEnum2;
        return (T)this;
    }
}
