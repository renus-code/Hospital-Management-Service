package com.hospital.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.auth.dto.AssignRoleRequest;
import com.hospital.auth.dto.RoleRequest;
import com.hospital.auth.exception.BadRequestException;
import com.hospital.auth.exception.DuplicateResourceException;
import com.hospital.auth.exception.ResourceNotFoundException;
import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.repository.RoleRepository;
import com.hospital.auth.repository.UserRepository;

@Service
public class RoleService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	public Role addRole(RoleRequest request) {
		roleRepository.findByRoleTitleIgnoreCase(request.roleTitle()).ifPresent(r -> {
			throw new DuplicateResourceException("Role title already exists: " + request.roleTitle());
		});
		Role role = Role.builder()
				.roleTitle(request.roleTitle().trim())
				.roleDescription(request.roleDescription())
				.build();
		return roleRepository.save(role);
	}

	public Role editRole(String roleId, RoleRequest request) {
		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));
		roleRepository.findByRoleTitleIgnoreCase(request.roleTitle()).filter(r -> !r.getRoleId().equals(roleId))
				.ifPresent(r -> {
					throw new DuplicateResourceException("Role title already exists: " + request.roleTitle());
				});
		role.setRoleTitle(request.roleTitle().trim());
		role.setRoleDescription(request.roleDescription());
		return roleRepository.save(role);
	}

	public void deleteRole(String roleId) {
		roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));
		if (userRepository.existsByUserRoleId(roleId)) {
			throw new BadRequestException("Cannot delete role that is assigned to users");
		}
		roleRepository.deleteById(roleId);
	}

	public Role getRole(String roleId) {
		return roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));
	}

	public List<Role> listRoles() {
		return roleRepository.findAll();
	}

	public List<Role> searchRoles(String title) {
		if (title == null || title.isBlank()) {
			return listRoles();
		}
		return roleRepository.findByRoleTitleContainingIgnoreCase(title.trim());
	}

	/** UML: assignRole — assigns a {@link Role} to a {@link User}. */
	public User assignRole(AssignRoleRequest request) {
		Role role = roleRepository.findById(request.roleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.roleId()));
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));
		user.setUserRoleId(role.getRoleId());
		return userRepository.save(user);
	}
}
