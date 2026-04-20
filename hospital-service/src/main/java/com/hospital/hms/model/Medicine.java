package com.hospital.hms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "medicines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

	@Id
	private String medicinesId;

	@Indexed
	private String medicinesName;

	private String medicinesCompany;

	private String medicinesCost;

	private String medicinesType;

	private String medicinesDescription;
}
