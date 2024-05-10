package com.kriyatec.dynamic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.annotation.Validated; 

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

@ComponentScan(basePackages = {"com.kriyatec.dynamic.controller","com.kriyatec.dynamic.model.LoginResponse","com.kriyatec.dynamic.repository","com.kriyatec.dynamic.service"})//, 
@Validated
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
//(exclude = {DataSourceAutoConfiguration.class })