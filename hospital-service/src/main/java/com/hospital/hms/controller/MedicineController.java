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

import com.hospital.hms.dto.MedicineRequest;
import com.hospital.hms.dto.MedicineResponse;
import com.hospital.hms.service.MedicineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

	private final MedicineService medicineService;

	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MedicineResponse addMedicine(@Valid @RequestBody MedicineRequest request) {
		return MedicineResponse.from(medicineService.addMedicine(request));
	}

	@PutMapping("/{medicinesId}")
	public MedicineResponse editMedicine(@PathVariable String medicinesId, @Valid @RequestBody MedicineRequest request) {
		return MedicineResponse.from(medicineService.editMedicine(medicinesId, request));
	}

	@DeleteMapping("/{medicinesId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMedicine(@PathVariable String medicinesId) {
		medicineService.deleteMedicine(medicinesId);
	}

	@GetMapping("/search")
	public List<MedicineResponse> searchMedicines(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String company,
			@RequestParam(required = false) String type) {
		return medicineService.searchMedicines(name, company, type).stream()
				.map(MedicineResponse::from)
				.toList();
	}

	@GetMapping("/{medicinesId}")
	public MedicineResponse getMedicine(@PathVariable String medicinesId) {
		return MedicineResponse.from(medicineService.getMedicine(medicinesId));
	}

	@GetMapping
	public List<MedicineResponse> listMedicines() {
		return medicineService.listMedicines().stream().map(MedicineResponse::from).toList();
	}
}
