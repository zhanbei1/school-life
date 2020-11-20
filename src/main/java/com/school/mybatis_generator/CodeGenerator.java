package com.school.mybatis_generator;/**
 * Created by zhanbei on 2020/8/27.
 */

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @ClassName CodeGenerator
 * @Description mybatis-plus代码逆向生成
 * @Author zhanbei
 * @Date 2020/8/27 0:17
 * @Version 1.0
 **/
public class CodeGenerator {

    private static String dataUrl = "jdbc:mysql//60.205.184.182:3306/schoolLife";
    private static String userName = "root";
    private static String password = "123456";
    /**
     *@Author zhanbei
     *@Description 从控制台读取信息
     *@Date 18:56 2020/8/27
     *@Param []
     *@Return java.lang.String
    **/
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入"+tip+":");
        System.out.println(help.toString());
        if(scanner.hasNext()){
            String ipt = scanner.next();
            if(StringUtils.isNotEmpty(ipt)){
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的"+tip+"!");
    }

    public static void main(String[] args) {
        //1、代码生成器
        AutoGenerator generator = new AutoGenerator();

        //2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("kite");
        gc.setOpen(false);
        //gc.setSwagger2(true);实体类试着swagger2注解
        generator.setGlobalConfig(gc);//设置代码生成器的全局变量

        //3、数据库连接配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(dataUrl);
        dataSourceConfig.setUsername(userName);
        dataSourceConfig.setPassword(password);
        generator.setDataSource(dataSourceConfig);

        //4、生成代码包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.baomidou.ant");
        packageConfig.setModuleName(scanner("模块名"));
        packageConfig.setController("com.school.controller");
        packageConfig.setService("com.school.service");
        packageConfig.setServiceImpl("com.school.service");
        packageConfig.setEntity("com.school.entity");
        packageConfig.setMapper("mapper");
        generator.setPackageInfo(packageConfig);

        //5、自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };
        //6、模板引擎配置
        String templatePath = "/templates/mapper.xml.ftl";

        //7、自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig() {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        //8、配置模板
        //TemplateConfig templateConfig = new TemplateConfig();

        //9、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }
}