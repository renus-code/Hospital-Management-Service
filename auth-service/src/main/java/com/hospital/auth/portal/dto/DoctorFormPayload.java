package com.hospital.auth.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorFormPayload(
		@NotBlank String doctorsName,
		@NotBlank String doctorsMobile,
		@NotBlank @Email String doctorsEmail,
		String doctorsAddress,
		@NotBlank String doctorsPassword,
		@NotBlank String doctorsUsername) {
}
