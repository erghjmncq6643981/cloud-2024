/*
 * chandler-elasticsearch-demo
 * 2024/6/24 15:28
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.elasticsearch.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/6/24 15:28
 * @version 1.0.0
 * @since 1.8
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 配置接口文档基本信息
                .info(this.getApiInfo())
                ;
    }

    private Info getApiInfo() {
        return new Info()
                // 配置文档标题
                .title("Elasticsearch Client Demo")
                // 配置文档描述
                .description("Elasticsearch交互测试")
                // 配置作者信息
                .contact(new Contact().name("chandler"))
                // 配置License许可证信息
                .license(new License().name("Apache 2.0"))
                // 概述信息
                .summary("SpringBoot3 Elasticsearch demo示例文档")
                .termsOfService("https://www.xiezhrspace.cn")
                // 配置版本号
                .version("2.0");
    }
}