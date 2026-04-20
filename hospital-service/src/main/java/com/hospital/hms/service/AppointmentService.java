package com.hospital.hms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.AppointmentRequest;
import com.hospital.hms.exception.BadRequestException;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;

@Service
public class AppointmentService {

	private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

	private final AppointmentRepository appointmentRepository;
	private final DoctorRepository doctorRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
		this.appointmentRepository = appointmentRepository;
		this.doctorRepository = doctorRepository;
	}

	public Appointment addAppointment(AppointmentRequest request) {
		log.info("Adding appointment: number='{}', doctorId='{}'", request.appointmentNumber(), request.appointmentDoctorId());
		validateDoctorFk(request.appointmentDoctorId());
		ensureUniqueNumber(request.appointmentNumber(), null);
		Appointment appointment = Appointment.builder()
				.appointmentNumber(request.appointmentNumber().trim())
				.appointmentType(request.appointmentType().trim())
				.appointmentDate(request.appointmentDate())
				.appointmentDescription(trimOrNull(request.appointmentDescription()))
				.appointmentDoctorId(trimOrNull(request.appointmentDoctorId()))
				.build();
		Appointment saved = appointmentRepository.save(appointment);
		log.info("Appointment created: appointmentId='{}', number='{}'", saved.getAppointmentId(), saved.getAppointmentNumber());
		return saved;
	}

	public Appointment editAppointment(String appointmentId, AppointmentRequest request) {
		log.info("Editing appointment: appointmentId='{}'", appointmentId);
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + appointmentId));
		validateDoctorFk(request.appointmentDoctorId());
		ensureUniqueNumber(request.appointmentNumber(), appointmentId);
		appointment.setAppointmentNumber(request.appointmentNumber().trim());
		appointment.setAppointmentType(request.appointmentType().trim());
		appointment.setAppointmentDate(request.appointmentDate());
		appointment.setAppointmentDescription(trimOrNull(request.appointmentDescription()));
		appointment.setAppointmentDoctorId(trimOrNull(request.appointmentDoctorId()));
		Appointment saved = appointmentRepository.save(appointment);
		log.info("Appointment updated: appointmentId='{}', number='{}'", saved.getAppointmentId(), saved.getAppointmentNumber());
		return saved;
	}

	public void deleteAppointment(String appointmentId) {
		log.info("Deleting appointment: appointmentId='{}'", appointmentId);
		appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + appointmentId));
		appointmentRepository.deleteById(appointmentId);
		log.info("Appointment deleted: appointmentId='{}'", appointmentId);
	}

	public Appointment getAppointment(String appointmentId) {
		log.debug("Fetching appointment by id: appointmentId='{}'", appointmentId);
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + appointmentId));
	}

	public List<Appointment> listAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		log.debug("Listed appointments count={}", appointments.size());
		return appointments;
	}

	public List<Appointment> searchAppointments(String number, String type, LocalDate date, String doctorId) {
		boolean hasNumber = number != null && !number.isBlank();
		boolean hasType = type != null && !type.isBlank();
		boolean hasDate = date != null;
		boolean hasDoctor = doctorId != null && !doctorId.isBlank();
		if (!hasNumber && !hasType && !hasDate && !hasDoctor) {
			return listAppointments();
		}
		String n = lower(number);
		String t = lower(type);
		String d = lower(doctorId);
		List<Appointment> results = appointmentRepository.findAll().stream()
				.filter(a -> !hasNumber || containsIgnoreCase(a.getAppointmentNumber(), n))
				.filter(a -> !hasType || containsIgnoreCase(a.getAppointmentType(), t))
				.filter(a -> !hasDate || a.getAppointmentDate().isEqual(date))
				.filter(a -> !hasDoctor || containsIgnoreCase(a.getAppointmentDoctorId(), d))
				.toList();
		log.debug(
				"Searched appointments: number='{}', type='{}', date='{}', doctorId='{}', matched={}",
				number,
				type,
				date,
				doctorId,
				results.size());
		return results;
	}

	private void validateDoctorFk(String doctorId) {
		String id = trimOrNull(doctorId);
		if (id == null) {
			return;
		}
		if (!doctorRepository.existsById(id)) {
			throw new BadRequestException("Linked doctor does not exist: " + id);
		}
	}

	private void ensureUniqueNumber(String appointmentNumber, String excludeAppointmentId) {
		String normalized = appointmentNumber.trim();
		appointmentRepository.findByAppointmentNumberIgnoreCase(normalized)
				.filter(a -> excludeAppointmentId == null || !a.getAppointmentId().equals(excludeAppointmentId))
				.ifPresent(a -> {
					throw new DuplicateResourceException("Appointment number already exists: " + normalized);
				});
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
