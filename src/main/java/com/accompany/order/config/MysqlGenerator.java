package com.accompany.order.config;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Scanner;

/**
 * Author:Accompany
 * Date:2019/12/20
 */
public class MysqlGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        String parentPackage = "com.accompany.order";
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("Accompany"); //作者信息
        gc.setOpen(false); //打开文件
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setDateType(DateType.ONLY_DATE); //设置为Date类型
        gc.setFileOverride(true); //是否覆盖文件
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://120.76.56.183:33306/order?useUnicode=true&characterEncoding=UTF-8&jdbcCompliantTruncation=false")
            .setDriverName("com.mysql.cj.jdbc.Driver")
            .setUsername("root")
            .setPassword("root")
            .setSchemaName("public")
            .setTypeConvert(new MySqlTypeConvert() {
                @Override
                public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                    if (fieldType.toLowerCase().contains("tinyint")) {
                        return DbColumnType.BOOLEAN;
                    }
                    return super.processTypeConvert(globalConfig, fieldType);
                }
            });
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);
        //pc.setModuleName(scanner("模块名"));
        String moduleName = scanner("模块名");
        pc.setController("controller." + moduleName);
        pc.setEntity("service." + moduleName + ".dto");
        pc.setMapper("service." + moduleName + ".dao");
        pc.setXml("service." + moduleName + ".dao");
        pc.setService("service." + moduleName + ".dao");
        pc.setServiceImpl("service." + moduleName);
        mpg.setPackageInfo(pc);

       /* // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                    + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);*/

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        //不生成对应的代码
        templateConfig.setService("");
        templateConfig.setController("");
        templateConfig.setServiceImpl("");
//        templateConfig.setXml("");
//        templateConfig.setEntity("");
//        templateConfig.setMapper("");
//        templateConfig.setEntityKt("");

        mpg.setTemplate(templateConfig);

        // 策略配置 数据库表的配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true)
            .setNaming(NamingStrategy.underline_to_camel)
            .setColumnNaming(NamingStrategy.underline_to_camel)
            .setEntityLombokModel(true)
            .setEntityTableFieldAnnotationEnable(true)
            //.setLogicDeleteFieldName("is_del")//添加了is_del那么在查询的时候就会忽略
            .setRestControllerStyle(false)
            .setControllerMappingHyphenStyle(true)
            //.setTablePrefix(pc.getModuleName() + "_")
            .setInclude(scanner("表名，多个英文逗号分割").split(","));
        mpg.setStrategy(strategy);
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
