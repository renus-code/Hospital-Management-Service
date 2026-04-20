package com.hospital.hms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.hms.model.Doctor;

public interface DoctorRepository extends MongoRepository<Doctor, String> {

	Optional<Doctor> findByDoctorsUsernameIgnoreCase(String doctorsUsername);

	List<Doctor> findByDoctorsNameContainingIgnoreCase(String name);

	List<Doctor> findByDoctorsEmailContainingIgnoreCase(String email);

	List<Doctor> findByDoctorsNameContainingIgnoreCaseAndDoctorsEmailContainingIgnoreCase(String name, String email);

	boolean existsByDoctorsUsernameIgnoreCase(String doctorsUsername);

	boolean existsByDoctorsEmailIgnoreCase(String doctorsEmail);
}
