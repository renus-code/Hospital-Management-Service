package com.hospital.hms.dto;

import java.time.LocalDate;

import com.hospital.hms.model.Appointment;

public record AppointmentResponse(
		String appointmentId,
		String appointmentNumber,
		String appointmentType,
		LocalDate appointmentDate,
		String appointmentDescription,
		String appointmentDoctorId) {

	public static AppointmentResponse from(Appointment appointment) {
		return new AppointmentResponse(
				appointment.getAppointmentId(),
				appointment.getAppointmentNumber(),
				appointment.getAppointmentType(),
				appointment.getAppointmentDate(),
				appointment.getAppointmentDescription(),
				appointment.getAppointmentDoctorId());
	}
}
