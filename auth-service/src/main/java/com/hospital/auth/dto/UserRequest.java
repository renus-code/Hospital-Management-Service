package com.hospital.auth.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
		@NotBlank String userName,
		@NotBlank @Email String userEmail,
		@NotBlank String userPassword,
		LocalDate userDob,
		String userAdd,
		String userAddress,
		String userRoleId) {
}
