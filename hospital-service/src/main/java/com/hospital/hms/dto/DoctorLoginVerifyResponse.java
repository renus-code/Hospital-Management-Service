package com.hospital.hms.dto;

public record DoctorLoginVerifyResponse(
		String doctorsId,
		String doctorsEmail,
		String doctorsName) {
}
