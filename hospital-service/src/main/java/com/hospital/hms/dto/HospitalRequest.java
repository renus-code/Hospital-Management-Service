package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;

public record HospitalRequest(
		@NotBlank String hospitalName,
		@NotBlank String hospitalType,
		String hospitalDescription,
		String hospitalPlace,
		String hospitalAddress,
		/** Optional link to {@link com.hospital.hms.model.Doctor#getDoctorsId()}. */
		String hospitalDoctorId) {
}
