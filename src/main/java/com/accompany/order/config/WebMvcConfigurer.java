package com.accompany.order.config;

/**
 * Created by wuzhonggui on 2018/4/9.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

/**
 * Spring MVC 配置
 * @author zhongyuming
 */
@Configuration
@EnableConfigurationProperties
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Value("${cors.max-age}")
    private Long corsMaxAge;

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> corsAllowedOrigins;

    @Bean
    public SecurityInterceptor securityInterceptor(){
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor())
                .addPathPatterns("/**")
            /**
             * 这些都是swagger相关的url
             * */
            .excludePathPatterns("/webjars/**")
            .excludePathPatterns("/error")
            .excludePathPatterns("/swagger**")
            .excludePathPatterns("/swagger-resources/**")
            .excludePathPatterns("/")
            .excludePathPatterns("/login/**")
            .excludePathPatterns("/csrf")
        /***/
        ;
    }

    /**
     * 跨域设置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsAllowedOrigins);
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(corsMaxAge);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    @Bean("gson")
    public Gson gson(){
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}
