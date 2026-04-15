package com.hospital.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.dto.LoginRequest;
import com.hospital.auth.dto.RegisterRequest;
import com.hospital.auth.dto.UserRequest;
import com.hospital.auth.exception.BadRequestException;
import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.repository.RoleRepository;
import com.hospital.auth.security.JwtService;

@Service
public class AuthService {

	private final UserService userService;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(
			UserService userService,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse login(LoginRequest request) {
		User user = userService.getUserEntityByEmail(request.userEmail());
		if (!passwordEncoder.matches(request.userPassword(), user.getUserPassword())) {
			throw new BadRequestException("Invalid credentials");
		}
		String roleTitle = roleRepository.findById(user.getUserRoleId())
				.map(Role::getRoleTitle)
				.orElse("USER");
		String token = jwtService.generateToken(user.getUserId(), user.getUserEmail(), roleTitle);
		return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000, user.getUserId(), roleTitle);
	}

	public AuthResponse register(RegisterRequest request) {
		UserRequest userRequest = new UserRequest(
				request.userName(),
				request.userEmail(),
				request.userPassword(),
				request.userDob(),
				request.userAddress(),
				request.userRoleId());
		userService.addUser(userRequest);
		return login(new LoginRequest(request.userEmail(), request.userPassword()));
	}
}
