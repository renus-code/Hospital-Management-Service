package com.hospital.patient.dto;

public record PatientLoginVerifyResponse(
		String patientId,
		String patientEmail,
		String patientName) {
}
