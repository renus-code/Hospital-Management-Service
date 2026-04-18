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
import com.hospital.auth.portal.dto.HospitalDto;
import com.hospital.auth.portal.dto.HospitalFormPayload;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/hospitals")
public class PortalHospitalWebController {

	private final HospitalApiClient hospitalApiClient;

	public PortalHospitalWebController(HospitalApiClient hospitalApiClient) {
		this.hospitalApiClient = hospitalApiClient;
	}

	@GetMapping
	public String list(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			model.addAttribute("hospitals", hospitalApiClient.listHospitals(token));
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load hospitals. Is hospital-service running on port 8082?");
			return "redirect:/dashboard";
		}
		return "hospitals/list";
	}

	@GetMapping("/new")
	public String createForm(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (!model.containsAttribute("hospitalRequest")) {
			model.addAttribute("hospitalRequest", new HospitalFormPayload("", "", "", "", "", null));
		}
		if (!loadDoctorOptionsIntoModel(token, ra, model)) {
			return "redirect:/hospitals";
		}
		model.addAttribute("editMode", false);
		return "hospitals/form";
	}

	@GetMapping("/{hospitalId}/edit")
	public String editForm(
			@PathVariable String hospitalId,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			HospitalDto h = hospitalApiClient.getHospital(token, hospitalId);
			model.addAttribute("hospitalRequest", new HospitalFormPayload(
					h.hospitalName(),
					h.hospitalType(),
					h.hospitalDescription(),
					h.hospitalPlace(),
					h.hospitalAddress(),
					h.hospitalDoctorId()));
			model.addAttribute("hospitalId", hospitalId);
			if (!loadDoctorOptionsIntoModel(token, ra, model)) {
				return "redirect:/hospitals";
			}
			model.addAttribute("editMode", true);
			return "hospitals/form";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Hospital not found or hospital-service unreachable.");
			return "redirect:/hospitals";
		}
	}

	@PostMapping
	public String create(
			@Valid @ModelAttribute("hospitalRequest") HospitalFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			loadDoctorOptionsIntoModel(token, ra, model);
			model.addAttribute("editMode", false);
			return "hospitals/form";
		}
		HospitalFormPayload payload = PortalSessionSupport.normalizeHospital(request);
		try {
			hospitalApiClient.createHospital(token, payload);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not create hospital.");
			return "redirect:/hospitals/new";
		}
		return "redirect:/hospitals";
	}

	@PostMapping("/{hospitalId}")
	public String update(
			@PathVariable String hospitalId,
			@Valid @ModelAttribute("hospitalRequest") HospitalFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("hospitalId", hospitalId);
			loadDoctorOptionsIntoModel(token, ra, model);
			model.addAttribute("editMode", true);
			return "hospitals/form";
		}
		HospitalFormPayload payload = PortalSessionSupport.normalizeHospital(request);
		try {
			hospitalApiClient.updateHospital(token, hospitalId, payload);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not update hospital.");
			return "redirect:/hospitals/" + hospitalId + "/edit";
		}
		return "redirect:/hospitals";
	}

	@PostMapping("/{hospitalId}/delete")
	public String delete(@PathVariable String hospitalId, HttpSession session, RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			hospitalApiClient.deleteHospital(token, hospitalId);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not delete hospital.");
		}
		return "redirect:/hospitals";
	}

	private boolean loadDoctorOptionsIntoModel(String token, RedirectAttributes ra, Model model) {
		try {
			model.addAttribute("doctorOptions", hospitalApiClient.listDoctorsForSelect(token));
			return true;
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load doctors for assignment.");
			return false;
		}
	}
}
