package com.hospital.hms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.dto.AppointmentResponse;
import com.hospital.hms.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;

	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AppointmentResponse addAppointment(@Valid @RequestBody AppointmentRequest request) {
		return AppointmentResponse.from(appointmentService.addAppointment(request));
	}

	@PutMapping("/{appointmentId}")
	public AppointmentResponse editAppointment(
			@PathVariable String appointmentId,
			@Valid @RequestBody AppointmentRequest request) {
		return AppointmentResponse.from(appointmentService.editAppointment(appointmentId, request));
	}

	@DeleteMapping("/{appointmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAppointment(@PathVariable String appointmentId) {
		appointmentService.deleteAppointment(appointmentId);
	}

	@GetMapping("/search")
	public List<AppointmentResponse> searchAppointments(
			@RequestParam(required = false) String number,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(required = false) String doctorId) {
		return appointmentService.searchAppointments(number, type, date, doctorId).stream()
				.map(AppointmentResponse::from)
				.toList();
	}

	@GetMapping("/{appointmentId}")
	public AppointmentResponse getAppointment(@PathVariable String appointmentId) {
		return AppointmentResponse.from(appointmentService.getAppointment(appointmentId));
	}

	@GetMapping
	public List<AppointmentResponse> listAppointments() {
		return appointmentService.listAppointments().stream().map(AppointmentResponse::from).toList();
	}
}
