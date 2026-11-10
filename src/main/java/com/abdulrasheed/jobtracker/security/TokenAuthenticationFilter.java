package com.abdulrasheed.jobtracker.security;

import com.abdulrasheed.jobtracker.model.AuthToken;
import com.abdulrasheed.jobtracker.repository.AuthTokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenRepository tokens;
    public TokenAuthenticationFilter(AuthTokenRepository tokens) { this.tokens=tokens; }
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
        throws ServletException,IOException {
        String header=request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")) {
            String raw=header.substring(7);
            AuthToken token=tokens.findByToken(raw).orElse(null);
            if(token!=null && token.getExpiresAt().isAfter(Instant.now()))
                request.setAttribute("authenticatedUser",token.getUser());
        }
        chain.doFilter(request,response);
    }
}
