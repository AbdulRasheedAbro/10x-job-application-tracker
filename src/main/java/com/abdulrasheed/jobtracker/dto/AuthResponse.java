package com.abdulrasheed.jobtracker.dto;
public record AuthResponse(String token, String tokenType, long expiresInSeconds) {}
