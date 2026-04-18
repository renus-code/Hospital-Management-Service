package com.hospital.auth.portal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorEditPayload(
		@NotBlank String doctorsName,
		@NotBlank String doctorsMobile,
		@NotBlank @Email String doctorsEmail,
		String doctorsAddress,
		String doctorsPassword,
		@NotBlank String doctorsUsername) {
}
