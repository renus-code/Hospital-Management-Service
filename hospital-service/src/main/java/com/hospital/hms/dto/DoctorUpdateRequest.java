package com.hospital.hms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorUpdateRequest(
		@NotBlank String doctorsName,
		@NotBlank String doctorsMobile,
		@NotBlank @Email String doctorsEmail,
		String doctorsAddress,
		/** When null or blank, the existing hash is kept. */
		String doctorsPassword,
		@NotBlank String doctorsUsername) {
}
