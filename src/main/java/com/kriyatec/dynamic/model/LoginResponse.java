package com.kriyatec.dynamic.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "login_response")
public class LoginResponse {
    
    @Id
    private String id; // If you want MongoDB to generate _id field
    @NotNull
    private String name;
    private Object role;
    private String token;
    private Object employeeId; // Marking as required in MongoDB is not directly supported in Java
    
    // Constructors, getters, setters
}
