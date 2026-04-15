package com.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
		@NotBlank String roleTitle,
		String roleDescription) {
}
