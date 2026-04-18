package com.hospital.auth.portal.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicineFormPayload(
		@NotBlank String medicinesName,
		String medicinesCompany,
		String medicinesCost,
		String medicinesType,
		String medicinesDescription) {
}
