package com.github.yt.mybatis.example.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;

import com.github.yt.mybatis.example.entity.BaseEntity;
import com.github.yt.mybatis.example.po.MysqlExamplePO;

/**
 * PO 类的扩展类
 */
@Table(name = "mysql_example")
public class MysqlExample extends MysqlExamplePO<MysqlExample> {
    // 扩展字段，一对一对象等。字段上加 @Transient 注解
    @Transient
    private int countNum;

    public int getCountNum() {
        return countNum;
    }

    public MysqlExample setCountNum(int countNum) {
        this.countNum = countNum;
        return this;
    }
}
