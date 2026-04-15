package com.hospital.auth.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
		String userName,
		@Email String userEmail,
		String userPassword,
		LocalDate userDob,
		String userAddress,
		String userRoleId) {
}
