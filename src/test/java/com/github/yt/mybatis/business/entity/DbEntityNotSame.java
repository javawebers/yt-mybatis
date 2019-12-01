package com.github.yt.mybatis.business.entity;

import javax.persistence.Table;
import javax.persistence.Column;
import com.github.yt.mybatis.business.entity.BusinessBaseEntity;
import com.github.yt.mybatis.business.po.DbEntityNotSamePO;

/**
* PO 类的扩展类
*/
@Table(name = "db_entity_not_same")
public class DbEntityNotSame extends DbEntityNotSamePO<DbEntityNotSame> {

    // 扩展字段，一对一对象等。字段上加 @Transient 注解

}
