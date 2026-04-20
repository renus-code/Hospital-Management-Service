package com.hospital.patient.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientUpdateRequest(
		@NotBlank String patientName,
		@NotBlank String patientUsername,
		LocalDate patientDob,
		String patientGender,
		@Pattern(regexp = "^[0-9+\\-()\\s]{7,20}$", message = "must be a valid phone number") String patientMobile,
		@Email String patientEmail,
		String patientAddress,
		String patientBloodGroup,
		String patientMedicalHistory,
		String hospitalId,
		String doctorId,
		/** When null or blank, existing password hash is kept. */
		String patientPassword) {
}
