package com.hospital.auth.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
		@NotBlank String userName,
		@NotBlank @Email String userEmail,
		@NotBlank String userPassword,
		LocalDate userDob,
		String userAddress,
		/** Optional; defaults to USER role if omitted. */
		String userRoleId) {
}
