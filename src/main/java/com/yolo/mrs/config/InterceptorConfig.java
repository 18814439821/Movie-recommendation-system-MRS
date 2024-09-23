package com.yolo.mrs.config;

import com.yolo.mrs.interceptor.LoginInterceptor;
import com.yolo.mrs.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    RefreshTokenInterceptor refreshTokenInterceptor;
    @Resource
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenInterceptor).order(0);
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(
                        "/movies/detail/**"
                ).excludePathPatterns("/**");
    }
}
