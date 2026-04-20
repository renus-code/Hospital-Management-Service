package com.hospital.auth.portal.web;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.hospital.auth.portal.dto.DoctorDto;
import com.hospital.auth.portal.dto.DoctorEditPayload;
import com.hospital.auth.portal.dto.DoctorFormPayload;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/doctors")
public class PortalDoctorWebController {

	private final HospitalApiClient hospitalApiClient;

	public PortalDoctorWebController(HospitalApiClient hospitalApiClient) {
		this.hospitalApiClient = hospitalApiClient;
	}

	@GetMapping
	public String list(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			model.addAttribute("doctors", hospitalApiClient.listDoctors(token));
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load doctors. Is hospital-service running on port 8082?");
			return "redirect:/dashboard";
		}
		return "doctors/list";
	}

	@GetMapping("/new")
	@PreAuthorize("hasRole('ADMIN')")
	public String createForm(HttpSession session, RedirectAttributes ra, Model model) {
		if (PortalSessionSupport.requireToken(session, ra) == null) {
			return "redirect:/login";
		}
		if (!model.containsAttribute("doctorRequest")) {
			model.addAttribute("doctorRequest", new DoctorFormPayload("", "", "", "", "", ""));
		}
		return "doctors/form-create";
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public String create(
			@Valid @ModelAttribute("doctorRequest") DoctorFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			return "doctors/form-create";
		}
		try {
			hospitalApiClient.createDoctor(token, request);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not create doctor (duplicate username or hospital-service error).");
			return "redirect:/doctors/new";
		}
		return "redirect:/doctors";
	}

	@GetMapping("/{doctorsId}/edit")
	@PreAuthorize("hasRole('ADMIN')")
	public String editForm(@PathVariable String doctorsId, HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			var doctors = hospitalApiClient.listDoctors(token);
			DoctorDto d = doctors.stream()
					.filter(x -> doctorsId.equals(x.doctorsId()))
					.findFirst()
					.orElse(null);
			if (d == null) {
				ra.addFlashAttribute("errorMessage", "Doctor not found.");
				return "redirect:/doctors";
			}
			model.addAttribute("doctorEdit", new DoctorEditPayload(
					d.doctorsName(),
					d.doctorsMobile(),
					d.doctorsEmail(),
					d.doctorsAddress(),
					null,
					d.doctorsUsername()));
			model.addAttribute("doctorsId", doctorsId);
			return "doctors/form-edit";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load doctor.");
			return "redirect:/doctors";
		}
	}

	@PostMapping("/{doctorsId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String update(
			@PathVariable String doctorsId,
			@Valid @ModelAttribute("doctorEdit") DoctorEditPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			return "doctors/form-edit";
		}
		DoctorEditPayload payload = PortalSessionSupport.normalizeDoctorEdit(request);
		try {
			hospitalApiClient.updateDoctor(token, doctorsId, payload);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not update doctor.");
			return "redirect:/doctors/" + doctorsId + "/edit";
		}
		return "redirect:/doctors";
	}

	@PostMapping("/{doctorsId}/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public String delete(@PathVariable String doctorsId, HttpSession session, RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			hospitalApiClient.deleteDoctor(token, doctorsId);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not delete doctor.");
		}
		return "redirect:/doctors";
	}
}
