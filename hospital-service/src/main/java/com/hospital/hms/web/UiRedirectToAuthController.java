package com.hospital.hms.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Browser UI lives on auth-service (8081). Old bookmarks to this port redirect to the unified app.
 */
@Controller
public class UiRedirectToAuthController {

	private final String authUiBase;

	public UiRedirectToAuthController(@Value("${services.auth.ui-base-url:http://localhost:8081}") String authUiBase) {
		this.authUiBase = authUiBase.replaceAll("/$", "");
	}

	@GetMapping({ "/", "/login", "/dashboard", "/hospitals", "/hospitals/**", "/doctors", "/doctors/**", "/patients", "/patients/**" })
	public RedirectView redirect(HttpServletRequest request) {
		String query = request.getQueryString();
		String target = authUiBase + request.getRequestURI() + (query != null ? "?" + query : "");
		return new RedirectView(target);
	}
}
