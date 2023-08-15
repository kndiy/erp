package com.kndiy.erp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**",
                        "/styles/**",
                        "/images/**",
                        "/scripts/**")
                .addResourceLocations("classpath:/static/files/",
                        "classpath:/static/styles/",
                        "classpath:/static/images/",
                        "classpath:/static/scripts/")
                .resourceChain(true);
    }
}
