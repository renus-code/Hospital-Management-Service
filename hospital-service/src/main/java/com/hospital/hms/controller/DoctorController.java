package com.hospital.hms.controller;

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

import com.hospital.hms.dto.DoctorRequest;
import com.hospital.hms.dto.DoctorResponse;
import com.hospital.hms.dto.DoctorUpdateRequest;
import com.hospital.hms.service.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	private final DoctorService doctorService;

	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DoctorResponse addDoctor(@Valid @RequestBody DoctorRequest request) {
		return DoctorResponse.from(doctorService.addDoctor(request));
	}

	@PutMapping("/{doctorsId}")
	public DoctorResponse editDoctor(@PathVariable String doctorsId, @Valid @RequestBody DoctorUpdateRequest request) {
		return DoctorResponse.from(doctorService.editDoctor(doctorsId, request));
	}

	@DeleteMapping("/{doctorsId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDoctor(@PathVariable String doctorsId) {
		doctorService.deleteDoctor(doctorsId);
	}

	@GetMapping("/search")
	public List<DoctorResponse> searchDoctors(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email) {
		return doctorService.searchDoctors(name, email).stream().map(DoctorResponse::from).toList();
	}

	@GetMapping("/{doctorsId}")
	public DoctorResponse getDoctor(@PathVariable String doctorsId) {
		return DoctorResponse.from(doctorService.getDoctor(doctorsId));
	}

	@GetMapping
	public List<DoctorResponse> listDoctors() {
		return doctorService.listDoctors().stream().map(DoctorResponse::from).toList();
	}
}
