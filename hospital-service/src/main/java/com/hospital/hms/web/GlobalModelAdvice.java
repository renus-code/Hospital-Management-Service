package com.hospital.hms.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hospital.hms.config.AuthServiceProperties;

@ControllerAdvice(basePackages = "com.hospital.hms.web")
public class GlobalModelAdvice {

	private final AuthServiceProperties authServiceProperties;

	public GlobalModelAdvice(AuthServiceProperties authServiceProperties) {
		this.authServiceProperties = authServiceProperties;
	}

	@ModelAttribute("authConsoleUrl")
	public String authConsoleUrl() {
		return authServiceProperties.getBaseUrl().replaceAll("/$", "");
	}
}
