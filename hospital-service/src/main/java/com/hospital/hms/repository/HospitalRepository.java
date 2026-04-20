package com.hospital.hms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.hms.model.Hospital;

public interface HospitalRepository extends MongoRepository<Hospital, String> {

	List<Hospital> findByHospitalNameContainingIgnoreCase(String hospitalName);

	boolean existsByHospitalDoctorId(String hospitalDoctorId);
}
