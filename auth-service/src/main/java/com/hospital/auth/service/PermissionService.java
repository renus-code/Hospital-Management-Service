package com.hospital.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.auth.dto.PermissionRequest;
import com.hospital.auth.exception.ResourceNotFoundException;
import com.hospital.auth.model.Permission;
import com.hospital.auth.repository.PermissionRepository;
import com.hospital.auth.repository.RoleRepository;

@Service
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final RoleRepository roleRepository;

	public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository;
	}

	public Permission addPermission(PermissionRequest request) {
		roleRepository.findById(request.permissionRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.permissionRoleId()));
		Permission permission = Permission.builder()
				.permissionRoleId(request.permissionRoleId())
				.permissionTitle(request.permissionTitle().trim())
				.permissionModule(request.permissionModule().trim())
				.permissionDescription(request.permissionDescription())
				.build();
		return permissionRepository.save(permission);
	}

	public Permission editPermission(String permissionId, PermissionRequest request) {
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionId));
		roleRepository.findById(request.permissionRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.permissionRoleId()));
		permission.setPermissionRoleId(request.permissionRoleId());
		permission.setPermissionTitle(request.permissionTitle().trim());
		permission.setPermissionModule(request.permissionModule().trim());
		permission.setPermissionDescription(request.permissionDescription());
		return permissionRepository.save(permission);
	}

	public void deletePermission(String permissionId) {
		Permission permission = permissionRepository.findById(permissionId)
				.orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionId));
		permissionRepository.delete(permission);
	}

	public Permission getPermission(String permissionId) {
		return permissionRepository.findById(permissionId)
				.orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionId));
	}

	public List<Permission> listPermissions() {
		return permissionRepository.findAll();
	}

	public List<Permission> searchPermissions(String title, String module) {
		boolean hasTitle = title != null && !title.isBlank();
		boolean hasModule = module != null && !module.isBlank();
		if (!hasTitle && !hasModule) {
			return listPermissions();
		}
		String t = hasTitle ? title.trim() : "";
		String m = hasModule ? module.trim() : "";
		if (hasTitle && hasModule) {
			return permissionRepository.findByPermissionTitleContainingIgnoreCaseAndPermissionModuleContainingIgnoreCase(t, m);
		}
		if (hasTitle) {
			return permissionRepository.findByPermissionTitleContainingIgnoreCase(t);
		}
		return permissionRepository.findByPermissionModuleContainingIgnoreCase(m);
	}
}
