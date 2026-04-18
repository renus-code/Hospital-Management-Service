package com.hospital.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.auth.dto.UserRequest;
import com.hospital.auth.dto.UserUpdateRequest;
import com.hospital.auth.service.RoleService;
import com.hospital.auth.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller
@RequestMapping("/users")
public class UserWebController {

	private final UserService userService;
	private final RoleService roleService;

	public UserWebController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("users", userService.listUsers());
		return "users/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("roles", roleService.listRoles());
		model.addAttribute("userRequest", new UserCreateForm());
		return "users/form-create";
	}

	@PostMapping
	public String create(
			@Valid @ModelAttribute("userRequest") UserCreateForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			return "users/form-create";
		}
		String roleId = form.getUserRoleId();
		if (roleId != null && roleId.isBlank()) {
			roleId = null;
		}
		userService.addUser(new UserRequest(
				form.getUserName(),
				form.getUserEmail(),
				form.getUserPassword(),
				form.getUserDob(),
				form.getUserAddress(),
				form.getUserAddress(),
				roleId));
		return "redirect:/users";
	}

	@GetMapping("/{userId}/edit")
	public String editForm(@PathVariable String userId, Model model) {
		var user = userService.getUser(userId);
		model.addAttribute("roles", roleService.listRoles());
		var f = new UserEditForm();
		f.setUserName(user.userName());
		f.setUserEmail(user.userEmail());
		f.setUserDob(user.userDob());
		f.setUserAddress(user.userAddress());
		f.setUserRoleId(user.userRoleId());
		model.addAttribute("userEdit", f);
		model.addAttribute("userId", userId);
		return "users/form-edit";
	}

	@PostMapping("/{userId}")
	public String update(
			@PathVariable String userId,
			@Valid @ModelAttribute("userEdit") UserEditForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			model.addAttribute("userId", userId);
			return "users/form-edit";
		}
		String pwd = form.getUserPassword();
		if (pwd != null && pwd.isBlank()) {
			pwd = null;
		}
		String roleId = form.getUserRoleId();
		if (roleId != null && roleId.isBlank()) {
			roleId = null;
		}
		userService.editUser(userId, new UserUpdateRequest(
				form.getUserName(),
				form.getUserEmail(),
				pwd,
				form.getUserDob(),
				form.getUserAddress(),
				form.getUserAddress(),
				roleId));
		return "redirect:/users";
	}

	@PostMapping("/{userId}/delete")
	public String delete(@PathVariable String userId) {
		userService.deleteUser(userId);
		return "redirect:/users";
	}

	public static class UserCreateForm {
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

	public static class UserEditForm {
		@NotBlank
		private String userName;
		@NotBlank
		@Email
		private String userEmail;
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
