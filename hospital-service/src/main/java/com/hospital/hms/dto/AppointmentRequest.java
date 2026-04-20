package com.hospital.hms.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentRequest(
		@NotBlank String appointmentNumber,
		@NotBlank String appointmentType,
		@NotNull LocalDate appointmentDate,
		String appointmentDescription,
		String appointmentDoctorId) {
}
