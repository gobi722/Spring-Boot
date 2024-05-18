package com.example.adminservice.ServiceImpl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public  class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String role;

    public CustomUserDetails(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Implement UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities (roles) for the user
        // For simplicity, you can return null or an empty collection here
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Implement other UserDetails methods...

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Assuming account is always enabled
    }


   
}
