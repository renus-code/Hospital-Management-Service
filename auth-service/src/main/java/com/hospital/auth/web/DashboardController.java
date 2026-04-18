package com.hospital.auth.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hospital.auth.portal.HospitalApiClient;
import com.hospital.auth.portal.PatientApiClient;

@Controller
public class DashboardController {

	private final HospitalApiClient hospitalApiClient;
	private final PatientApiClient patientApiClient;

	public DashboardController(HospitalApiClient hospitalApiClient, PatientApiClient patientApiClient) {
		this.hospitalApiClient = hospitalApiClient;
		this.patientApiClient = patientApiClient;
	}

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		if (authentication != null) {
			model.addAttribute("username", authentication.getName());
		}
		model.addAttribute("hospitalIntegrationOk", hospitalApiClient.pingHospitalIntegration());
		model.addAttribute("patientIntegrationOk", patientApiClient.pingPatientIntegration());
		return "dashboard";
	}
}
