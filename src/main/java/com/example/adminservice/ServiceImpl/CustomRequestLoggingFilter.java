package com.example.adminservice.ServiceImpl;


import org.springframework.web.filter.CommonsRequestLoggingFilter;

import jakarta.servlet.http.HttpServletRequest;

public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        // Capture the start time of the request
        request.setAttribute("startTime", System.currentTimeMillis());
        super.beforeRequest(request, message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        // Calculate the elapsed time since the request started
        long startTime = (long) request.getAttribute("startTime");
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Capture custom attributes such as response status and client host
        int responseStatus = (int) request.getAttribute("responseStatus");
        String clientHost = request.getRemoteAddr();

        // Create a log message with the necessary information
        String logMessage = String.format("%s | Response Status: %d | Elapsed Time: %d ms | Client Host: %s", message, responseStatus, elapsedTime, clientHost);
        super.afterRequest(request, logMessage);
    }
}

