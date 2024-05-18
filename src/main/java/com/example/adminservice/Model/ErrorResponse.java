package com.example.adminservice.Model;

public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    // Getters and setters
}
