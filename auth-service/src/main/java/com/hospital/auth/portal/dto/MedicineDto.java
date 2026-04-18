package com.hospital.auth.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicineDto(
		String medicinesId,
		String medicinesName,
		String medicinesCompany,
		String medicinesCost,
		String medicinesType,
		String medicinesDescription) {
}
