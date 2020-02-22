package com.hezepeng.annotationserver.conf;

import com.hezepeng.annotationserver.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/22 21:49
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //静态资源； *.css,*.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(loginInterceptor)
                        .addPathPatterns("/api/**")
                        .excludePathPatterns("/api/user/login");
            }
        };
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("X-Token", "Content-Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With")
                .allowCredentials(true);
    }
}
