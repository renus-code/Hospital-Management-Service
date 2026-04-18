package com.hospital.auth.portal.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Thymeleaf form for create/edit patient. Password required on create only (validated in controller).
 */
public record PatientFormPayload(
		@NotBlank String patientName,
		@NotBlank String patientUsername,
		String patientPassword,
		LocalDate patientDob,
		String patientGender,
		@Pattern(regexp = "^[0-9+\\-()\\s]{7,20}$", message = "must be a valid phone number") String patientMobile,
		@Email String patientEmail,
		String patientAddress,
		String patientBloodGroup,
		String patientMedicalHistory,
		String hospitalId,
		String doctorId) {
}
