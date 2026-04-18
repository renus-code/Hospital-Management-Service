package com.hospital.auth.security;

/**
 * Result of a successful unified login (admin user, doctor, or patient) used for JWT and session.
 */
public record LoginOutcome(String subjectId, String email, String roleTitle) {
}
