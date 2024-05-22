package com.example.adminservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.adminservice.ServiceImpl.CustomRequestLoggingFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public CustomRequestLoggingFilter customRequestLoggingFilter() {
        return new CustomRequestLoggingFilter();
    }
}
