package com.example.adminservice.Service;

import com.example.adminservice.Model.User;

public interface UserService {
    void changePassword(String username, String newPassword);
    User register(User user) ;
    User findByUsername(String username);
}
