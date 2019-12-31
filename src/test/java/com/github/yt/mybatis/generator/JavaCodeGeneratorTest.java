package com.github.yt.mybatis.generator;

import com.github.yt.mybatis.example.entity.BusinessBaseEntity;
import com.github.yt.mybatis.entity.BaseEntity;

public class JavaCodeGeneratorTest {
    public static void main(String[] args) {
        JavaCodeGeneratorTest javaCodeGeneratorTest = new JavaCodeGeneratorTest();
        javaCodeGeneratorTest.before();
        javaCodeGeneratorTest.createSame();
//        javaCodeGeneratorTest.createIntId();
        javaCodeGeneratorTest.createNotSame();
    }

    JavaCodeGenerator javaCodeGenerator;

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

    public void createSame() {
        javaCodeGenerator.create("DbEntitySame",
                "数据库属性和bean字段名一致",
                "com.github.yt.mybatis.business",
                JavaCodeGenerator.CodePath.SRC_TEST,
                BaseEntity.class
//                ,JavaCodeGenerator.TemplateEnum.PO
//                ,JavaCodeGenerator.TemplateEnum.BEAN
                ,JavaCodeGenerator.TemplateEnum.MAPPER
//                , JavaCodeGenerator.TemplateEnum.MAPPER_XML
                ,JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }

    public void createIntId() {
        javaCodeGenerator.create("IntId",
                "int 主键",
                "com.github.yt.mybatis.business",
                JavaCodeGenerator.CodePath.SRC_TEST,
                BaseEntity.class
//                , JavaCodeGenerator.TemplateEnum.PO
//                , JavaCodeGenerator.TemplateEnum.BEAN
                ,JavaCodeGenerator.TemplateEnum.MAPPER
//                ,JavaCodeGenerator.TemplateEnum.MAPPER_XML
                ,JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }

    public void createNotSame() {
        javaCodeGenerator.create("db_entity_not_same",
                "db_entity_not_same 中文描述",
                "com.github.yt.mybatis.business",
                JavaCodeGenerator.CodePath.SRC_TEST,
                BusinessBaseEntity.class

                , JavaCodeGenerator.TemplateEnum.PO
//                , JavaCodeGenerator.TemplateEnum.BEAN
//                ,JavaCodeGenerator.TemplateEnum.MAPPER
//                ,JavaCodeGenerator.TemplateEnum.MAPPER_XML
//                ,JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }

}
