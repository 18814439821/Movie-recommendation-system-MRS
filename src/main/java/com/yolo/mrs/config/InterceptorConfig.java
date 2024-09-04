package com.yolo.mrs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
@Configuration
public class InterceptorConfig extends WebMvcConfig {
    @Override
    public void addInterceptions(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("movies/detail/**")
                .excludePathPatterns("users/login/");
    };
}
