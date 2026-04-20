package com.hospital.hms.service;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.MedicineRequest;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.Medicine;
import com.hospital.hms.repository.MedicineRepository;

@Service
public class MedicineService {

	private static final Logger log = LoggerFactory.getLogger(MedicineService.class);

	private final MedicineRepository medicineRepository;

	public MedicineService(MedicineRepository medicineRepository) {
		this.medicineRepository = medicineRepository;
	}

	public Medicine addMedicine(MedicineRequest request) {
		log.info("Adding medicine: name='{}', company='{}'", request.medicinesName(), request.medicinesCompany());
		Medicine medicine = Medicine.builder()
				.medicinesName(request.medicinesName().trim())
				.medicinesCompany(trimOrNull(request.medicinesCompany()))
				.medicinesCost(trimOrNull(request.medicinesCost()))
				.medicinesType(trimOrNull(request.medicinesType()))
				.medicinesDescription(trimOrNull(request.medicinesDescription()))
				.build();
		Medicine saved = medicineRepository.save(medicine);
		log.info("Medicine created: medicinesId='{}'", saved.getMedicinesId());
		return saved;
	}

	public Medicine editMedicine(String medicinesId, MedicineRequest request) {
		log.info("Editing medicine: medicinesId='{}'", medicinesId);
		Medicine medicine = medicineRepository.findById(medicinesId)
				.orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicinesId));
		medicine.setMedicinesName(request.medicinesName().trim());
		medicine.setMedicinesCompany(trimOrNull(request.medicinesCompany()));
		medicine.setMedicinesCost(trimOrNull(request.medicinesCost()));
		medicine.setMedicinesType(trimOrNull(request.medicinesType()));
		medicine.setMedicinesDescription(trimOrNull(request.medicinesDescription()));
		Medicine saved = medicineRepository.save(medicine);
		log.info("Medicine updated: medicinesId='{}'", saved.getMedicinesId());
		return saved;
	}

	public void deleteMedicine(String medicinesId) {
		log.info("Deleting medicine: medicinesId='{}'", medicinesId);
		medicineRepository.findById(medicinesId)
				.orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicinesId));
		medicineRepository.deleteById(medicinesId);
		log.info("Medicine deleted: medicinesId='{}'", medicinesId);
	}

	public Medicine getMedicine(String medicinesId) {
		log.debug("Fetching medicine by id: medicinesId='{}'", medicinesId);
		return medicineRepository.findById(medicinesId)
				.orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicinesId));
	}

	public List<Medicine> listMedicines() {
		List<Medicine> medicines = medicineRepository.findAll();
		log.debug("Listed medicines count={}", medicines.size());
		return medicines;
	}

	public List<Medicine> searchMedicines(String name, String company, String type) {
		boolean hasName = name != null && !name.isBlank();
		boolean hasCompany = company != null && !company.isBlank();
		boolean hasType = type != null && !type.isBlank();
		if (!hasName && !hasCompany && !hasType) {
			return listMedicines();
		}
		String n = lower(name);
		String c = lower(company);
		String t = lower(type);
		List<Medicine> results = medicineRepository.findAll().stream()
				.filter(m -> !hasName || containsIgnoreCase(m.getMedicinesName(), n))
				.filter(m -> !hasCompany || containsIgnoreCase(m.getMedicinesCompany(), c))
				.filter(m -> !hasType || containsIgnoreCase(m.getMedicinesType(), t))
				.toList();
		log.debug("Searched medicines: name='{}', company='{}', type='{}', matched={}", name, company, type, results.size());
		return results;
	}

	private static String trimOrNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}

	private static String lower(String value) {
		return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
	}

	private static boolean containsIgnoreCase(String value, String search) {
		return value != null && value.toLowerCase(Locale.ROOT).contains(search);
	}
}
