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

import com.hospital.hms.dto.HospitalRequest;
import com.hospital.hms.model.Hospital;
import com.hospital.hms.service.HospitalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

	private final HospitalService hospitalService;

	public HospitalController(HospitalService hospitalService) {
		this.hospitalService = hospitalService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Hospital addHospital(@Valid @RequestBody HospitalRequest request) {
		return hospitalService.addHospital(request);
	}

	@PutMapping("/{hospitalId}")
	public Hospital editHospital(@PathVariable String hospitalId, @Valid @RequestBody HospitalRequest request) {
		return hospitalService.editHospital(hospitalId, request);
	}

	@DeleteMapping("/{hospitalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteHospital(@PathVariable String hospitalId) {
		hospitalService.deleteHospital(hospitalId);
	}

	@GetMapping("/search")
	public List<Hospital> searchHospitals(@RequestParam(required = false) String name) {
		return hospitalService.searchHospitals(name);
	}

	@GetMapping("/{hospitalId}")
	public Hospital getHospital(@PathVariable String hospitalId) {
		return hospitalService.getHospital(hospitalId);
	}

	@GetMapping
	public List<Hospital> listHospitals() {
		return hospitalService.listHospitals();
	}
}
