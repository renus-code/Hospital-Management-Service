package com.hospital.auth.portal;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hospital.auth.config.HospitalServiceProperties;
import com.hospital.auth.portal.dto.DoctorDto;
import com.hospital.auth.portal.dto.DoctorEditPayload;
import com.hospital.auth.portal.dto.DoctorFormPayload;
import com.hospital.auth.portal.dto.AppointmentDto;
import com.hospital.auth.portal.dto.AppointmentFormPayload;
import com.hospital.auth.portal.dto.HospitalDto;
import com.hospital.auth.portal.dto.HospitalFormPayload;
import com.hospital.auth.portal.dto.MedicineDto;
import com.hospital.auth.portal.dto.MedicineFormPayload;

@Component
public class HospitalApiClient {

	private final RestTemplate restTemplate;
	private final HospitalServiceProperties hospitalProps;

	public HospitalApiClient(RestTemplate restTemplate, HospitalServiceProperties hospitalProps) {
		this.restTemplate = restTemplate;
		this.hospitalProps = hospitalProps;
	}

	private String base() {
		return hospitalProps.getBaseUrl().replaceAll("/$", "");
	}

	private HttpHeaders bearer(String rawToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(rawToken.replaceFirst("^Bearer\\s+", ""));
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	/** True when hospital-service is up (integration endpoint responds with HTTP 2xx). */
	public boolean pingHospitalIntegration() {
		try {
			String url = base() + "/api/integration/auth/ping";
			var response = restTemplate.getForEntity(url, String.class);
			return response.getStatusCode().is2xxSuccessful();
		}
		catch (RestClientException e) {
			return false;
		}
	}

	public List<HospitalDto> listHospitals(String accessToken) {
		String url = base() + "/api/hospitals";
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		ResponseEntity<HospitalDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, HospitalDto[].class);
		HospitalDto[] body = response.getBody();
		return body == null ? List.of() : Arrays.asList(body);
	}

	public HospitalDto getHospital(String accessToken, String hospitalId) {
		String url = base() + "/api/hospitals/" + hospitalId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		return restTemplate.exchange(url, HttpMethod.GET, entity, HospitalDto.class).getBody();
	}

	public void createHospital(String accessToken, HospitalFormPayload payload) {
		String url = base() + "/api/hospitals";
		HttpEntity<HospitalFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.POST, entity, HospitalDto.class);
	}

	public void updateHospital(String accessToken, String hospitalId, HospitalFormPayload payload) {
		String url = base() + "/api/hospitals/" + hospitalId;
		HttpEntity<HospitalFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
	}

	public void deleteHospital(String accessToken, String hospitalId) {
		String url = base() + "/api/hospitals/" + hospitalId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}

	public List<DoctorDto> listDoctors(String accessToken) {
		String url = base() + "/api/doctors";
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		ResponseEntity<DoctorDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, DoctorDto[].class);
		DoctorDto[] body = response.getBody();
		return body == null ? List.of() : Arrays.asList(body);
	}

	public void createDoctor(String accessToken, DoctorFormPayload payload) {
		String url = base() + "/api/doctors";
		HttpEntity<DoctorFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.POST, entity, DoctorDto.class);
	}

	public void updateDoctor(String accessToken, String doctorsId, DoctorEditPayload payload) {
		String url = base() + "/api/doctors/" + doctorsId;
		HttpEntity<DoctorEditPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
	}

	public void deleteDoctor(String accessToken, String doctorsId) {
		String url = base() + "/api/doctors/" + doctorsId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}

	public List<DoctorDto> listDoctorsForSelect(String accessToken) {
		return listDoctors(accessToken);
	}

	public List<MedicineDto> listMedicines(String accessToken) {
		String url = base() + "/api/medicines";
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		ResponseEntity<MedicineDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, MedicineDto[].class);
		MedicineDto[] body = response.getBody();
		return body == null ? List.of() : Arrays.asList(body);
	}

	public MedicineDto getMedicine(String accessToken, String medicinesId) {
		String url = base() + "/api/medicines/" + medicinesId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		return restTemplate.exchange(url, HttpMethod.GET, entity, MedicineDto.class).getBody();
	}

	public void createMedicine(String accessToken, MedicineFormPayload payload) {
		String url = base() + "/api/medicines";
		HttpEntity<MedicineFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.POST, entity, MedicineDto.class);
	}

	public void updateMedicine(String accessToken, String medicinesId, MedicineFormPayload payload) {
		String url = base() + "/api/medicines/" + medicinesId;
		HttpEntity<MedicineFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
	}

	public void deleteMedicine(String accessToken, String medicinesId) {
		String url = base() + "/api/medicines/" + medicinesId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}

	public List<AppointmentDto> listAppointments(String accessToken) {
		String url = base() + "/api/appointments";
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		ResponseEntity<AppointmentDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, AppointmentDto[].class);
		AppointmentDto[] body = response.getBody();
		return body == null ? List.of() : Arrays.asList(body);
	}

	public AppointmentDto getAppointment(String accessToken, String appointmentId) {
		String url = base() + "/api/appointments/" + appointmentId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		return restTemplate.exchange(url, HttpMethod.GET, entity, AppointmentDto.class).getBody();
	}

	public void createAppointment(String accessToken, AppointmentFormPayload payload) {
		String url = base() + "/api/appointments";
		HttpEntity<AppointmentFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.POST, entity, AppointmentDto.class);
	}

	public void updateAppointment(String accessToken, String appointmentId, AppointmentFormPayload payload) {
		String url = base() + "/api/appointments/" + appointmentId;
		HttpEntity<AppointmentFormPayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
	}

	public void deleteAppointment(String accessToken, String appointmentId) {
		String url = base() + "/api/appointments/" + appointmentId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}
}
