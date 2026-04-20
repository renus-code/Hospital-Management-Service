package com.hospital.hms.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

	@Id
	private String appointmentId;

	@Indexed(unique = true)
	private String appointmentNumber;

	private String appointmentType;

	private LocalDate appointmentDate;

	private String appointmentDescription;

	/** FK to {@link Doctor#doctorsId} (UML: appointment_doctor_id). */
	private String appointmentDoctorId;
}
