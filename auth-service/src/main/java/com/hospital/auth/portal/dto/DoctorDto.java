package com.hospital.auth.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DoctorDto(
		String doctorsId,
		String doctorsName,
		String doctorsMobile,
		String doctorsEmail,
		String doctorsAddress,
		String doctorsUsername) {
}
