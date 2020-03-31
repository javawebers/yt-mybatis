package com.github.yt.mybatis;

/**
 * 异常枚举
 * @author liujiasheng
 */
public enum YtMybatisExceptionEnum {

    //
    CODE_21("保存的数据已经存在", "数据库异常，记录已存在"),
    CODE_22("系统异常，请联系管理员", "数据库异常，数据完整性异常(字段不为空，数据长度限制等)"),
    CODE_23("系统异常，请联系管理员", "数据库异常，脚本语法异常(字段不存在等)"),
    CODE_24("数据不存在", "数据库异常，记录数不正确(findOne getOne 等)"),

    // error
    CODE_76("删除的记录数过多"),
    CODE_77("逻辑删除的记录数过多"),
    CODE_78("删除标志位字段不存在"),
    CODE_79("实例化resultConfig对象异常，{0}"),
    CODE_80("不是根据id比较，不可调用map差异"),
    CODE_81("实体未配置Table注解 entityClass = {0}"),
    CODE_82("实体的Table注解未配置name属性 entityClass = {0}"),
    CODE_83("{0}实体未配置Id"),
    CODE_84("{0},删除时主键不能为空!"),
    CODE_85("service中没有覆盖getMapper方法也没有mapper字段"),
    CODE_86("批量插入的数据为null"),
    CODE_87("设置id异常"),
    CODE_88("{0},get单个对象时主键不能为空!"),
    CODE_89("创建where语句异常"),
    CODE_90("Object to Map convert Error"),
    CODE_91("Id field is not found in[{0}]{1}"),
    CODE_92("parameter is null"),
    CODE_93("class does not have this field :{0}"),
    CODE_94("获取对象属性异常"),
    CODE_95("{0}不是集合属性"),
    CODE_96("设置集合异常"),
    CODE_97("设置id值异常"),

    CODE_98("当前记录不存在"),
    CODE_99("系统异常"),
    ;

    private String message;
    private String description;

    YtMybatisExceptionEnum(String message) {
        this.message = message;
    }

    YtMybatisExceptionEnum(String message, String description) {
        this.message = message;
        this.description = description;
    }
}
