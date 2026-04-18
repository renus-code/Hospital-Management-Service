package com.hospital.auth.portal.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentFormPayload(
		@NotBlank String appointmentNumber,
		@NotBlank String appointmentType,
		@NotNull LocalDate appointmentDate,
		String appointmentDescription,
		String appointmentDoctorId) {
}
