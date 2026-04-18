package com.hospital.auth.portal.web;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.auth.portal.dto.DoctorEditPayload;
import com.hospital.auth.portal.dto.HospitalFormPayload;
import com.hospital.auth.portal.dto.PatientFormPayload;
import com.hospital.auth.security.JwtSessionAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpSession;

final class PortalSessionSupport {

	private PortalSessionSupport() {
	}

	static String requireToken(HttpSession session, RedirectAttributes ra) {
		Object raw = session.getAttribute(JwtSessionAuthenticationSuccessHandler.SESSION_ACCESS_TOKEN);
		if (!(raw instanceof String token) || token.isBlank()) {
			ra.addFlashAttribute("errorMessage", "Please sign in again to use the application.");
			return null;
		}
		return token;
	}

	static HospitalFormPayload normalizeHospital(HospitalFormPayload request) {
		String docId = request.hospitalDoctorId();
		if (docId != null && docId.isBlank()) {
			docId = null;
		}
		return new HospitalFormPayload(
				request.hospitalName(),
				request.hospitalType(),
				request.hospitalDescription(),
				request.hospitalPlace(),
				request.hospitalAddress(),
				docId);
	}

	static DoctorEditPayload normalizeDoctorEdit(DoctorEditPayload request) {
		String pwd = request.doctorsPassword();
		if (pwd != null && pwd.isBlank()) {
			pwd = null;
		}
		return new DoctorEditPayload(
				request.doctorsName(),
				request.doctorsMobile(),
				request.doctorsEmail(),
				request.doctorsAddress(),
				pwd,
				request.doctorsUsername());
	}

	static PatientFormPayload normalizePatientForm(PatientFormPayload request) {
		String hospitalId = request.hospitalId();
		String doctorId = request.doctorId();
		if (hospitalId != null && hospitalId.isBlank()) {
			hospitalId = null;
		}
		if (doctorId != null && doctorId.isBlank()) {
			doctorId = null;
		}
		return new PatientFormPayload(
				request.patientName(),
				request.patientUsername(),
				request.patientPassword(),
				request.patientDob(),
				request.patientGender(),
				request.patientMobile(),
				request.patientEmail(),
				request.patientAddress(),
				request.patientBloodGroup(),
				request.patientMedicalHistory(),
				hospitalId,
				doctorId);
	}
}
