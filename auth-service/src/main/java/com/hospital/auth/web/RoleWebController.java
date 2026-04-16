package com.hospital.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.auth.dto.AssignRoleRequest;
import com.hospital.auth.dto.RoleRequest;
import com.hospital.auth.service.RoleService;
import com.hospital.auth.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Controller
@RequestMapping("/roles")
public class RoleWebController {

	private final RoleService roleService;
	private final UserService userService;

	public RoleWebController(RoleService roleService, UserService userService) {
		this.roleService = roleService;
		this.userService = userService;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("roles", roleService.listRoles());
		return "roles/list";
	}

	@GetMapping("/assign-role")
	public String assignForm(Model model) {
		model.addAttribute("roles", roleService.listRoles());
		model.addAttribute("users", userService.listUsers());
		model.addAttribute("assignForm", new AssignForm());
		return "roles/assign";
	}

	@PostMapping("/assign-role")
	public String assign(@Valid @ModelAttribute("assignForm") AssignForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			model.addAttribute("users", userService.listUsers());
			return "roles/assign";
		}
		roleService.assignRole(new AssignRoleRequest(form.getUserId(), form.getRoleId()));
		return "redirect:/users";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("roleRequest", new RoleRequest("", ""));
		model.addAttribute("editMode", false);
		return "roles/form";
	}

	@GetMapping("/{roleId}/edit")
	public String editForm(@PathVariable String roleId, Model model) {
		var role = roleService.getRole(roleId);
		model.addAttribute("roleRequest", new RoleRequest(role.getRoleTitle(), role.getRoleDescription()));
		model.addAttribute("roleId", roleId);
		model.addAttribute("editMode", true);
		return "roles/form";
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("roleRequest") RoleRequest request, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("editMode", false);
			return "roles/form";
		}
		roleService.addRole(request);
		return "redirect:/roles";
	}

	@PostMapping("/{roleId}")
	public String update(
			@PathVariable String roleId,
			@Valid @ModelAttribute("roleRequest") RoleRequest request,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roleId", roleId);
			model.addAttribute("editMode", true);
			return "roles/form";
		}
		roleService.editRole(roleId, request);
		return "redirect:/roles";
	}

	@PostMapping("/{roleId}/delete")
	public String delete(@PathVariable String roleId) {
		roleService.deleteRole(roleId);
		return "redirect:/roles";
	}

	public static class AssignForm {
		@NotBlank
		private String userId;
		@NotBlank
		private String roleId;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getRoleId() {
			return roleId;
		}

		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}
	}
}
