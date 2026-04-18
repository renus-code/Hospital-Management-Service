package com.hospital.auth.portal.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentDto(
		String appointmentId,
		String appointmentNumber,
		String appointmentType,
		LocalDate appointmentDate,
		String appointmentDescription,
		String appointmentDoctorId) {
}
