package com.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;

/** {@code userEmail} is the auth user email, or a doctor/patient username (UML). */
public record LoginRequest(
		@NotBlank String userEmail,
		@NotBlank String userPassword) {
}
