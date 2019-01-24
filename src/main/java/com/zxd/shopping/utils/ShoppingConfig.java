package com.zxd.shopping.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ShoppingConfig implements WebMvcConfigurer {

    @Bean
    public FilterInterceptor filterInterceptor(){
        return new FilterInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器

        InterceptorRegistration filterRegistry = registry.addInterceptor(filterInterceptor());
        // 拦截路径
        filterRegistry.addPathPatterns("/apis/**");
        // 排除路径
        filterRegistry.excludePathPatterns("/apis/login");
        // 排除资源请求
//        filterRegistry.excludePathPatterns("/css/login/*.css");
//        filterRegistry.excludePathPatterns("/js/login/**/*.js");
//        filterRegistry.excludePathPatterns("/image/login/*.png");
    }
}
