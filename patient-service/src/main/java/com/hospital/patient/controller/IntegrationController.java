package com.hospital.patient.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

	@GetMapping("/health")
	public Map<String, Object> health() {
		return Map.of(
				"service", "patient-service",
				"status", "UP");
	}
}
