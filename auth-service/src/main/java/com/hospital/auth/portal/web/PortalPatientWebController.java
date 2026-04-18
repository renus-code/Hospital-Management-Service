package com.hospital.auth.portal.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.auth.portal.HospitalApiClient;
import com.hospital.auth.portal.PatientApiClient;
import com.hospital.auth.portal.dto.PatientDto;
import com.hospital.auth.portal.dto.PatientFormPayload;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/patients")
public class PortalPatientWebController {

	private final PatientApiClient patientApiClient;
	private final HospitalApiClient hospitalApiClient;

	public PortalPatientWebController(PatientApiClient patientApiClient, HospitalApiClient hospitalApiClient) {
		this.patientApiClient = patientApiClient;
		this.hospitalApiClient = hospitalApiClient;
	}

	@GetMapping
	public String list(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			model.addAttribute("patients", patientApiClient.listPatients(token));
			return "patients/list";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load patients. Is patient-service running on port 8083?");
			return "redirect:/dashboard";
		}
	}

	@GetMapping("/new")
	public String createForm(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (!model.containsAttribute("patientRequest")) {
			model.addAttribute("patientRequest", new PatientFormPayload(
					"", "", "", null, "", "", "", "", "", "", null, null));
		}
		if (!loadSelectOptions(token, ra, model)) {
			return "redirect:/patients";
		}
		model.addAttribute("editMode", false);
		return "patients/form";
	}

	@GetMapping("/{patientId}/edit")
	public String editForm(@PathVariable String patientId, HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			PatientDto p = patientApiClient.getPatient(token, patientId);
			model.addAttribute("patientRequest", new PatientFormPayload(
					p.patientName(),
					p.patientUsername() != null ? p.patientUsername() : "",
					"",
					p.patientDob(),
					p.patientGender(),
					p.patientMobile(),
					p.patientEmail(),
					p.patientAddress(),
					p.patientBloodGroup(),
					p.patientMedicalHistory(),
					p.hospitalId(),
					p.doctorId()));
			model.addAttribute("patientId", patientId);
			if (!loadSelectOptions(token, ra, model)) {
				return "redirect:/patients";
			}
			model.addAttribute("editMode", true);
			return "patients/form";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load patient.");
			return "redirect:/patients";
		}
	}

	@PostMapping
	public String create(
			@Valid @ModelAttribute("patientRequest") PatientFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (request.patientPassword() == null || request.patientPassword().isBlank()) {
			bindingResult.rejectValue("patientPassword", "required", "Password is required for new patients");
		}
		if (bindingResult.hasErrors()) {
			loadSelectOptions(token, ra, model);
			model.addAttribute("editMode", false);
			return "patients/form";
		}
		try {
			PatientFormPayload normalized = PortalSessionSupport.normalizePatientForm(request);
			patientApiClient.createPatient(token, PatientPortalMapper.toCreate(normalized));
			return "redirect:/patients";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not create patient.");
			return "redirect:/patients/new";
		}
	}

	@PostMapping("/{patientId}")
	public String update(
			@PathVariable String patientId,
			@Valid @ModelAttribute("patientRequest") PatientFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("patientId", patientId);
			loadSelectOptions(token, ra, model);
			model.addAttribute("editMode", true);
			return "patients/form";
		}
		try {
			PatientFormPayload normalized = PortalSessionSupport.normalizePatientForm(request);
			patientApiClient.updatePatient(token, patientId, PatientPortalMapper.toUpdate(normalized));
			return "redirect:/patients";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not update patient.");
			return "redirect:/patients/" + patientId + "/edit";
		}
	}

	@PostMapping("/{patientId}/delete")
	public String delete(@PathVariable String patientId, HttpSession session, RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			patientApiClient.deletePatient(token, patientId);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not delete patient.");
		}
		return "redirect:/patients";
	}

	private boolean loadSelectOptions(String token, RedirectAttributes ra, Model model) {
		try {
			model.addAttribute("hospitals", hospitalApiClient.listHospitals(token));
			model.addAttribute("doctors", hospitalApiClient.listDoctors(token));
			return true;
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load doctor/hospital options.");
			return false;
		}
	}
}
