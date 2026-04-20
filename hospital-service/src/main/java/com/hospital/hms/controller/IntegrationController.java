package com.hospital.hms.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.client.AuthServiceClient;
import com.hospital.hms.client.AuthUserDto;
import com.hospital.hms.config.AuthServiceProperties;

/**
 * Demonstrates {@link org.springframework.web.client.RestTemplate} calls to auth-service.
 */
@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

	private final AuthServiceClient authServiceClient;
	private final AuthServiceProperties authServiceProperties;

	public IntegrationController(AuthServiceClient authServiceClient, AuthServiceProperties authServiceProperties) {
		this.authServiceClient = authServiceClient;
		this.authServiceProperties = authServiceProperties;
	}

	/** Calls auth {@code GET /api/auth/ping} via RestTemplate. */
	@GetMapping("/auth/ping")
	public Map<String, Object> authPing() {
		boolean ok = authServiceClient.pingAuthService();
		return Map.of(
				"authBaseUrl", authServiceProperties.getBaseUrl(),
				"authReachable", ok,
				"integration", "RestTemplate -> auth-service");
	}

	/**
	 * Calls auth {@code GET /api/users/{userId}} with the same Bearer token (JWT propagation demo).
	 */
	@GetMapping("/auth/users/{userId}")
	public ResponseEntity<AuthUserDto> proxyUser(
			@PathVariable String userId,
			@RequestHeader(value = "Authorization", required = false) String authorization) {
		if (authorization == null || authorization.isBlank()) {
			return ResponseEntity.badRequest().build();
		}
		return authServiceClient.getUser(userId, authorization)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
