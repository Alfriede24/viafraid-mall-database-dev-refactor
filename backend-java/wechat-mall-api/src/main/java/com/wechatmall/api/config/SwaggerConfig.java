package com.wechatmall.api.config;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: swagger配置类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 20:40
 */
@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(description = "开发环境",url = "http://localhost:8080"),
                @Server(description = "测试环境",url = "http://localhost:8080")
        },
        externalDocs = @ExternalDocumentation(
                description = "项目部署",
                url = "http://localhost:8080/readme.md"
        )
)
public class SwaggerConfig {
    private static final String TOKEN_HEADER = "Authorization";
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info())
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(TOKEN_HEADER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(TOKEN_HEADER));
    }

    private Info info() {
        return new Info()
                .title("商城系统接口")
                .version("1.0")
                .description("商城系统接口接口文档")
                .contact(new Contact().name("zpc").url("https://www.baidu.com").email("Zhupc1024@163.com"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                .termsOfService("https://www.baidu.com")
                .summary("商城系统接口接口文档");
    }

   @Bean
   public GroupedOpenApi authApi(){
       return GroupedOpenApi.builder()
               .group("认证接口")
               .pathsToMatch("/auth/**")
               .packagesToScan("com.wechatmall.api.controller.auth")
               .build();
   }

    @Bean
    public GroupedOpenApi commonApi(){
        return GroupedOpenApi.builder()
                .group("公共接口")
                .pathsToMatch("/common/**")
                .packagesToScan("com.wechatmall.api.controller.common")
                .build();
    }
   
   @Bean
   public GroupedOpenApi merchantApi(){
       return GroupedOpenApi.builder()
               .group("商家后台接口")
               .pathsToMatch("/merchant/**")
               .packagesToScan("com.wechatmall.api.controller.merchant")
               .build();
   }

    @Bean
    public GroupedOpenApi userApi(){
        return GroupedOpenApi.builder()
                .group("用户相关接口")
                .pathsToMatch("/user/**")
                .packagesToScan("com.wechatmall.api.controller.user")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi(){
        return GroupedOpenApi.builder()
                .group("管理员相关接口")
                .pathsToMatch("/admin/**")
                .packagesToScan("com.wechatmall.api.controller.admin")
                .build();
    }

    @Bean
    public GroupedOpenApi clientApi(){
        return GroupedOpenApi.builder()
                .group("客户端相关接口")
                .pathsToMatch("/client/**")
                .packagesToScan("com.wechatmall.api.controller.client")
                .build();
    }

    @Bean
    public GroupedOpenApi messageApi(){
        return GroupedOpenApi.builder()
                .group("消息管理相关接口")
                .pathsToMatch("/message/**")
                .packagesToScan("com.wechatmall.api.controller.message")
                .build();
    }

    @Bean
    public GlobalOperationCustomizer globalOperationCustomizer() {
        return (operation, handlerMethod) -> {
            // 添加全局参数
            operation.addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter()
                    .in("header")
                    .name("Authorization")
                    .required(true)
                    .description("JWT Token")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()));

            // 添加安全要求
            operation.addSecurityItem(new SecurityRequirement().addList(TOKEN_HEADER));

            return operation;
        };
    }

    @Bean
    public OpenApiCustomiser securityOpenApiCustomiser() {
        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement().addList(TOKEN_HEADER))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(TOKEN_HEADER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
