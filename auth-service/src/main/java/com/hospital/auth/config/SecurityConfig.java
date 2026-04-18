package com.hospital.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hospital.auth.security.JwtAuthenticationFilter;
import com.hospital.auth.security.JwtSessionAuthenticationSuccessHandler;
import com.hospital.auth.security.UnifiedFormLoginAuthenticationProvider;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtSessionAuthenticationSuccessHandler jwtSessionAuthenticationSuccessHandler;
	private final UnifiedFormLoginAuthenticationProvider unifiedFormLoginAuthenticationProvider;

	public SecurityConfig(
			JwtAuthenticationFilter jwtAuthenticationFilter,
			JwtSessionAuthenticationSuccessHandler jwtSessionAuthenticationSuccessHandler,
			UnifiedFormLoginAuthenticationProvider unifiedFormLoginAuthenticationProvider) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtSessionAuthenticationSuccessHandler = jwtSessionAuthenticationSuccessHandler;
		this.unifiedFormLoginAuthenticationProvider = unifiedFormLoginAuthenticationProvider;
	}

	/** REST API: JWT, stateless. */
	@Bean
	@Order(1)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/api/**")
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/ping").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/** Thymeleaf UI: form login + HTTP session + CSRF. */
	@Bean
	@Order(2)
	SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authenticationProvider(unifiedFormLoginAuthenticationProvider)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**", "/login", "/register", "/error").permitAll()
						.requestMatchers(HttpMethod.POST, "/register").permitAll()
						.requestMatchers(HttpMethod.GET, "/").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler(jwtSessionAuthenticationSuccessHandler)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.permitAll());
		return http.build();
	}

}
