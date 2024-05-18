package com.example.adminservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.adminservice.Model.ChangePasswordRequest;
import com.example.adminservice.Model.JwtRequest;
import com.example.adminservice.Model.LoginResponse;
import com.example.adminservice.Model.User;
import com.example.adminservice.Service.UserService;
import com.example.adminservice.ServiceImpl.CustomUserDetails;
import com.example.adminservice.ServiceImpl.JwtUtil;
import com.example.adminservice.ServiceImpl.MyUserDetailsService;

//import com.example.login.security.JwtResponse;


@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/user_register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
        	return ResponseEntity.badRequest().body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);
        LoginResponse response = new LoginResponse();
        response.setName(userDetails.getUsername());
        if (userDetails instanceof CustomUserDetails) {
        
        response.setUserRole(((CustomUserDetails) userDetails).getRole());
        }  
        response.setToken(jwt);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        // Extract username and new password from the request
        String username = changePasswordRequest.getUsername();
        String newPassword = changePasswordRequest.getNewPassword();

        // Update password logic
        try {
        	userService .changePassword(username, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to change password: " + e.getMessage());
        }
    }
}
