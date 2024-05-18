package com.example.adminservice.Model;

import java.util.Map;

import lombok.Data;
@Data
public class LoginResponse {
    private String name;
    private String userRole;
    
    private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

   
}

