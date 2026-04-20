package com.hospital.hms.dto;

import com.hospital.hms.model.Doctor;

public record DoctorResponse(
		String doctorsId,
		String doctorsName,
		String doctorsMobile,
		String doctorsEmail,
		String doctorsAddress,
		String doctorsUsername) {

	public static DoctorResponse from(Doctor d) {
		return new DoctorResponse(
				d.getDoctorsId(),
				d.getDoctorsName(),
				d.getDoctorsMobile(),
				d.getDoctorsEmail(),
				d.getDoctorsAddress(),
				d.getDoctorsUsername());
	}
}
