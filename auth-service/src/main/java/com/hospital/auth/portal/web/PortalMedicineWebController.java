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
import com.hospital.auth.portal.dto.MedicineFormPayload;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/medicines")
public class PortalMedicineWebController {

	private final HospitalApiClient hospitalApiClient;

	public PortalMedicineWebController(HospitalApiClient hospitalApiClient) {
		this.hospitalApiClient = hospitalApiClient;
	}

	@GetMapping
	public String list(HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			model.addAttribute("medicines", hospitalApiClient.listMedicines(token));
			return "medicines/list";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load medicines. Is hospital-service running on port 8082?");
			return "redirect:/dashboard";
		}
	}

	@GetMapping("/new")
	@PreAuthorize("hasRole('ADMIN')")
	public String createForm(HttpSession session, RedirectAttributes ra, Model model) {
		if (PortalSessionSupport.requireToken(session, ra) == null) {
			return "redirect:/login";
		}
		if (!model.containsAttribute("medicineRequest")) {
			model.addAttribute("medicineRequest", new MedicineFormPayload("", "", "", "", ""));
		}
		model.addAttribute("editMode", false);
		return "medicines/form";
	}

	@GetMapping("/{medicinesId}/edit")
	@PreAuthorize("hasRole('ADMIN')")
	public String editForm(@PathVariable String medicinesId, HttpSession session, RedirectAttributes ra, Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			var m = hospitalApiClient.getMedicine(token, medicinesId);
			model.addAttribute("medicineRequest", new MedicineFormPayload(
					m.medicinesName(),
					m.medicinesCompany(),
					m.medicinesCost(),
					m.medicinesType(),
					m.medicinesDescription()));
			model.addAttribute("medicinesId", medicinesId);
			model.addAttribute("editMode", true);
			return "medicines/form";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not load medicine.");
			return "redirect:/medicines";
		}
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public String create(
			@Valid @ModelAttribute("medicineRequest") MedicineFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("editMode", false);
			return "medicines/form";
		}
		try {
			hospitalApiClient.createMedicine(token, request);
			return "redirect:/medicines";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not create medicine.");
			return "redirect:/medicines/new";
		}
	}

	@PostMapping("/{medicinesId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String update(
			@PathVariable String medicinesId,
			@Valid @ModelAttribute("medicineRequest") MedicineFormPayload request,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes ra,
			Model model) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("medicinesId", medicinesId);
			model.addAttribute("editMode", true);
			return "medicines/form";
		}
		try {
			hospitalApiClient.updateMedicine(token, medicinesId, request);
			return "redirect:/medicines";
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not update medicine.");
			return "redirect:/medicines/" + medicinesId + "/edit";
		}
	}

	@PostMapping("/{medicinesId}/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public String delete(@PathVariable String medicinesId, HttpSession session, RedirectAttributes ra) {
		String token = PortalSessionSupport.requireToken(session, ra);
		if (token == null) {
			return "redirect:/login";
		}
		try {
			hospitalApiClient.deleteMedicine(token, medicinesId);
		}
		catch (RestClientException ex) {
			ra.addFlashAttribute("errorMessage", "Could not delete medicine.");
		}
		return "redirect:/medicines";
	}
}
