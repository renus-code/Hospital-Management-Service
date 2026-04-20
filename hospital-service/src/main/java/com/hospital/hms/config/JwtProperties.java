package com.hospital.hms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Must use the same {@code jwt.secret} as auth-service so tokens issued there validate here (HS256).
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String secret;

	private long expirationMs = 86400000L;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	public void setExpirationMs(long expirationMs) {
		this.expirationMs = expirationMs;
	}
}
