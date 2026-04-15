package com.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequest(
		@NotBlank String userId,
		@NotBlank String roleId) {
}
