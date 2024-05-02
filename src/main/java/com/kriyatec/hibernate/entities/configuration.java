package com.kriyatec.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
@Entity
@Table(name = "configuration")
public class configuration {
	 
	 @Column(name = "userName")
	    private String userName;
	 @Column(name = "password")
	    private String password;
	 @Column(name = "url")
	    private String url; 
	 @Column(name = "className")
	    private String className;
	 @Column(name = "OrgId")
	    private String OrgId;
	  

	    // Getters and setters
	    public String getuserName() {
	        return userName;
	    }

	    public void setuserName(String userName) {
	        this.userName = userName;
	    }

	    public String getpassword() {
	        return password;
	    }

	    public void setRole(String password) {
	        this.password = password;
	    }

	    public String geturl() {
	        return url;
	    }

	    public void seturl(String url) {
	        this.url = url;
	    }

	    public String getclassName() {
	        return className;
	    }

	    public void setclassName(String className) {
	        this.className = className;
	    }
	    public String getOrgId() {
	        return OrgId;
	    }

	    public void setOrgId(String OrgId) {
	        this.OrgId = OrgId;
	    }
	   
}
