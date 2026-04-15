package com.hospital.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

	@Id
	private String permissionId;

	private String permissionRoleId;

	private String permissionTitle;

	private String permissionModule;

	private String permissionDescription;
}
