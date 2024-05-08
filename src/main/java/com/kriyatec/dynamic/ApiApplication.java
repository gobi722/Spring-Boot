package com.kriyatec.dynamic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories; 

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

@ComponentScan(basePackages = {"com.kriyatec.dynamic.controller","com.kriyatec.dynamic.model","com.kriyatec.dynamic.repository","com.kriyatec.dynamic.service"})//, 
//@EnableMongoRepositories(basePackages = "com.kriyatec.dynamic.repository")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
//(exclude = {DataSourceAutoConfiguration.class })