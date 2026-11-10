package com.abdulrasheed.jobtracker.service;

import com.abdulrasheed.jobtracker.dto.*;
import com.abdulrasheed.jobtracker.model.*;
import com.abdulrasheed.jobtracker.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository users;
    private final AuthTokenRepository tokens;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final long TOKEN_SECONDS = 86400;
    public AuthService(UserRepository users, AuthTokenRepository tokens) { this.users=users; this.tokens=tokens; }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email=request.email().trim().toLowerCase();
        if(users.findByEmail(email).isPresent()) throw new IllegalArgumentException("Email is already registered");
        User user=users.save(new User(email, encoder.encode(request.password())));
        return issue(user);
    }
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user=users.findByEmail(request.email().trim().toLowerCase())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if(!encoder.matches(request.password(), user.getPasswordHash()))
            throw new IllegalArgumentException("Invalid email or password");
        return issue(user);
    }
    private AuthResponse issue(User user) {
        String token=UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        tokens.save(new AuthToken(token, user, Instant.now().plusSeconds(TOKEN_SECONDS)));
        return new AuthResponse(token, "Bearer", TOKEN_SECONDS);
    }
}
