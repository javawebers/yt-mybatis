package com.github.yt.mybatis.example.entity;

import javax.persistence.Table;

import com.github.yt.mybatis.example.po.IntIdPO;

/**
* PO 类的扩展类
*/
@Table(name = "IntId")
public class IntId extends IntIdPO<IntId> {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解

}
