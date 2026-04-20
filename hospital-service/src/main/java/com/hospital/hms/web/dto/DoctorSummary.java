package com.hospital.hms.web.dto;

import com.hospital.hms.model.Doctor;

public record DoctorSummary(String doctorsId, String doctorsName) {

	public static DoctorSummary from(Doctor d) {
		return new DoctorSummary(d.getDoctorsId(), d.getDoctorsName());
	}
}
