package com.hospital.auth.dto;

public record AuthResponse(String token, String tokenType, long expiresInSeconds, String userId, String roleTitle) {
}
