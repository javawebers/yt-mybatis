package com.github.yt.mybatis.generator;


import com.github.yt.commons.util.YtArrayUtils;
import com.github.yt.commons.util.YtStringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JavaCodeGenerator {

    private String username;
    private String passWord;
    private String dbInstance;
    private String url;

    public JavaCodeGenerator(String username, String passWord, String dbInstance, String url) {
        this.username = username;
        this.passWord = passWord;
        this.dbInstance = dbInstance;
        this.url = url;
    }

    private static List<String> CLASS_NAME_SUFFIX_LIST = new ArrayList<>();

    static {
        CLASS_NAME_SUFFIX_LIST.add("T");
        CLASS_NAME_SUFFIX_LIST.add("R");
    }

    public String getReplaceSuffixClassName(String className) {
        if (className == null || className.length() < 1) {
            return className;
        }
        String suffix = className.substring(className.length() - 1, className.length());
        if (CLASS_NAME_SUFFIX_LIST.contains(suffix)) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }

    /**
     * 模板枚举
     */
    enum TemplateEnum {
        BEAN, MAPPER, MAPPER_XML, SERVICE, CONTROLLER, HTML;
    }

    /**
     * 生成文件位置
     */
    enum CodePath {
        SRC_MAIN("src" + File.separator + "main"),
        SRC_TEST("src" + File.separator + "test");
        String path;

        CodePath(String path) {
            this.path = path;
        }
    }

    /**
     * @param tableName     表名
     * @param codeName      表名对应的中文注释
     * @param modulePackage 模块包：com.fdcz.pro.system
     * @param moduleName    模块名
     * @param templates     生成那些模板
     */
    @Deprecated
    public void create(String tableName, String codeName, String moduleName, String modulePackage, String... templates) {
        if (YtArrayUtils.isEmpty(templates)) {
            return;
        }
        TemplateEnum[] templateEnums = new TemplateEnum[templates.length];
        for (int i = 0; i < templates.length; i++) {
            templateEnums[i] = TemplateEnum.valueOf(templates[i].toUpperCase());
        }
        create(tableName, codeName, modulePackage, CodePath.SRC_MAIN, templateEnums);
    }

    public void create(String tableName, String modulePackage, TemplateEnum... templates) {
        create(tableName, null, modulePackage, CodePath.SRC_TEST, templates);
    }

    /**
     * 代码生成参数
     *
     * @param tableName     表名
     * @param tableDesc     表名对应的注释
     * @param modulePackage 生成在包下：com.fdcz.pro.system
     * @param codePath      代码生成的位置
     * @param templates     生成哪些模板
     */
    public void create(String tableName, String tableDesc, String modulePackage, CodePath codePath, TemplateEnum... templates) {
        if (YtStringUtils.isBlank(tableName)) {
            return;
        }
        if (YtStringUtils.isBlank(modulePackage)) {
            return;
        }
        if (codePath == null) {
            return;
        }
        if (YtArrayUtils.isEmpty(templates)) {
            return;
        }

        CreateBean createBean = new CreateBean();
        createBean.setMysqlInfo(url, username, passWord, dbInstance);
        String className = createBean.getTablesNameToClassName(tableName);
        String replaceSuffixClassName = getReplaceSuffixClassName(className);
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
        String replaceSuffixLowerName = replaceSuffixClassName.substring(0, 1).toLowerCase() + replaceSuffixClassName.substring(1, replaceSuffixClassName.length());
        String rootPath = CommonPageParser.getRootPath();
        String resourcePath = File.separator + codePath.path + File.separator
                + "resources" + File.separator;
        String javaPath = File.separator + codePath.path + File.separator + "java"
                + File.separator;
        String webappPath = File.separator + codePath.path + File.separator + "webapp"
                + File.separator + "module" + File.separator;
        String moduleSimplePackage = modulePackage
                .substring(modulePackage.lastIndexOf(".") + 1, modulePackage.length());

        // java,xml文件名称
        String modelPath = File.separator + "entity" + File.separator + className + ".java";
        String mapperPath = File.separator + "dao" + File.separator + replaceSuffixClassName + "Mapper.java";
        String mapperXmlPath = File.separator + "dao" + File.separator + replaceSuffixClassName + "Mapper.xml";
        String servicePath = File.separator + "service" + File.separator + replaceSuffixClassName + "Service.java";
        String serviceImplPath = File.separator + "service" + File.separator + "impl" + File.separator + replaceSuffixClassName
                + "ServiceImpl.java";
        String controllerPath = File.separator + "controller" + File.separator + replaceSuffixClassName + "Controller.java";
        String htmlPath = File.separator + replaceSuffixLowerName + ".html";

        try {
            if (YtStringUtils.isBlank(tableDesc)) {
                tableDesc = createBean.getTableComment(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("codeName", tableDesc);
        context.put("tableName", tableName);
        context.put("modulePackage", modulePackage);
        context.put("moduleSimplePackage", moduleSimplePackage);
        context.put("replaceSuffixClassName", replaceSuffixClassName);
        context.put("replaceSuffixLowerName", replaceSuffixLowerName);
        /****************************** 生成bean字段 *********************************/
        try {
            context.put("feilds", createBean.getBeanFeilds(tableName)); // 生成bean
        } catch (Exception e) {
            e.printStackTrace();
        }

        /******************************* 生成sql语句 **********************************/
        try {
            Map<String, Object> sqlMap = createBean.getAutoCreateSql(tableName);
            List<ColumnData> columnDatas = createBean.getColumnDatas(tableName);
            List<ColumnData> normalColumns = new ArrayList<>();
            ColumnData columnDataPriKey = new ColumnData();
            for (ColumnData columnData : columnDatas) {
                if (columnData.getIsPriKey()) {
                    columnDataPriKey = columnData;
                    continue;
                }
                normalColumns.add(columnData);
            }
            context.put("columnDatas", columnDatas); // 生成bean
            context.put("prikey", columnDataPriKey.getColumnName()); // 生成主见
            context.put("normalColumns", normalColumns); // 生成非主键列表
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // -------------------生成文件代码---------------------/
        String modulePakPath = modulePackage.replaceAll("\\.", "/");
        // 生成Model
        for (TemplateEnum templateEnum : templates) {
            switch (templateEnum) {
                case BEAN:
                    CommonPageParser.writerPage(context, "Bean.java.vm", rootPath + javaPath + modulePakPath, modelPath); //
                    break;
                case MAPPER:
                    CommonPageParser.writerPage(context, "Mapper.java.vm", rootPath + javaPath + modulePakPath, mapperPath); // 生成MybatisMapper接口
                    break;
                case MAPPER_XML:
                    CommonPageParser.writerPage(context, "Mapper.xml.vm", rootPath + resourcePath + modulePakPath, mapperXmlPath); //
                    break;
                case SERVICE:
                    CommonPageParser.writerPage(context, "Service.java.vm", rootPath + javaPath + modulePakPath, servicePath);// 生成Service
                    CommonPageParser.writerPage(context, "ServiceImpl.java.vm", rootPath + javaPath + modulePakPath, serviceImplPath);// 生成Service
                    break;
                case CONTROLLER:
                    CommonPageParser.writerPage(context, "Controller.java.vm", rootPath + javaPath + modulePakPath, controllerPath);// 生成Controller
                    break;
                case HTML:
                    CommonPageParser.writerPage(context, "Html.html.vm", rootPath + webappPath + YtStringUtils.substringAfterLast(modulePackage, "."), htmlPath);//
                    break;
                default:
                    break;
            }
        }
    }
}
