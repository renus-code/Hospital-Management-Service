package com.hospital.hms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.DoctorLoginVerifyRequest;
import com.hospital.hms.dto.DoctorLoginVerifyResponse;
import com.hospital.hms.service.DoctorService;

import jakarta.validation.Valid;

/**
 * Password check for unified auth UI login (doctors_username / doctors_password per UML).
 */
@RestController
@RequestMapping("/api/integration/auth")
public class DoctorAuthIntegrationController {

	private final DoctorService doctorService;

	public DoctorAuthIntegrationController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@PostMapping("/verify-doctor")
	public ResponseEntity<DoctorLoginVerifyResponse> verifyDoctor(@Valid @RequestBody DoctorLoginVerifyRequest request) {
		return doctorService.verifyLogin(request.username(), request.password())
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}
