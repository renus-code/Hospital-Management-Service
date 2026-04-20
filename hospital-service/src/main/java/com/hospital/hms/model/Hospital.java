package com.hospital.hms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "hospitals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

	@Id
	private String hospitalId;

	@Indexed
	private String hospitalName;

	private String hospitalType;

	private String hospitalDescription;

	private String hospitalPlace;

	private String hospitalAddress;

	/** FK to {@link Doctor#doctorsId} (UML: hospital_doctor_id). */
	private String hospitalDoctorId;
}
