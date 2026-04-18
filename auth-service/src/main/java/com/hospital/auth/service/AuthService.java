package com.hospital.auth.service;

import org.springframework.stereotype.Service;

import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.dto.LoginRequest;
import com.hospital.auth.dto.RegisterRequest;
import com.hospital.auth.dto.UserRequest;
import com.hospital.auth.exception.BadRequestException;
import com.hospital.auth.security.JwtService;
import com.hospital.auth.security.LoginOutcome;
import com.hospital.auth.security.UnifiedLoginService;

@Service
public class AuthService {

	private final UserService userService;
	private final JwtService jwtService;
	private final UnifiedLoginService unifiedLoginService;

	public AuthService(
			UserService userService,
			JwtService jwtService,
			UnifiedLoginService unifiedLoginService) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.unifiedLoginService = unifiedLoginService;
	}

	/**
	 * {@code userEmail} field accepts admin email, or doctor/patient username (UML-aligned).
	 */
	public AuthResponse login(LoginRequest request) {
		LoginOutcome outcome = unifiedLoginService.authenticate(request.userEmail(), request.userPassword())
				.orElseThrow(() -> new BadRequestException("Invalid credentials"));
		String token = jwtService.generateToken(outcome.subjectId(), outcome.email(), outcome.roleTitle());
		return new AuthResponse(
				token,
				"Bearer",
				jwtService.getExpirationMs() / 1000,
				outcome.subjectId(),
				outcome.roleTitle());
	}

	public AuthResponse register(RegisterRequest request) {
		UserRequest userRequest = new UserRequest(
				request.userName(),
				request.userEmail(),
				request.userPassword(),
				request.userDob(),
				request.userAdd(),
				request.userAddress(),
				request.userRoleId());
		userService.addUser(userRequest);
		return login(new LoginRequest(request.userEmail(), request.userPassword()));
	}
}
