package com.hospital.patient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.patient.dto.PatientLoginVerifyRequest;
import com.hospital.patient.dto.PatientLoginVerifyResponse;
import com.hospital.patient.service.PatientService;

import jakarta.validation.Valid;

/**
 * Password check for unified auth UI login (patient_username / patient_password per UML).
 */
@RestController
@RequestMapping("/api/integration/auth")
public class PatientAuthIntegrationController {

	private final PatientService patientService;

	public PatientAuthIntegrationController(PatientService patientService) {
		this.patientService = patientService;
	}

	@PostMapping("/verify-patient")
	public ResponseEntity<PatientLoginVerifyResponse> verifyPatient(@Valid @RequestBody PatientLoginVerifyRequest request) {
		return patientService.verifyLogin(request.username(), request.password())
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}
