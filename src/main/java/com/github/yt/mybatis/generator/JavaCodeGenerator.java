package com.github.yt.mybatis.generator;


import com.github.yt.commons.util.YtArrayUtils;
import com.github.yt.commons.util.YtStringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 代码生成类
 * @author sheng
 */
public class JavaCodeGenerator {

    private String username;
    private String passWord;
    private String dbInstance;
    private String url;

    /**
     * 最后一位包名，默认如下
     */
    private String packageEntity = "entity";
    private String packagePo = "po";
    private String packageDao = "dao";
    private String packageService = "service";
    private String packageController = "controller";

    public JavaCodeGenerator setPackagePo(String packagePo) {
        this.packagePo = packagePo;
        return this;
    }

    public JavaCodeGenerator setPackageEntity(String packageEntity) {
        this.packageEntity = packageEntity;
        return this;
    }

    public JavaCodeGenerator setPackageDao(String packageDao) {
        this.packageDao = packageDao;
        return this;
    }

    public JavaCodeGenerator setPackageService(String packageService) {
        this.packageService = packageService;
        return this;
    }

    public JavaCodeGenerator setPackageController(String packageController) {
        this.packageController = packageController;
        return this;
    }

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
    public enum TemplateEnum {
        /**
         * po，对应数据库实体
         */
        PO,

        /**
         * 继承自po，可在其中扩展字段，mapper，service，controller使用此类
         */
        BEAN,
        /**
         * mybatis mapper
         */
        MAPPER,
        /**
         * mybatis mapper xml
         */
        MAPPER_XML,
        /**
         * service
         */
        SERVICE,
        /**
         * controller
         */
        CONTROLLER,
        HTML,
        ;
    }

    /**
     * 生成文件位置
     */
    public enum CodePath {
        /**
         * 源码 main 目录
         */
        SRC_MAIN("src" + File.separator + "main"),
        /**
         * 源码 test 目录
         */
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
        create(tableName, codeName, modulePackage, CodePath.SRC_MAIN, null, templateEnums);
    }

    public void create(String tableName, String modulePackage, TemplateEnum... templates) {
        create(tableName, null, modulePackage, CodePath.SRC_TEST, null, templates);
    }

    /**
     * 代码生成
     *
     * @param tableName       表名
     * @param tableDesc       表名对应的注释
     * @param modulePackage   生成在包下：com.fdcz.pro.system
     * @param codePath        代码生成的位置
     * @param baseEntityClass 指定继承的 BaseEntity
     * @param templates       生成哪些模板
     */
    public void create(String tableName, String tableDesc, String modulePackage,
                       CodePath codePath, Class<?> baseEntityClass,
                       TemplateEnum... templates) {
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
        String poClassName = className + "PO";
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
        String entityPath = File.separator + packageEntity + File.separator + className + ".java";
        String poPath = File.separator + packagePo + File.separator + poClassName + ".java";
        String mapperPath = File.separator + packageDao + File.separator + replaceSuffixClassName + "Mapper.java";
        String mapperXmlPath = File.separator + packageDao + File.separator + replaceSuffixClassName + "Mapper.xml";
        String servicePath = File.separator + packageService + File.separator + replaceSuffixClassName + "Service.java";
        String serviceImplPath = File.separator + packageService + File.separator + "impl" + File.separator + replaceSuffixClassName
                + "ServiceImpl.java";
        String controllerPath = File.separator + packageController + File.separator + replaceSuffixClassName + "Controller.java";
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
        context.put("poClassName", poClassName);
        context.put("lowerName", lowerName);
        context.put("codeName", tableDesc);
        context.put("tableName", tableName);
        context.put("modulePackage", modulePackage);
        context.put("moduleSimplePackage", moduleSimplePackage);
        context.put("replaceSuffixClassName", replaceSuffixClassName);
        context.put("replaceSuffixLowerName", replaceSuffixLowerName);

        context.put("packageEntity", packageEntity);
        context.put("packagePO", packagePo);
        context.put("packageDao", packageDao);
        context.put("packageService", packageService);
        context.put("packageController", packageController);

        if (baseEntityClass != null) {
            context.put("importBaseEntity", "import " + baseEntityClass.getName() + ";");
            context.put("extendsBaseEntity", "extends " + baseEntityClass.getSimpleName() + "<T>");
        } else {
            context.put("importBaseEntity", "");
            context.put("extendsBaseEntity", "");
        }
        List<ColumnData> columnDataList = null;

        /****************************** 生成bean字段 *********************************/
        try {
            columnDataList = createBean.getColumnDataList(tableName, baseEntityClass);
            String fieldList = createBean.getBeanFieldList(columnDataList);
            context.put("columnDataList", columnDataList);
            context.put("fieldList", fieldList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        /******************************* 生成sql语句 **********************************/


        // -------------------生成文件代码---------------------/
        String modulePakPath = modulePackage.replaceAll("\\.", "/");
        // 生成Model
        for (TemplateEnum templateEnum : templates) {
            switch (templateEnum) {
                case PO:
                    // 生成枚举
                    for (ColumnData columnData : columnDataList) {
                        if ("enum".equals(columnData.getDataType())) {
                            context.put("columnComment", columnData.getColumnComment());
                            context.put("enumClassName", columnData.getEnumClassName());
                            String enumPath = File.separator + packagePo + File.separator + columnData.getEnumClassName() + ".java";
                            context.put("enumColumnDataList", columnData.getEnumColumnDataList());
                            CommonPageParser.writerPage(context, "Enum.java.vm", rootPath + javaPath + modulePakPath, enumPath);
                        }
                    }
                    CommonPageParser.writerPage(context, "PO.java.vm", rootPath + javaPath + modulePakPath, poPath);
                    break;
                case BEAN:
                    CommonPageParser.writerPage(context, "Bean.java.vm", rootPath + javaPath + modulePakPath, entityPath);
                    break;
                case MAPPER:
                    CommonPageParser.writerPage(context, "Mapper.java.vm", rootPath + javaPath + modulePakPath, mapperPath);
                    break;
                case MAPPER_XML:
                    CommonPageParser.writerPage(context, "Mapper.xml.vm", rootPath + resourcePath + modulePakPath, mapperXmlPath);
                    break;
                case SERVICE:
                    CommonPageParser.writerPage(context, "Service.java.vm", rootPath + javaPath + modulePakPath, servicePath);
                    CommonPageParser.writerPage(context, "ServiceImpl.java.vm", rootPath + javaPath + modulePakPath, serviceImplPath);
                    break;
                case CONTROLLER:
                    CommonPageParser.writerPage(context, "Controller.java.vm", rootPath + javaPath + modulePakPath, controllerPath);
                    break;
                case HTML:
                    CommonPageParser.writerPage(context, "Html.html.vm", rootPath + webappPath + YtStringUtils.substringAfterLast(modulePackage, "."), htmlPath);
                    break;
                default:
                    break;
            }
        }
    }
}
