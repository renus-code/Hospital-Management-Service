package com.hospital.auth.controller;

import com.hospital.auth.dto.AssignRoleRequest;
import com.hospital.auth.dto.RoleRequest;
import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Role addRole(@Valid @RequestBody RoleRequest request) {
		return roleService.addRole(request);
	}

	@PutMapping("/{roleId}")
	public Role editRole(@PathVariable String roleId, @Valid @RequestBody RoleRequest request) {
		return roleService.editRole(roleId, request);
	}


	@DeleteMapping("/{roleId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable String roleId) {
		roleService.deleteRole(roleId);
	}

	@GetMapping("/search")
	public List<Role> searchRoles(@RequestParam(required = false) String title) {
		return roleService.searchRoles(title);
	}

	@GetMapping("/{roleId}")
	public Role getRole(@PathVariable String roleId) {
		return roleService.getRole(roleId);
	}

	@GetMapping
	public List<Role> listRoles() {
		return roleService.listRoles();
	}

	@PostMapping("/assign")
	public User assignRole(@Valid @RequestBody AssignRoleRequest request) {
		return roleService.assignRole(request);
	}
}
