package com.github.yt.mybatis.example.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.yt.mybatis.example.po.DbEntityNotSamePO;

/**
* PO 类的扩展类
*/
@Table(name = "db_entity_not_same")
public class DbEntityNotSame extends DbEntityNotSamePO<DbEntityNotSame> {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解
    @Transient
    private Integer countNum;

    public Integer getCountNum() {
        return countNum;
    }

    public DbEntityNotSame setCountNum(Integer countNum) {
        this.countNum = countNum;
        return this;
    }
}
