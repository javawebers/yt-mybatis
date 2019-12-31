package com.github.yt.mybatis.example.po;

import javax.persistence.Table;
import javax.persistence.Column;

import com.github.yt.mybatis.example.entity.BaseEntity;

@Table(name = "mysql_example")
public class MysqlExamplePO<T extends MysqlExamplePO<T>> extends BaseEntity<T> {

    /** 
     * id  
     */
    @javax.persistence.Id
    @Column(name = "example_id")
    private String exampleId;
    /** 
     * String类型  
     */
    @Column(name = "test_varchar")
    private String testVarchar;
    /** 
     * int类型  
     */
    @Column(name = "test_int")
    private Integer testInt;
    /** 
     * boolean类型  
     */
    @Column(name = "test_boolean")
    private Boolean testBoolean;
    /** 
     * enum 类型，MALE:男 ，FEMALE:女，OTHER:其他  
     */
    @Column(name = "test_Enum")
    private MysqlExampleTestEnumEnum testEnum;

    public String getExampleId() {
        return this.exampleId;
    }

    public T setExampleId(String exampleId) {
        this.exampleId = exampleId;
        return (T) this;
    }

    public String getTestVarchar() {
        return this.testVarchar;
    }

    public T setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
        return (T) this;
    }

    public Integer getTestInt() {
        return this.testInt;
    }

    public T setTestInt(Integer testInt) {
        this.testInt = testInt;
        return (T) this;
    }

    public Boolean getTestBoolean() {
        return this.testBoolean;
    }

    public T setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
        return (T) this;
    }

    public MysqlExampleTestEnumEnum getTestEnum() {
        return this.testEnum;
    }

    public T setTestEnum(MysqlExampleTestEnumEnum testEnum) {
        this.testEnum = testEnum;
        return (T) this;
    }
}
