package com.hospital.patient.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.dto.PatientResponse;
import com.hospital.patient.dto.PatientUpdateRequest;
import com.hospital.patient.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

	private final PatientService patientService;

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PatientResponse addPatient(@Valid @RequestBody PatientRequest request) {
		return PatientResponse.from(patientService.addPatient(request));
	}

	@PutMapping("/{patientId}")
	public PatientResponse editPatient(@PathVariable String patientId, @Valid @RequestBody PatientUpdateRequest request) {
		return PatientResponse.from(patientService.editPatient(patientId, request));
	}

	@DeleteMapping("/{patientId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePatient(@PathVariable String patientId) {
		patientService.deletePatient(patientId);
	}

	@GetMapping("/{patientId}")
	public PatientResponse getPatient(@PathVariable String patientId) {
		return PatientResponse.from(patientService.getPatient(patientId));
	}

	@GetMapping
	public List<PatientResponse> listPatients() {
		return patientService.listPatients().stream().map(PatientResponse::from).toList();
	}

	@GetMapping("/search")
	public List<PatientResponse> searchPatients(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String hospitalId,
			@RequestParam(required = false) String doctorId) {
		return patientService.searchPatients(name, email, hospitalId, doctorId)
				.stream()
				.map(PatientResponse::from)
				.toList();
	}
}
