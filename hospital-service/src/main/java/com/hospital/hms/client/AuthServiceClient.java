package com.hospital.hms.client;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hospital.hms.config.AuthServiceProperties;

/**
 * Inter-service calls to auth-service using {@link RestTemplate} (Jackson on JSON).
 */
@Component
public class AuthServiceClient {

	private final RestTemplate restTemplate;
	private final AuthServiceProperties props;

	public AuthServiceClient(RestTemplate restTemplate, AuthServiceProperties props) {
		this.restTemplate = restTemplate;
		this.props = props;
	}

	public boolean isEnabled() {
		return props.isEnabled() && props.getBaseUrl() != null && !props.getBaseUrl().isBlank();
	}

	/** GET /api/auth/ping — no JWT required. */
	public boolean pingAuthService() {
		if (!isEnabled()) {
			return true;
		}
		try {
			String url = props.getBaseUrl().replaceAll("/$", "") + "/api/auth/ping";
			@SuppressWarnings("unchecked")
			Map<String, String> body = restTemplate.getForObject(url, Map.class);
			return body != null && "UP".equalsIgnoreCase(body.getOrDefault("status", ""));
		}
		catch (RestClientException e) {
			return false;
		}
	}

	/** GET /api/users/{userId} — requires Bearer token from auth login. */
	public Optional<AuthUserDto> getUser(String userId, String bearerToken) {
		if (!isEnabled()) {
			return Optional.empty();
		}
		try {
			String url = props.getBaseUrl().replaceAll("/$", "") + "/api/users/" + userId;
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(bearerToken.replaceFirst("^Bearer\\s+", ""));
			headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			ResponseEntity<AuthUserDto> response = restTemplate.exchange(
					url,
					HttpMethod.GET,
					entity,
					AuthUserDto.class);
			return Optional.ofNullable(response.getBody());
		}
		catch (RestClientException e) {
			return Optional.empty();
		}
	}
}
