package com.yolo.mrs.config;

import com.yolo.mrs.interceptor.LoginInterceptor;
import com.yolo.mrs.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.yolo.mrs.utils.Constant.FILE_PATH;

@Slf4j
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

