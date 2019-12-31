package com.github.yt.mybatis.example.po;

import javax.persistence.Table;
import javax.persistence.Column;

import com.github.yt.mybatis.example.entity.BusinessBaseEntity;

@Table(name = "db_entity_not_same")
public class DbEntityNotSamePO<T extends DbEntityNotSamePO<T>> extends BusinessBaseEntity<T> {

    /** 
     * id  
     */
    @javax.persistence.Id
    @Column(name = "db_entity_not_same_id")
    private String dbEntityNotSameId;
    /** 
     * boolean类型  
     */
    @Column(name = "test_boolean")
    private Boolean testBoolean;
    /** 
     * int类型  
     */
    @Column(name = "test_int")
    private Integer testInt;
    /** 
     * String类型  
     */
    @Column(name = "test_varchar")
    private String testVarchar;

    public String getDbEntityNotSameId() {
        return this.dbEntityNotSameId;
    }

    public T setDbEntityNotSameId(String dbEntityNotSameId) {
        this.dbEntityNotSameId = dbEntityNotSameId;
        return (T) this;
    }

    public Boolean getTestBoolean() {
        return this.testBoolean;
    }

    public T setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
        return (T) this;
    }

    public Integer getTestInt() {
        return this.testInt;
    }

    public T setTestInt(Integer testInt) {
        this.testInt = testInt;
        return (T) this;
    }

    public String getTestVarchar() {
        return this.testVarchar;
    }

    public T setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
        return (T) this;
    }
}
