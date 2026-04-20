package com.hospital.patient.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Browser UI is served by auth-service (8081). Patient-service remains API-first.
 */
@Controller
public class UiRedirectToAuthController {

	private final String authUiBase;

	public UiRedirectToAuthController(@Value("${services.auth.ui-base-url:http://localhost:8081}") String authUiBase) {
		this.authUiBase = authUiBase.replaceAll("/$", "");
	}

	@GetMapping({ "/", "/login", "/dashboard", "/patients", "/patients/**" })
	public RedirectView redirect(HttpServletRequest request) {
		String query = request.getQueryString();
		String target = authUiBase + request.getRequestURI() + (query != null ? "?" + query : "");
		return new RedirectView(target);
	}
}
