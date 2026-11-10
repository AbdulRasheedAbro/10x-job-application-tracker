package com.abdulrasheed.jobtracker.config;

import com.abdulrasheed.jobtracker.security.TokenAuthenticationFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenAuthenticationFilter filter;
    public WebConfig(TokenAuthenticationFilter filter){this.filter=filter;}
    @Bean public org.springframework.boot.web.servlet.FilterRegistrationBean<TokenAuthenticationFilter> tokenFilter(){
        var bean=new org.springframework.boot.web.servlet.FilterRegistrationBean<TokenAuthenticationFilter>();
        bean.setFilter(filter); bean.addUrlPatterns("/api/*"); bean.setOrder(1); return bean;
    }
}
