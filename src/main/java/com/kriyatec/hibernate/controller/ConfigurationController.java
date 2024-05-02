package com.kriyatec.hibernate.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.kriyatec.hibernate.config.DatabaseConfig;
import com.kriyatec.hibernate.entities.Transaction;
import com.kriyatec.hibernate.entities.configuration;
import com.kriyatec.hibernate.services.ConfigurationService;

@RestController
public class ConfigurationController {
	@Autowired
    private ConfigurationService configService;
    private DatabaseConfig dataSourceService;
   
    ConfigurationController(DatabaseConfig dataSourceService){
    	this.dataSourceService = dataSourceService;
    }
    @GetMapping("/dbconfig")
    public List getEmployeesByRole(@RequestHeader("OrgId") String OrgId) throws SQLException {

    	List<configuration> config = configService.findDbbyOrgId(OrgId);
    	String Username = null;
        String Url = null;
        String DriverClassName = null;
        String Password = null;
        if (!config.isEmpty()) {
        	configuration dbdata = config.get(0); 
        	Url = dbdata.geturl();
            Username = dbdata.getuserName();
            DriverClassName=dbdata.getclassName();
            Password= dbdata.getpassword();
        }
       //System.out.println(Url+Username+Password+DriverClassName+OrgId);
        DataSource ds=dataSourceService. getConnection( OrgId, Username,  Password,  Url,  DriverClassName);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

     // Execute your SQL query
     String sqlQuery = "SELECT * FROM transactions";
     List<Transaction> transactions = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Transaction.class));
        return transactions;
    }
}
