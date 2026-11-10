package com.abdulrasheed.jobtracker.security;
import com.abdulrasheed.jobtracker.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
@Component
public class CurrentUser {
    public User require(HttpServletRequest request) {
        Object user=request.getAttribute("authenticatedUser");
        if(user instanceof User u) return u;
        throw new SecurityException("Authentication required. Sign in and send a Bearer token.");
    }
}
