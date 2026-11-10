package com.abdulrasheed.jobtracker.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "auth_tokens")
public class AuthToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 64) private String token;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private User user;
    @Column(nullable = false) private Instant expiresAt;
    protected AuthToken() {}
    public AuthToken(String token, User user, Instant expiresAt) { this.token = token; this.user = user; this.expiresAt = expiresAt; }
    public String getToken() { return token; }
    public User getUser() { return user; }
    public Instant getExpiresAt() { return expiresAt; }
}
