package com.hospital.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.auth.dto.PermissionRequest;
import com.hospital.auth.service.PermissionService;
import com.hospital.auth.service.RoleService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/permissions")
public class PermissionWebController {

	private final PermissionService permissionService;
	private final RoleService roleService;

	public PermissionWebController(PermissionService permissionService, RoleService roleService) {
		this.permissionService = permissionService;
		this.roleService = roleService;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("permissions", permissionService.listPermissions());
		return "permissions/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("roles", roleService.listRoles());
		model.addAttribute("permissionRequest", new PermissionRequest("", "", "", ""));
		model.addAttribute("editMode", false);
		return "permissions/form";
	}

	@GetMapping("/{permissionId}/edit")
	public String editForm(@PathVariable String permissionId, Model model) {
		var p = permissionService.getPermission(permissionId);
		model.addAttribute("roles", roleService.listRoles());
		model.addAttribute("editMode", true);
		model.addAttribute("permissionRequest", new PermissionRequest(
				p.getPermissionRoleId(),
				p.getPermissionTitle(),
				p.getPermissionModule(),
				p.getPermissionDescription()));
		model.addAttribute("permissionId", permissionId);
		model.addAttribute("editMode", true);
		return "permissions/form";
	}

	@PostMapping
	public String create(
			@Valid @ModelAttribute("permissionRequest") PermissionRequest request,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			model.addAttribute("editMode", false);
			return "permissions/form";
		}
		permissionService.addPermission(request);
		return "redirect:/permissions";
	}

	@PostMapping("/{permissionId}")
	public String update(
			@PathVariable String permissionId,
			@Valid @ModelAttribute("permissionRequest") PermissionRequest request,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", roleService.listRoles());
			model.addAttribute("permissionId", permissionId);
			model.addAttribute("editMode", true);
			return "permissions/form";
		}
		permissionService.editPermission(permissionId, request);
		return "redirect:/permissions";
	}

	@PostMapping("/{permissionId}/delete")
	public String delete(@PathVariable String permissionId) {
		permissionService.deletePermission(permissionId);
		return "redirect:/permissions";
	}
}
