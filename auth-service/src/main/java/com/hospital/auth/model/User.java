package com.hospital.auth.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private String userId;

	private String userRoleId;

	private String userName;

	@Indexed(unique = true)
	private String userEmail;

	private LocalDate userDob;

	private String userAddress;

	/** Stored as BCrypt hash; required for login / JWT issuance. */
	private String userPassword;
}
