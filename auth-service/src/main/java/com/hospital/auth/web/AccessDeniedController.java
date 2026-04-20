package com.hospital.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Friendly 403 page served when a signed-in user tries to reach a module outside their role.
 * Wired from {@code SecurityConfig#webSecurityFilterChain.exceptionHandling.accessDeniedPage}.
 */
@Controller
public class AccessDeniedController {

	@GetMapping("/access-denied")
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String accessDenied() {
		return "access-denied";
	}
}
