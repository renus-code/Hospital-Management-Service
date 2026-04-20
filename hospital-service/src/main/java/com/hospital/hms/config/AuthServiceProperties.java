package com.hospital.hms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.auth")
public class AuthServiceProperties {

	/**
	 * Base URL of auth-service, e.g. http://localhost:8081
	 */
	private String baseUrl = "http://localhost:8081";

	/** Enable RestTemplate calls to auth-service. */
	private boolean enabled = true;

	/**
	 * When true, hospital mutations require a successful {@code GET /api/auth/ping} to auth-service.
	 */
	private boolean strict = false;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}
}
