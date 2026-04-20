package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;

public record DoctorLoginVerifyRequest(
		@NotBlank String username,
		@NotBlank String password) {
}
