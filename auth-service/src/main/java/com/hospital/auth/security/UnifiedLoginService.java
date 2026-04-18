package com.hospital.auth.security;

import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.hospital.auth.config.HospitalServiceProperties;
import com.hospital.auth.config.PatientServiceProperties;
import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.repository.RoleRepository;
import com.hospital.auth.repository.UserRepository;

/**
 * UML-aligned login: {@link User} uses email; {@link com.hospital.hms.model.Doctor} uses
 * doctors_username; {@link com.hospital.patient.model.Patient} uses patient_username — all via one UI.
 */
@Service
public class UnifiedLoginService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final RestTemplate restTemplate;
	private final HospitalServiceProperties hospitalProps;
	private final PatientServiceProperties patientProps;

	public UnifiedLoginService(
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			RestTemplate restTemplate,
			HospitalServiceProperties hospitalProps,
			PatientServiceProperties patientProps) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.restTemplate = restTemplate;
		this.hospitalProps = hospitalProps;
		this.patientProps = patientProps;
	}

	public Optional<LoginOutcome> authenticate(String principal, String rawPassword) {
		if (principal == null || principal.isBlank() || rawPassword == null) {
			return Optional.empty();
		}
		String p = principal.trim();

		Optional<User> userOpt = userRepository.findByUserEmailIgnoreCase(p.toLowerCase());
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(rawPassword, user.getUserPassword())) {
				String roleTitle = roleRepository.findById(user.getUserRoleId())
						.map(Role::getRoleTitle)
						.orElse("USER");
				return Optional.of(new LoginOutcome(user.getUserId(), user.getUserEmail(), roleTitle));
			}
			return Optional.empty();
		}

		Optional<LoginOutcome> doctor = verifyDoctorRemote(p, rawPassword);
		if (doctor.isPresent()) {
			return doctor;
		}
		return verifyPatientRemote(p, rawPassword);
	}

	private Optional<LoginOutcome> verifyDoctorRemote(String username, String rawPassword) {
		try {
			String url = hospitalBase() + "/api/integration/auth/verify-doctor";
			var body = new DoctorVerifyRequest(username, rawPassword);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<DoctorVerifyRequest> entity = new HttpEntity<>(body, headers);
			ResponseEntity<DoctorVerifyResponse> response = restTemplate.postForEntity(url, entity, DoctorVerifyResponse.class);
			DoctorVerifyResponse r = response.getBody();
			if (r == null || r.doctorsId() == null) {
				return Optional.empty();
			}
			return Optional.of(new LoginOutcome(r.doctorsId(), r.doctorsEmail(), "DOCTOR"));
		}
		catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 401) {
				return Optional.empty();
			}
			return Optional.empty();
		}
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	private Optional<LoginOutcome> verifyPatientRemote(String username, String rawPassword) {
		try {
			String url = patientBase() + "/api/integration/auth/verify-patient";
			var body = new PatientVerifyRequest(username, rawPassword);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<PatientVerifyRequest> entity = new HttpEntity<>(body, headers);
			ResponseEntity<PatientVerifyResponse> response = restTemplate.postForEntity(url, entity, PatientVerifyResponse.class);
			PatientVerifyResponse r = response.getBody();
			if (r == null || r.patientId() == null) {
				return Optional.empty();
			}
			String email = r.patientEmail() != null ? r.patientEmail() : "";
			return Optional.of(new LoginOutcome(r.patientId(), email, "PATIENT"));
		}
		catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().value() == 401) {
				return Optional.empty();
			}
			return Optional.empty();
		}
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	private String hospitalBase() {
		return hospitalProps.getBaseUrl().replaceAll("/$", "");
	}

	private String patientBase() {
		return patientProps.getBaseUrl().replaceAll("/$", "");
	}

	private record DoctorVerifyRequest(String username, String password) {
	}

	private record DoctorVerifyResponse(String doctorsId, String doctorsEmail, String doctorsName) {
	}

	private record PatientVerifyRequest(String username, String password) {
	}

	private record PatientVerifyResponse(String patientId, String patientEmail, String patientName) {
	}
}
