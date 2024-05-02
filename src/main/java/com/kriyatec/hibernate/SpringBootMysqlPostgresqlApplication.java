package com.kriyatec.hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SpringBootMysqlPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMysqlPostgresqlApplication.class, args);
    }
}
