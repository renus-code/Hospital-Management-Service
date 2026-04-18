package com.hospital.auth.portal.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PatientDto(
		String patientId,
		String patientName,
		String patientUsername,
		LocalDate patientDob,
		String patientGender,
		String patientMobile,
		String patientEmail,
		String patientAddress,
		String patientBloodGroup,
		String patientMedicalHistory,
		String hospitalId,
		String doctorId) {
}
