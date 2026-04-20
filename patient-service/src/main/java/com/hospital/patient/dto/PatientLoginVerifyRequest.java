package com.hospital.patient.dto;

import jakarta.validation.constraints.NotBlank;

public record PatientLoginVerifyRequest(
		@NotBlank String username,
		@NotBlank String password) {
}
