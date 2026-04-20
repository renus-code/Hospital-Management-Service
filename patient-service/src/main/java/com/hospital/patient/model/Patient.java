package com.hospital.patient.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "patients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

	@Id
	private String patientId;

	@Indexed
	private String patientName;

	/** UML: patient_username — used for unified login at auth-service. */
	@Indexed(unique = true)
	private String patientUsername;

	/** BCrypt hash of patient_password (UML). */
	private String patientPassword;

	private LocalDate patientDob;
	private String patientGender;
	private String patientMobile;
	private String patientEmail;
	private String patientAddress;
	private String patientBloodGroup;
	private String patientMedicalHistory;

	/** Optional reference to hospital-service hospitalId. */
	private String hospitalId;

	/** Optional reference to hospital-service doctorsId. */
	private String doctorId;
}
