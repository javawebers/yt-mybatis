package com.github.yt.mybatis.generator;

import com.github.yt.mybatis.example.entity.BaseEntity;

public class ExampleCodeGeneratorExample {

    public static void main(String[] args) {
        createJavaCode();
    }

    private static void createJavaCode() {
        JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(
                "root",
                "root",
                "yt-mybatis",
                "jdbc:mysql://localhost:3306/yt-mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        javaCodeGenerator.create("mysql_example", "", "com.github.yt.mybatis.example",
                JavaCodeGenerator.CodePath.SRC_TEST,
                BaseEntity.class
                ,JavaCodeGenerator.TemplateEnum.PO
                ,JavaCodeGenerator.TemplateEnum.BEAN
                ,JavaCodeGenerator.TemplateEnum.MAPPER
//                , JavaCodeGenerator.TemplateEnum.MAPPER_XML
                ,JavaCodeGenerator.TemplateEnum.SERVICE
        );
    }
}
