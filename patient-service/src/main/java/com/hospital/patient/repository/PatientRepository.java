package com.hospital.patient.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.patient.model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {

	Optional<Patient> findByPatientUsernameIgnoreCase(String patientUsername);

	List<Patient> findByPatientNameContainingIgnoreCase(String patientName);

	List<Patient> findByPatientEmailContainingIgnoreCase(String patientEmail);

	List<Patient> findByHospitalId(String hospitalId);

	List<Patient> findByDoctorId(String doctorId);
}
