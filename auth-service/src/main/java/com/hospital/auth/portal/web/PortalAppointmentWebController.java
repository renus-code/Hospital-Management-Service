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
import com.hospital.auth.portal.dto.AppointmentFormPayload;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/appointments")
public class PortalAppointmentWebController {

	private final HospitalApiClient hospitalApiClient;

	public PortalAppointmentWebController(HospitalApiClient hospitalApiClient) {
		this.hospitalApiClient = hospitalApiClient;
	}

	@GetMapping
	public String list(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			model.addAttribute("appointments", hospitalApiClient.listAppointments(token));
			return "appointments/list";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load appointments. Is hospital-service running on port 8082?");
			return "redirect:/dashboard";
		}
	}

	@GetMapping("/new")
	public String createForm(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (!model.containsAttribute("appointmentRequest")) {
			model.addAttribute("appointmentRequest", new AppointmentFormPayload("", "", null, "", ""));
		}
		if (!loadDoctorOptions(token, ra, model)) {
			return "redirect:/appointments";
		}
		model.addAttribute("editMode", false);
		return "appointments/form";
	}

	@GetMapping("/{appointmentId}/edit")
	public String editForm(@PathVariable String appointmentId, HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			var a = hospitalApiClient.getAppointment(token, appointmentId);
			model.addAttribute("appointmentRequest", new AppointmentFormPayload(
					a.appointmentNumber(),
					a.appointmentType(),
					a.appointmentDate(),
					a.appointmentDescription(),
					a.appointmentDoctorId()));
			model.addAttribute("appointmentId", appointmentId);
			if (!loadDoctorOptions(token, ra, model)) {
				return "redirect:/appointments";
			}
			model.addAttribute("editMode", true);
			return "appointments/form";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load appointment.");
			return "redirect:/appointments";
		}
	}

	@PostMapping
	public String create(
			@Valid @ModelAttribute("appointmentRequest") AppointmentFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			loadDoctorOptions(token, ra, model);
			model.addAttribute("editMode", false);
			return "appointments/form";
		}
		try {
			hospitalApiClient.createAppointment(token, request);
			return "redirect:/appointments";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not create appointment.");
			return "redirect:/appointments/new";
		}
	}

	@PostMapping("/{appointmentId}")
	public String update(
			@PathVariable String appointmentId,
			@Valid @ModelAttribute("appointmentRequest") AppointmentFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("appointmentId", appointmentId);
			loadDoctorOptions(token, ra, model);
			model.addAttribute("editMode", true);
			return "appointments/form";
		}
		try {
			hospitalApiClient.updateAppointment(token, appointmentId, request);
			return "redirect:/appointments";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not update appointment.");
			return "redirect:/appointments/" + appointmentId + "/edit";
		}
	}

	@PostMapping("/{appointmentId}/delete")
	public String delete(@PathVariable String appointmentId, HttpSession session, RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			hospitalApiClient.deleteAppointment(token, appointmentId);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not delete appointment.");
		}
		return "redirect:/appointments";
	}

	private boolean loadDoctorOptions(String token, RedirectAttributes ra, Model model) {
		try {
			model.addAttribute("doctorOptions", hospitalApiClient.listDoctorsForSelect(token));
			return true;
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load doctors for appointment linking.");
			return false;
		}
	}
}
