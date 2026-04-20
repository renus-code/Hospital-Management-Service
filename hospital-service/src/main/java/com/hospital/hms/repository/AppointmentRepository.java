package com.hospital.hms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.hms.model.Appointment;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

	Optional<Appointment> findByAppointmentNumberIgnoreCase(String appointmentNumber);
}
