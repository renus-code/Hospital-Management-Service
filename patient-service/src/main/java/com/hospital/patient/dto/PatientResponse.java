package com.hospital.patient.dto;

import java.time.LocalDate;

import com.hospital.patient.model.Patient;

public record PatientResponse(
		String patientId,
		String patientName,
		String patientUsername,
		LocalDate patientDob,
		String patientGender,
		String patientMobile,
		String patientEmail,
		String patientAddress,
		String patientBloodGroup,
		String patientMedicalHistory,
		String hospitalId,
		String doctorId) {

	public static PatientResponse from(Patient patient) {
		return new PatientResponse(
				patient.getPatientId(),
				patient.getPatientName(),
				patient.getPatientUsername(),
				patient.getPatientDob(),
				patient.getPatientGender(),
				patient.getPatientMobile(),
				patient.getPatientEmail(),
				patient.getPatientAddress(),
				patient.getPatientBloodGroup(),
				patient.getPatientMedicalHistory(),
				patient.getHospitalId(),
				patient.getDoctorId());
	}
}
