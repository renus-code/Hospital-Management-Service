package com.hospital.auth.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * After browser form login, issue the same JWT as /api/auth/login and store it in the session so
 * the unified UI can call hospital-service with {@code Authorization: Bearer …}.
 */
@Component
public class JwtSessionAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	public static final String SESSION_ACCESS_TOKEN = "ACCESS_TOKEN";

	private final JwtService jwtService;

	public JwtSessionAuthenticationSuccessHandler(JwtService jwtService) {
		this.jwtService = jwtService;
		setDefaultTargetUrl("/dashboard");
		setAlwaysUseDefaultTargetUrl(true);
	}

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (!(authentication.getDetails() instanceof LoginOutcome outcome)) {
			throw new IllegalStateException("Missing LoginOutcome; check UnifiedFormLoginAuthenticationProvider wiring");
		}
		String token = jwtService.generateToken(outcome.subjectId(), outcome.email(), outcome.roleTitle());
		request.getSession().setAttribute(SESSION_ACCESS_TOKEN, token);
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
