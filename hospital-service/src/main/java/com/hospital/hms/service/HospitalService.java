package com.hospital.hms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.hms.client.AuthServiceClient;
import com.hospital.hms.config.AuthServiceProperties;
import com.hospital.hms.dto.HospitalRequest;
import com.hospital.hms.exception.BadRequestException;
import com.hospital.hms.exception.DuplicateResourceException;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.exception.ServiceUnavailableException;
import com.hospital.hms.model.Hospital;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.HospitalRepository;

@Service
public class HospitalService {

	private final HospitalRepository hospitalRepository;
	private final DoctorRepository doctorRepository;
	private final AuthServiceClient authServiceClient;
	private final AuthServiceProperties authServiceProperties;

	public HospitalService(
			HospitalRepository hospitalRepository,
			DoctorRepository doctorRepository,
			AuthServiceClient authServiceClient,
			AuthServiceProperties authServiceProperties) {
		this.hospitalRepository = hospitalRepository;
		this.doctorRepository = doctorRepository;
		this.authServiceClient = authServiceClient;
		this.authServiceProperties = authServiceProperties;
	}

	public Hospital addHospital(HospitalRequest request) {
		assertAuthIfStrict();
		validateDoctorFk(request.hospitalDoctorId());
		ensureUniqueHospitalName(request.hospitalName(), null);
		Hospital h = Hospital.builder()
				.hospitalName(request.hospitalName().trim())
				.hospitalType(request.hospitalType().trim())
				.hospitalDescription(request.hospitalDescription())
				.hospitalPlace(request.hospitalPlace())
				.hospitalAddress(request.hospitalAddress())
				.hospitalDoctorId(trimOrNull(request.hospitalDoctorId()))
				.build();
		return hospitalRepository.save(h);
	}

	public Hospital editHospital(String hospitalId, HospitalRequest request) {
		assertAuthIfStrict();
		validateDoctorFk(request.hospitalDoctorId());
		Hospital h = hospitalRepository.findById(hospitalId)
				.orElseThrow(() -> new ResourceNotFoundException("Hospital not found: " + hospitalId));
		ensureUniqueHospitalName(request.hospitalName(), hospitalId);
		h.setHospitalName(request.hospitalName().trim());
		h.setHospitalType(request.hospitalType().trim());
		h.setHospitalDescription(request.hospitalDescription());
		h.setHospitalPlace(request.hospitalPlace());
		h.setHospitalAddress(request.hospitalAddress());
		h.setHospitalDoctorId(trimOrNull(request.hospitalDoctorId()));
		return hospitalRepository.save(h);
	}

	public void deleteHospital(String hospitalId) {
		assertAuthIfStrict();
		Hospital h = hospitalRepository.findById(hospitalId)
				.orElseThrow(() -> new ResourceNotFoundException("Hospital not found: " + hospitalId));
		hospitalRepository.delete(h);
	}

	public Hospital getHospital(String hospitalId) {
		return hospitalRepository.findById(hospitalId)
				.orElseThrow(() -> new ResourceNotFoundException("Hospital not found: " + hospitalId));
	}

	public List<Hospital> listHospitals() {
		return hospitalRepository.findAll();
	}

	public List<Hospital> searchHospitals(String name) {
		if (name == null || name.isBlank()) {
			return listHospitals();
		}
		return hospitalRepository.findByHospitalNameContainingIgnoreCase(name.trim());
	}

	private void validateDoctorFk(String hospitalDoctorId) {
		String id = trimOrNull(hospitalDoctorId);
		if (id == null) {
			return;
		}
		if (!doctorRepository.existsById(id)) {
			throw new BadRequestException("Linked doctor does not exist: " + id);
		}
	}

	private void ensureUniqueHospitalName(String name, String excludeId) {
		String n = name.trim();
		hospitalRepository.findAll().stream()
				.filter(h -> excludeId == null || !h.getHospitalId().equals(excludeId))
				.filter(h -> h.getHospitalName() != null && h.getHospitalName().equalsIgnoreCase(n))
				.findAny()
				.ifPresent(h -> {
					throw new DuplicateResourceException("Hospital name already exists: " + n);
				});
	}

	private void assertAuthIfStrict() {
		if (!authServiceProperties.isStrict()) {
			return;
		}
		if (!authServiceClient.isEnabled()) {
			throw new ServiceUnavailableException("Auth integration disabled but strict mode is on");
		}
		if (!authServiceClient.pingAuthService()) {
			throw new ServiceUnavailableException("auth-service is not reachable (RestTemplate ping failed)");
		}
	}

	private static String trimOrNull(String s) {
		if (s == null || s.isBlank()) {
			return null;
		}
		return s.trim();
	}
}
