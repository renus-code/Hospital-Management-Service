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

import com.hospital.auth.config.PatientServiceProperties;
import com.hospital.auth.portal.dto.PatientCreatePayload;
import com.hospital.auth.portal.dto.PatientDto;
import com.hospital.auth.portal.dto.PatientUpdatePayload;

@Component
public class PatientApiClient {

	private final RestTemplate restTemplate;
	private final PatientServiceProperties patientProps;

	public PatientApiClient(RestTemplate restTemplate, PatientServiceProperties patientProps) {
		this.restTemplate = restTemplate;
		this.patientProps = patientProps;
	}

	private String base() {
		return patientProps.getBaseUrl().replaceAll("/$", "");
	}

	private HttpHeaders bearer(String rawToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(rawToken.replaceFirst("^Bearer\\s+", ""));
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public boolean pingPatientIntegration() {
		try {
			String url = base() + "/api/integration/health";
			var response = restTemplate.getForEntity(url, String.class);
			return response.getStatusCode().is2xxSuccessful();
		}
		catch (RestClientException e) {
			return false;
		}
	}

	public List<PatientDto> listPatients(String accessToken) {
		String url = base() + "/api/patients";
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		ResponseEntity<PatientDto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, PatientDto[].class);
		PatientDto[] body = response.getBody();
		return body == null ? List.of() : Arrays.asList(body);
	}

	public PatientDto getPatient(String accessToken, String patientId) {
		String url = base() + "/api/patients/" + patientId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		return restTemplate.exchange(url, HttpMethod.GET, entity, PatientDto.class).getBody();
	}

	public void createPatient(String accessToken, PatientCreatePayload payload) {
		String url = base() + "/api/patients";
		HttpEntity<PatientCreatePayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.POST, entity, PatientDto.class);
	}

	public void updatePatient(String accessToken, String patientId, PatientUpdatePayload payload) {
		String url = base() + "/api/patients/" + patientId;
		HttpEntity<PatientUpdatePayload> entity = new HttpEntity<>(payload, bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.PUT, entity, PatientDto.class);
	}

	public void deletePatient(String accessToken, String patientId) {
		String url = base() + "/api/patients/" + patientId;
		HttpEntity<Void> entity = new HttpEntity<>(bearer(accessToken));
		restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
	}
}
