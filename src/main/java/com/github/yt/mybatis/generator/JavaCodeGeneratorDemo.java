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

        javaCodeGenerator.create("Message",
                "message 中文描述",
                "message 模块",
                "test.yt.mybatis",
                "bean", "mapper", "service", "controller");
    }

    public static void main(String[] args) {
        JavaCodeGeneratorDemo.generate();
    }
}
