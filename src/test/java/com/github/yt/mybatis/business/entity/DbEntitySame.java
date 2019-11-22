package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.entity.BaseEntity;

@Table(name = "DbEntitySame")
public class DbEntitySame extends BaseEntity<DbEntitySame>{

    /**
     * id
     */
    @javax.persistence.Id
    private String dbEntitySameId;
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
    /**
     * int类型
     */
    private Integer testInt;
    /**
     * String类型
     */
    private String testVarchar;

    public String getDbEntitySameId() {
        return this.dbEntitySameId;
    }

    public DbEntitySame setDbEntitySameId(String dbEntitySameId) {
        this.dbEntitySameId = dbEntitySameId;
        return this;
    }

    public Boolean getTestBoolean() {
        return this.testBoolean;
    }

    public DbEntitySame setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
        return this;
    }

    public DbEntitySameTestEnumEnum getTestEnum() {
        return this.testEnum;
    }

    public DbEntitySame setTestEnum(DbEntitySameTestEnumEnum testEnum) {
        this.testEnum = testEnum;
        return this;
    }

    public DbEntitySameTestEnum2Enum getTestEnum2() {
        return this.testEnum2;
    }

    public DbEntitySame setTestEnum2(DbEntitySameTestEnum2Enum testEnum2) {
        this.testEnum2 = testEnum2;
        return this;
    }

    public Integer getTestInt() {
        return this.testInt;
    }

    public DbEntitySame setTestInt(Integer testInt) {
        this.testInt = testInt;
        return this;
    }

    public String getTestVarchar() {
        return this.testVarchar;
    }

    public DbEntitySame setTestVarchar(String testVarchar) {
        this.testVarchar = testVarchar;
        return this;
    }
}
