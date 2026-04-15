package com.hospital.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequest(
		@NotBlank String permissionRoleId,
		@NotBlank String permissionTitle,
		@NotBlank String permissionModule,
		String permissionDescription) {
}
