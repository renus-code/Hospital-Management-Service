package com.hospital.auth.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HospitalDto(
		String hospitalId,
		String hospitalName,
		String hospitalType,
		String hospitalDescription,
		String hospitalPlace,
		String hospitalAddress,
		String hospitalDoctorId) {
}
