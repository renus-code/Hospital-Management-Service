package com.hospital.hms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.DoctorLoginVerifyResponse;
import com.hospital.hms.dto.DoctorRequest;
import com.hospital.hms.dto.DoctorResponse;
import com.hospital.hms.dto.DoctorUpdateRequest;
import com.hospital.hms.exception.BadRequestException;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.HospitalRepository;

@Service
public class DoctorService {

	private final DoctorRepository doctorRepository;
	private final HospitalRepository hospitalRepository;
	private final PasswordEncoder passwordEncoder;

	public DoctorService(
			DoctorRepository doctorRepository,
			HospitalRepository hospitalRepository,
			PasswordEncoder passwordEncoder) {
		this.doctorRepository = doctorRepository;
		this.hospitalRepository = hospitalRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Doctor addDoctor(DoctorRequest request) {
		ensureUniqueCredentials(request.doctorsEmail(), request.doctorsUsername(), null);
		Doctor doctor = Doctor.builder()
				.doctorsName(request.doctorsName().trim())
				.doctorsMobile(request.doctorsMobile().trim())
				.doctorsEmail(request.doctorsEmail().trim().toLowerCase())
				.doctorsAddress(request.doctorsAddress())
				.doctorsPassword(passwordEncoder.encode(request.doctorsPassword()))
				.doctorsUsername(request.doctorsUsername().trim())
				.build();
		return doctorRepository.save(doctor);
	}

	public Doctor editDoctor(String doctorsId, DoctorUpdateRequest request) {
		Doctor doctor = doctorRepository.findById(doctorsId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorsId));
		ensureUniqueCredentials(request.doctorsEmail(), request.doctorsUsername(), doctorsId);
		doctor.setDoctorsName(request.doctorsName().trim());
		doctor.setDoctorsMobile(request.doctorsMobile().trim());
		doctor.setDoctorsEmail(request.doctorsEmail().trim().toLowerCase());
		doctor.setDoctorsAddress(request.doctorsAddress());
		doctor.setDoctorsUsername(request.doctorsUsername().trim());
		if (request.doctorsPassword() != null && !request.doctorsPassword().isBlank()) {
			doctor.setDoctorsPassword(passwordEncoder.encode(request.doctorsPassword()));
		}
		return doctorRepository.save(doctor);
	}

	public void deleteDoctor(String doctorsId) {
		doctorRepository.findById(doctorsId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorsId));
		if (hospitalRepository.existsByHospitalDoctorId(doctorsId)) {
			throw new BadRequestException("Cannot delete doctor linked to one or more hospitals");
		}
		doctorRepository.deleteById(doctorsId);
	}

	public Doctor getDoctor(String doctorsId) {
		return doctorRepository.findById(doctorsId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorsId));
	}

	public List<Doctor> listDoctors() {
		return doctorRepository.findAll();
	}

	/** Used by auth-service unified login (UML: doctors_username + doctors_password). */
	public Optional<DoctorLoginVerifyResponse> verifyLogin(String username, String rawPassword) {
		if (username == null || username.isBlank() || rawPassword == null) {
			return Optional.empty();
		}
		return doctorRepository.findByDoctorsUsernameIgnoreCase(username.trim())
				.filter(d -> passwordEncoder.matches(rawPassword, d.getDoctorsPassword()))
				.map(DoctorResponse::from)
				.map(r -> new DoctorLoginVerifyResponse(r.doctorsId(), r.doctorsEmail(), r.doctorsName()));
	}

	public List<Doctor> searchDoctors(String name, String email) {
		boolean hasName = name != null && !name.isBlank();
		boolean hasEmail = email != null && !email.isBlank();
		if (!hasName && !hasEmail) {
			return listDoctors();
		}
		String n = hasName ? name.trim() : "";
		String e = hasEmail ? email.trim() : "";
		if (hasName && hasEmail) {
			return doctorRepository.findByDoctorsNameContainingIgnoreCaseAndDoctorsEmailContainingIgnoreCase(n, e);
		}
		if (hasName) {
			return doctorRepository.findByDoctorsNameContainingIgnoreCase(n);
		}
		return doctorRepository.findByDoctorsEmailContainingIgnoreCase(e);
	}

	private void ensureUniqueCredentials(String email, String username, String excludeDoctorId) {
		doctorRepository.findAll().stream()
				.filter(d -> excludeDoctorId == null || !d.getDoctorsId().equals(excludeDoctorId))
				.forEach(d -> {
					if (d.getDoctorsEmail().equalsIgnoreCase(email)) {
						throw new DuplicateResourceException("Email already in use: " + email);
					}
					if (d.getDoctorsUsername().equalsIgnoreCase(username)) {
						throw new DuplicateResourceException("Username already in use: " + username);
					}
				});
	}
}
