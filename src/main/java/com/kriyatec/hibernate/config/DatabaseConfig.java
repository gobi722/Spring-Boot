package com.kriyatec.hibernate.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseConfig {
private final Map<String, DataSource> connectionMap = new HashMap<>();
	@Transactional
    public DataSource getConnection(String dbName,String Username, String Password, String Url, String DriverClassName) throws SQLException {
        if (connectionMap.containsKey(dbName)) {
        	DataSource ds ;
             ds=connectionMap.get(dbName);
           try (Connection connection = ds.getConnection()) {
              		  System.out.println("Already connected");
                      return connectionMap.get(dbName);   	 
            }
        } else {
            // Create a new connection
        	 DataSource ds=createDataSource(Username, Password, Url, DriverClassName);
            connectionMap.put(dbName, ds);
            System.out.println("Newly connected");
            return ds;
        }
    }
	 public DataSource createDataSource(String Username, String Password, String Url, String DriverClassName) {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        try {
	            dataSource.setDriverClassName(DriverClassName);
	            dataSource.setUrl(Url);
	            dataSource.setUsername(Username);
	            dataSource.setPassword(Password);
	        } catch (Exception e) {
	            // Log or handle the exception
	            System.err.println("Error configuring MySQL data source: " + e.getMessage());
	        }
	        return dataSource;
		
	    }
	
	
}
