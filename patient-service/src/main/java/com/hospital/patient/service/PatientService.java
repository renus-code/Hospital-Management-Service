package com.hospital.patient.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.patient.dto.PatientLoginVerifyResponse;
import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.dto.PatientUpdateRequest;
import com.hospital.patient.exception.DuplicateResourceException;
import com.hospital.patient.exception.ResourceNotFoundException;
import com.hospital.patient.model.Patient;
import com.hospital.patient.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository patientRepository;
	private final PasswordEncoder passwordEncoder;

	public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
		this.patientRepository = patientRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Patient addPatient(PatientRequest request) {
		ensureUniquePatientCredentials(request.patientEmail(), request.patientUsername(), null);
		Patient patient = Patient.builder()
				.patientName(request.patientName().trim())
				.patientUsername(request.patientUsername().trim())
				.patientPassword(passwordEncoder.encode(request.patientPassword()))
				.patientDob(request.patientDob())
				.patientGender(trimOrNull(request.patientGender()))
				.patientMobile(trimOrNull(request.patientMobile()))
				.patientEmail(normalizeEmail(request.patientEmail()))
				.patientAddress(trimOrNull(request.patientAddress()))
				.patientBloodGroup(trimOrNull(request.patientBloodGroup()))
				.patientMedicalHistory(trimOrNull(request.patientMedicalHistory()))
				.hospitalId(trimOrNull(request.hospitalId()))
				.doctorId(trimOrNull(request.doctorId()))
				.build();
		return patientRepository.save(patient);
	}

	public Patient editPatient(String patientId, PatientUpdateRequest request) {
		Patient patient = getPatient(patientId);
		ensureUniquePatientCredentials(request.patientEmail(), request.patientUsername(), patientId);
		patient.setPatientName(request.patientName().trim());
		patient.setPatientUsername(request.patientUsername().trim());
		patient.setPatientDob(request.patientDob());
		patient.setPatientGender(trimOrNull(request.patientGender()));
		patient.setPatientMobile(trimOrNull(request.patientMobile()));
		patient.setPatientEmail(normalizeEmail(request.patientEmail()));
		patient.setPatientAddress(trimOrNull(request.patientAddress()));
		patient.setPatientBloodGroup(trimOrNull(request.patientBloodGroup()));
		patient.setPatientMedicalHistory(trimOrNull(request.patientMedicalHistory()));
		patient.setHospitalId(trimOrNull(request.hospitalId()));
		patient.setDoctorId(trimOrNull(request.doctorId()));
		if (request.patientPassword() != null && !request.patientPassword().isBlank()) {
			patient.setPatientPassword(passwordEncoder.encode(request.patientPassword()));
		}
		return patientRepository.save(patient);
	}

	public void deletePatient(String patientId) {
		Patient patient = getPatient(patientId);
		patientRepository.delete(patient);
	}

	public Patient getPatient(String patientId) {
		return patientRepository.findById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
	}

	/** Used by auth-service unified login (UML: patient_username + patient_password). */
	public Optional<PatientLoginVerifyResponse> verifyLogin(String username, String rawPassword) {
		if (username == null || username.isBlank() || rawPassword == null) {
			return Optional.empty();
		}
		return patientRepository.findByPatientUsernameIgnoreCase(username.trim())
				.filter(p -> p.getPatientPassword() != null && passwordEncoder.matches(rawPassword, p.getPatientPassword()))
				.map(p -> new PatientLoginVerifyResponse(
						p.getPatientId(),
						p.getPatientEmail() != null ? p.getPatientEmail() : "",
						p.getPatientName()));
	}

	public List<Patient> listPatients() {
		return patientRepository.findAll();
	}

	public List<Patient> searchPatients(String name, String email, String hospitalId, String doctorId) {
		if (hospitalId != null && !hospitalId.isBlank()) {
			return patientRepository.findByHospitalId(hospitalId.trim());
		}
		if (doctorId != null && !doctorId.isBlank()) {
			return patientRepository.findByDoctorId(doctorId.trim());
		}
		if (email != null && !email.isBlank()) {
			return patientRepository.findByPatientEmailContainingIgnoreCase(email.trim());
		}
		if (name != null && !name.isBlank()) {
			return patientRepository.findByPatientNameContainingIgnoreCase(name.trim());
		}
		return listPatients();
	}

	private void ensureUniquePatientCredentials(String email, String username, String excludePatientId) {
		String u = username != null ? username.trim() : "";
		String e = email != null ? normalizeEmail(email) : null;
		patientRepository.findAll().stream()
				.filter(p -> excludePatientId == null || !p.getPatientId().equals(excludePatientId))
				.forEach(p -> {
					if (p.getPatientUsername() != null && p.getPatientUsername().equalsIgnoreCase(u)) {
						throw new DuplicateResourceException("Username already in use: " + u);
					}
					if (e != null && p.getPatientEmail() != null && p.getPatientEmail().equalsIgnoreCase(e)) {
						throw new DuplicateResourceException("Email already in use: " + e);
					}
				});
	}

	private static String trimOrNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}

	private static String normalizeEmail(String email) {
		String normalized = trimOrNull(email);
		if (normalized == null) {
			return null;
		}
		return normalized.toLowerCase(Locale.ROOT);
	}
}
