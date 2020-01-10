package com.accompany.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Method;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${server.domain}")
    private String domain;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(domain)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accompany.order.controller"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Method.class)
                //.ignoredParameterTypes(Pageable.class)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("熊熊的早点店 API 文档")
                .description("")
                .termsOfServiceUrl("XX")
                .build();
    }
}
