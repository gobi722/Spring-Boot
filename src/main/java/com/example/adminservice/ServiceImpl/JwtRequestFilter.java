package com.example.adminservice.ServiceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.adminservice.Model.User;
import com.example.adminservice.Service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	    @Autowired
	    private JwtUtil jwtUtil;

	    @Autowired
	    @Lazy
	    private MyUserDetailsService userDetailsService;

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	            throws ServletException, IOException {
	        final String authorizationHeader = request.getHeader("Authorization");

	        String username = null;
	        String jwt = null;

	        try {
	            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	                jwt = authorizationHeader.substring(7);
	                username = jwtUtil.extractUsername(jwt);
	            }

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

	                if (jwtUtil.validateToken(jwt, userDetails)) {
	                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
	                            userDetails, null, userDetails.getAuthorities());
	                    usernamePasswordAuthenticationToken
	                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	                } else {
	                    throw new RuntimeException("JWT Token is not valid");
	                }
	            }
	            chain.doFilter(request, response);
	        } catch (UsernameNotFoundException e) {
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.getWriter().write("Invalid username or password");
	        } catch (RuntimeException e) {
	            response.setStatus(HttpStatus.UNAUTHORIZED.value());
	            response.getWriter().write("Invalid or expired JWT token");
	        }
	    }

	}


	

