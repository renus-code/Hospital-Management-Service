package com.hospital.auth.dto;

import java.time.LocalDate;

public record UserResponse(
		String userId,
		String userRoleId,
		String userName,
		String userEmail,
		LocalDate userDob,
		String userAddress) {
}
