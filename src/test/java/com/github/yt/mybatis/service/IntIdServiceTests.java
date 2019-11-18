package com.github.yt.mybatis.service;

import com.github.yt.mybatis.YtMybatisDemoApplication;
import com.github.yt.mybatis.business.entity.IntId;
import com.github.yt.mybatis.business.service.IntIdService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {YtMybatisDemoApplication.class})
public class IntIdServiceTests extends AbstractTestNGSpringContextTests {

    @Resource
    IntIdService intIdService;

    @Test
    public void save_intId() {
        IntId entity = new IntId();
        intIdService.save(entity);
        System.out.println(entity.getIntId());
        System.out.println(entity.getTestVarchar());
    }
    @Test
    public void saveBatch_intId() {
        IntId entity1 = new IntId().setTestVarchar("222");
        IntId entity2 = new IntId().setTestVarchar("233");
        List<IntId> intIdList = Arrays.asList(entity1, entity2);
        intIdService.saveBatch(intIdList);
        System.out.println(entity1.getIntId());
        System.out.println(entity1.getTestVarchar());
        System.out.println(entity2.getIntId());
        System.out.println(entity2.getTestVarchar());
    }
}
