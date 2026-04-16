package com.hospital.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Lightweight smoke test (no Spring context). Full startup requires MongoDB; run the app locally or add
 * integration tests with Testcontainers when Docker is available.
 */
class AuthServiceApplicationTests {

	@Test
	void applicationClassLoads() {
		assertThat(AuthServiceApplication.class).isNotNull();
	}

}
