package com.hospital.hms.dto;

import com.hospital.hms.model.Medicine;

public record MedicineResponse(
		String medicinesId,
		String medicinesName,
		String medicinesCompany,
		String medicinesCost,
		String medicinesType,
		String medicinesDescription) {

	public static MedicineResponse from(Medicine medicine) {
		return new MedicineResponse(
				medicine.getMedicinesId(),
				medicine.getMedicinesName(),
				medicine.getMedicinesCompany(),
				medicine.getMedicinesCost(),
				medicine.getMedicinesType(),
				medicine.getMedicinesDescription());
	}
}
