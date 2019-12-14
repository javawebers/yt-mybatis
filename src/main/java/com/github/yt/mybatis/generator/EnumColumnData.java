package com.github.yt.mybatis.generator;

/**
 * @author sheng
 */
public class EnumColumnData {
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public EnumColumnData setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public EnumColumnData setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
