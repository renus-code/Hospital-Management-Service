package com.hospital.hms.client;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Mirrors auth-service {@code UserResponse} JSON for {@link AuthServiceClient}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthUserDto(
		String userId,
		String userRoleId,
		String userName,
		String userEmail,
		LocalDate userDob,
		String userAdd,
		String userAddress) {
}
