package com.hospital.auth.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Replaces default email-only {@code DaoAuthenticationProvider} so doctors/patients can sign in with UML
 * usernames while admins still use email.
 */
@Component
public class UnifiedFormLoginAuthenticationProvider implements AuthenticationProvider {

	private final UnifiedLoginService unifiedLoginService;

	public UnifiedFormLoginAuthenticationProvider(UnifiedLoginService unifiedLoginService) {
		this.unifiedLoginService = unifiedLoginService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getName();
		Object credentials = authentication.getCredentials();
		String password = credentials != null ? credentials.toString() : "";

		LoginOutcome outcome = unifiedLoginService.authenticate(principal, password)
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

		String authority = "ROLE_" + outcome.roleTitle();
		var token = new UsernamePasswordAuthenticationToken(
				outcome.subjectId(),
				null,
				List.of(new SimpleGrantedAuthority(authority)));
		token.setDetails(outcome);
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
