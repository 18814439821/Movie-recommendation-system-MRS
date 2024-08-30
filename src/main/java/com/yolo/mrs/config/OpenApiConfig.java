package com.yolo.mrs.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @description: knife4j配置类
 * @authod:  yolo
 * @date:    2024/8/29
 **/
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("API接口文档")
                        // 接口文档简介
                        .description("这是基于Knife4j OpenApi3的接口文档")
                        // 接口文档版本
                        .version("v1.0")
                        // 开发者联系方式
                        .contact(new Contact().name("yolo").email("1494997312@qq.com")));
                    // 外部文档
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringBoot基础框架")
//                        .url("http://127.0.0.1:8088"));
    }

    /**以下分组和资源映射都可省略*/
//    @Bean
//    public GroupedOpenApi loginApi() {
//        return GroupedOpenApi.builder().group("login登录模块")
//                .pathsToMatch("/login/**")
//                .build();
//    }
}