package com.github.yt.mybatis.example.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.yt.mybatis.example.po.DbEntitySamePO;

import java.io.Serializable;

/**
 * PO 类的扩展类
 */
@Table(name = "DbEntitySame")
public class DbEntitySame extends DbEntitySamePO<DbEntitySame> implements Serializable {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解

    @Transient
    private Integer countNum;
    @Transient
    private DbEntityNotSame dbEntityNotSame;

    public DbEntityNotSame getDbEntityNotSame() {
        return dbEntityNotSame;
    }

    public DbEntitySame setDbEntityNotSame(DbEntityNotSame dbEntityNotSame) {
        this.dbEntityNotSame = dbEntityNotSame;
        return this;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public DbEntitySame setCountNum(Integer countNum) {
        this.countNum = countNum;
        return this;
    }
}
