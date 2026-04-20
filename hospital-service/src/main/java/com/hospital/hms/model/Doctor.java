package com.hospital.hms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "doctors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

	@Id
	private String doctorsId;

	private String doctorsName;

	private String doctorsMobile;

	@Indexed(unique = true)
	private String doctorsEmail;

	private String doctorsAddress;

	private String doctorsPassword;

	@Indexed(unique = true)
	private String doctorsUsername;
}
