package com.github.yt.mybatis.util;

import com.github.yt.mybatis.example.entity.DbEntityNotSame;
import com.github.yt.mybatis.example.entity.DbEntitySame;
import com.github.yt.mybatis.entity.BaseEntity;
import com.github.yt.mybatis.entity.YtColumnType;
import org.junit.Test;
import org.testng.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityUtilsTest {

    private static class GetEntityClassEntity {
    }

    @Test
    public void getEntityClass_success() {
        List<GetEntityClassEntity> entityClassEntityList = new ArrayList<>();
        entityClassEntityList.add(new GetEntityClassEntity());
        Class<GetEntityClassEntity> classEntityClass = EntityUtils.getEntityClass(entityClassEntityList);
        Assert.assertEquals(classEntityClass, GetEntityClassEntity.class, "类型不相同");
    }

    @Test
    public void getEntityClass_emptyCollection() {
        List<GetEntityClassEntity> entityClassEntityList = new ArrayList<>();
        boolean runtimeException = false;
        try {
            Class<GetEntityClassEntity> classEntityClass = EntityUtils.getEntityClass(entityClassEntityList);
        } catch (RuntimeException e) {
            runtimeException = true;
        }
        Assert.assertTrue(runtimeException);
    }


    private static class IsInstance extends BaseEntity<IsInstance> {
    }

    @Test
    public void testIsInstance() {
        boolean in = BaseEntity.class.isAssignableFrom(IsInstance.class);
        Assert.assertTrue(in);
    }

    @Test
    public void testGetYtColumnField() {
        // 继承默认 BaseEntity 情况
        Field sameField = EntityUtils.getYtColumnField(DbEntitySame.class, YtColumnType.CREATE_TIME);
        Assert.assertNotNull(sameField);
        String sameFieldName = EntityUtils.getFieldColumnName(sameField);
        Assert.assertEquals("createTime", sameFieldName);

        // 继承自己实现 BaseEntity 情况
        Field notSameField = EntityUtils.getYtColumnField(DbEntityNotSame.class, YtColumnType.CREATE_TIME);
        Assert.assertNotNull(notSameField);
        String notSameFieldName = EntityUtils.getFieldColumnName(notSameField);
        Assert.assertEquals("create_time", notSameFieldName);

        // 字段不存在的情况
        Field noField = EntityUtils.getYtColumnField(DbEntityNotSame.class, YtColumnType.FOUNDER_NAME);
        Assert.assertNull(noField);
    }


}
