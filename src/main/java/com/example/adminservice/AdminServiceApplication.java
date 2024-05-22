package com.example.adminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.example.adminservice.ServiceImpl.CustomRequestLoggingFilter;

@SpringBootApplication
//(exclude = {DataSourceAutoConfiguration.class })
public class AdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}
	  @Bean
	    public CustomRequestLoggingFilter customRequestLoggingFilter() {
	        return new CustomRequestLoggingFilter();
	    }
}
