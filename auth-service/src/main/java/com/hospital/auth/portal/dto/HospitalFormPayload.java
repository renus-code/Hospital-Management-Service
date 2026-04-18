package com.hospital.auth.portal.dto;

import jakarta.validation.constraints.NotBlank;

public record HospitalFormPayload(
		@NotBlank String hospitalName,
		@NotBlank String hospitalType,
		String hospitalDescription,
		String hospitalPlace,
		String hospitalAddress,
		String hospitalDoctorId) {
}
