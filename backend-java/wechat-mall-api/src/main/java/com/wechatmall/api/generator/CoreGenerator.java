package com.wechatmall.api.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Description: 代码生成器 <br>
 * WebSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * Copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * Program Name: CoreGenerator <br>
 * Date: 2024-12-28  下午 01:48 <br>
 *
 * @author PengCai Zhu Zhupc1024@163.com
 * @version 1.0
 */
public class CoreGenerator {
    public static void main(String[] args) {

        FastAutoGenerator.create(getDataSourceConfig()
                                 .schema("wechat_mall")
                                 .keyWordsHandler(new MySqlKeyWordsHandler())
                )
                // 全局配置
                .globalConfig((scanner, builder) ->
                        builder.author(scanner.apply("请输入作者名称？"))
                                .disableOpenDir()
                                .outputDir("/Users/zhupengcai/Desktop/small/viafraid-mall-database/backend-java/wechat-mall-api/src/main/java/")
                                .dateType(DateType.TIME_PACK)
                                .commentDate("yyyy-MM-dd HH:mm:ss")
                                .enableSwagger()
                )
                // 包配置
                .packageConfig((scanner, builder) ->
                        // builder.parent(scanner.apply("请输入包名？"))
                        builder.parent("com.wechatmall")
                                .moduleName("api")
                                .entity("pojo.entity")
                                .mapper("dao")

                )
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .entityBuilder()
                        .enableLombok()
                        .javaTemplate("/templates/modern-entity.java")
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        )
                        .controllerBuilder()
                        .template("/templates/modern-controller.java")
                        .build())
                .templateEngine(new FreemarkerTemplateEngine())
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    protected  static  DataSourceConfig.Builder getDataSourceConfig(){
        Properties prop = new Properties();
        try {
            InputStream in = CoreGenerator.class.getClassLoader().getResourceAsStream("datasource.properties");
            prop.load(in);
            String url = prop.getProperty("datasource.url");
            String username = prop.getProperty("datasource.username");
            String password = prop.getProperty("datasource.password");
            return new DataSourceConfig.Builder(url, username, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
