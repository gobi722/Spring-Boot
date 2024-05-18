package com.example.adminservice.Model;

public class ChangePasswordRequest {
    private String username;
    private String newPassword;

   
    public ChangePasswordRequest() {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
