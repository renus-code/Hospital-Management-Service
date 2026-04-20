package com.hospital.hms.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicineRequest(
		@NotBlank String medicinesName,
		String medicinesCompany,
		String medicinesCost,
		String medicinesType,
		String medicinesDescription) {
}
