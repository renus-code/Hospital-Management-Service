package com.hospital.auth.controller;

import java.util.List;

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

import com.hospital.auth.dto.PermissionRequest;
import com.hospital.auth.model.Permission;
import com.hospital.auth.service.PermissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	/** UML: addPermission */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Permission addPermission(@Valid @RequestBody PermissionRequest request) {
		return permissionService.addPermission(request);
	}

	/** UML: editPermission */
	@PutMapping("/{permissionId}")
	public Permission editPermission(
			@PathVariable String permissionId,
			@Valid @RequestBody PermissionRequest request) {
		return permissionService.editPermission(permissionId, request);
	}

	/** UML: deletePermission */
	@DeleteMapping("/{permissionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePermission(@PathVariable String permissionId) {
		permissionService.deletePermission(permissionId);
	}

	/** UML: searchPermission — optional {@code title} and {@code module}; lists all when both omitted or blank. */
	@GetMapping("/search")
	public List<Permission> searchPermissions(
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String module) {
		return permissionService.searchPermissions(title, module);
	}

	@GetMapping("/{permissionId}")
	public Permission getPermission(@PathVariable String permissionId) {
		return permissionService.getPermission(permissionId);
	}

	@GetMapping
	public List<Permission> listPermissions() {
		return permissionService.listPermissions();
	}
}
