package com.hospital.hms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.hms.model.Medicine;

public interface MedicineRepository extends MongoRepository<Medicine, String> {
}
