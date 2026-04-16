package com.hospital.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.auth.dto.RegisterRequest;
import com.hospital.auth.service.AuthService;
import com.hospital.auth.service.RoleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller
public class AuthWebController {

	private final AuthService authService;
	private final RoleService roleService;

	public AuthWebController(AuthService authService, RoleService roleService) {
		this.authService = authService;
		this.roleService = roleService;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("roles", roleService.listRoles());
		if (!model.containsAttribute("registerRequest")) {
			model.addAttribute("registerRequest", new RegisterForm());
		}
		return "register";
	}

	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("registerRequest") RegisterForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			return "register";
		}
		String roleId = form.getUserRoleId();
		if (roleId != null && roleId.isBlank()) {
			roleId = null;
		}
		RegisterRequest request = new RegisterRequest(
				form.getUserName(),
				form.getUserEmail(),
				form.getUserPassword(),
				form.getUserDob(),
				form.getUserAddress(),
				roleId);
		authService.register(request);
		redirectAttributes.addFlashAttribute("successMessage", "Account created. Please sign in.");
		return "redirect:/login";
	}

	/** Mutable form backing bean for Thymeleaf (records are awkward for checkbox/date binding). */
	public static class RegisterForm {

		@NotBlank
		private String userName;
		@NotBlank
		@Email
		private String userEmail;
		@NotBlank
		private String userPassword;
		private java.time.LocalDate userDob;
		private String userAddress;
		private String userRoleId;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserEmail() {
			return userEmail;
		}

		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}

		public String getUserPassword() {
			return userPassword;
		}

		public void setUserPassword(String userPassword) {
			this.userPassword = userPassword;
		}

		public java.time.LocalDate getUserDob() {
			return userDob;
		}

		public void setUserDob(java.time.LocalDate userDob) {
			this.userDob = userDob;
		}

		public String getUserAddress() {
			return userAddress;
		}

		public void setUserAddress(String userAddress) {
			this.userAddress = userAddress;
		}

		public String getUserRoleId() {
			return userRoleId;
		}

		public void setUserRoleId(String userRoleId) {
			this.userRoleId = userRoleId;
		}
	}
}
