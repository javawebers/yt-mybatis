package com.github.yt.mybatis.util;

import com.github.yt.mybatis.entity.BaseEntity;
import org.junit.Test;
import org.testng.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityUtilsTest {

    private static class GetEntityClassEntity {
    }

    @Test
    public void getEntityClass_success(){
        List<GetEntityClassEntity> entityClassEntityList = new ArrayList<>();
        entityClassEntityList.add(new GetEntityClassEntity());
        Class<GetEntityClassEntity> classEntityClass = EntityUtils.getEntityClass(entityClassEntityList);
        Assert.assertEquals(classEntityClass, GetEntityClassEntity.class, "类型不相同");
    }

    @Test
    public void getEntityClass_emptyCollection(){
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

}
