package com.hospital.auth.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.auth.dto.UserRequest;
import com.hospital.auth.dto.UserResponse;
import com.hospital.auth.dto.UserUpdateRequest;
import com.hospital.auth.exception.DuplicateResourceException;
import com.hospital.auth.exception.ResourceNotFoundException;
import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.repository.RoleRepository;
import com.hospital.auth.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponse addUser(UserRequest request) {
		userRepository.findByUserEmailIgnoreCase(request.userEmail()).ifPresent(u -> {
			throw new DuplicateResourceException("Email already registered: " + request.userEmail());
		});
		String roleId = resolveRoleId(request.userRoleId());
		User user = User.builder()
				.userName(request.userName().trim())
				.userEmail(request.userEmail().trim().toLowerCase())
				.userPassword(passwordEncoder.encode(request.userPassword()))
				.userDob(request.userDob())
				.userAddress(request.userAddress())
				.userRoleId(roleId)
				.build();
		User saved = userRepository.save(user);
		return toResponse(saved);
	}

	public UserResponse editUser(String userId, UserUpdateRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
		if (request.userEmail() != null && !request.userEmail().isBlank()) {
			String email = request.userEmail().trim().toLowerCase();
			userRepository.findByUserEmailIgnoreCase(email)
					.filter(u -> !u.getUserId().equals(userId))
					.ifPresent(u -> {
						throw new DuplicateResourceException("Email already in use: " + email);
					});
			user.setUserEmail(email);
		}
		if (request.userName() != null && !request.userName().isBlank()) {
			user.setUserName(request.userName().trim());
		}
		if (request.userPassword() != null && !request.userPassword().isBlank()) {
			user.setUserPassword(passwordEncoder.encode(request.userPassword()));
		}
		if (request.userDob() != null) {
			user.setUserDob(request.userDob());
		}
		if (request.userAddress() != null) {
			user.setUserAddress(request.userAddress());
		}
		if (request.userRoleId() != null && !request.userRoleId().isBlank()) {
			roleRepository.findById(request.userRoleId())
					.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.userRoleId()));
			user.setUserRoleId(request.userRoleId());
		}
		return toResponse(userRepository.save(user));
	}

	public void deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
		userRepository.delete(user);
	}

	public UserResponse getUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
		return toResponse(user);
	}

	public List<UserResponse> listUsers() {
		return userRepository.findAll().stream().map(this::toResponse).toList();
	}

	public List<UserResponse> searchUsers(String name, String email) {
		boolean hasName = name != null && !name.isBlank();
		boolean hasEmail = email != null && !email.isBlank();
		if (!hasName && !hasEmail) {
			return listUsers();
		}
		String n = hasName ? name.trim() : "";
		String e = hasEmail ? email.trim() : "";
		Stream<User> stream;
		if (hasName && hasEmail) {
			stream = userRepository.findByUserNameContainingIgnoreCaseAndUserEmailContainingIgnoreCase(n, e).stream();
		}
		else if (hasName) {
			stream = userRepository.findByUserNameContainingIgnoreCase(n).stream();
		}
		else {
			stream = userRepository.findByUserEmailContainingIgnoreCase(e).stream();
		}
		return stream.map(this::toResponse).toList();
	}

	public User getUserEntity(String userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
	}

	public User getUserEntityByEmail(String email) {
		return userRepository.findByUserEmailIgnoreCase(email.trim().toLowerCase())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
	}

	private String resolveRoleId(String userRoleId) {
		if (userRoleId != null && !userRoleId.isBlank()) {
			return roleRepository.findById(userRoleId)
					.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + userRoleId))
					.getRoleId();
		}
		Role defaultRole = roleRepository.findByRoleTitleIgnoreCase("USER")
				.orElseThrow(() -> new ResourceNotFoundException("Default role USER not found; run data seed"));
		return defaultRole.getRoleId();
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(
				user.getUserId(),
				user.getUserRoleId(),
				user.getUserName(),
				user.getUserEmail(),
				user.getUserDob(),
				user.getUserAddress());
	}
}
