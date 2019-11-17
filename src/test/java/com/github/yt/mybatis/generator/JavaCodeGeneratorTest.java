package com.github.yt.mybatis.generator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JavaCodeGeneratorTest {
    JavaCodeGenerator javaCodeGenerator;

    @BeforeClass
    public void before() {
        javaCodeGenerator = new JavaCodeGenerator(
                "root",
                "root",
                "yt-mybatis",
                "jdbc:mysql://localhost:3306/yt-mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
    }

//    @Test
//    public void createOld() {
//        javaCodeGenerator.create("DbEntitySame",
//                "DbEntitySame 中文描述",
//                "DbEntitySame 模块",
//                "test.yt.mybatis",
//                "bean", "mapper", "service");
//    }

    @Test
    public void createNew1() {
        javaCodeGenerator.create("db_entity_not_same",
                "DbEntitySame 中文描述",
                "test.yt.mybatis",
                JavaCodeGenerator.CodePath.SRC_TEST,
                JavaCodeGenerator.TemplateEnum.BEAN,
                JavaCodeGenerator.TemplateEnum.MAPPER,
                JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }

    @Test
    public void createNew2() {
        javaCodeGenerator.create("IntId",
                "test.yt.mybatis",
                JavaCodeGenerator.TemplateEnum.BEAN,
                JavaCodeGenerator.TemplateEnum.MAPPER,
                JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }
}
