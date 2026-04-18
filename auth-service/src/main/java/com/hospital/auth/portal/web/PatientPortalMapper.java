package com.hospital.auth.portal.web;

import com.hospital.auth.portal.dto.PatientCreatePayload;
import com.hospital.auth.portal.dto.PatientFormPayload;
import com.hospital.auth.portal.dto.PatientUpdatePayload;

final class PatientPortalMapper {

	private PatientPortalMapper() {
	}

	static PatientCreatePayload toCreate(PatientFormPayload f) {
		return new PatientCreatePayload(
				f.patientName(),
				f.patientUsername().trim(),
				f.patientPassword(),
				f.patientDob(),
				f.patientGender(),
				f.patientMobile(),
				f.patientEmail(),
				f.patientAddress(),
				f.patientBloodGroup(),
				f.patientMedicalHistory(),
				f.hospitalId(),
				f.doctorId());
	}

	static PatientUpdatePayload toUpdate(PatientFormPayload f) {
		String pwd = f.patientPassword();
		if (pwd != null && pwd.isBlank()) {
			pwd = null;
		}
		return new PatientUpdatePayload(
				f.patientName(),
				f.patientUsername().trim(),
				f.patientDob(),
				f.patientGender(),
				f.patientMobile(),
				f.patientEmail(),
				f.patientAddress(),
				f.patientBloodGroup(),
				f.patientMedicalHistory(),
				f.hospitalId(),
				f.doctorId(),
				pwd);
	}
}
