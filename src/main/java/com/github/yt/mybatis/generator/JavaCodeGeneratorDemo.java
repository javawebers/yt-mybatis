package com.github.yt.mybatis.generator;

/**
 * 生成示例
 * @author liujiasheng
 */
public class JavaCodeGeneratorDemo {

    public static void generate() {
        JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(
                "root",
                "root",
                "yt-mybatis",
                "jdbc:mysql://localhost:3306/yt-mybatis?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");

        javaCodeGenerator.create("DbEntitySame",
                "test.yt.mybatis",
                JavaCodeGenerator.TemplateEnum.BEAN,
                JavaCodeGenerator.TemplateEnum.MAPPER,
                JavaCodeGenerator.TemplateEnum.SERVICE,
                JavaCodeGenerator.TemplateEnum.CONTROLLER
        );
    }

    public static void main(String[] args) {
        JavaCodeGeneratorDemo.generate();
    }
}
