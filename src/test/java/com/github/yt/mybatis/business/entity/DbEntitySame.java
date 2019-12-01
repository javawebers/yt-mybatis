package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.entity.BaseEntity;
import com.github.yt.mybatis.business.po.DbEntitySamePO;

/**
* PO 类的扩展类
*/
@Table(name = "DbEntitySame")
public class DbEntitySame extends DbEntitySamePO<DbEntitySame> {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解

}
