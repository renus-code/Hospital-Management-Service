package com.hospital.auth.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hospital.auth.model.Role;
import com.hospital.auth.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	public DataInitializer(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) {
		if (roleRepository.count() > 0) {
			return;
		}
		roleRepository.save(Role.builder()
				.roleTitle("ADMIN")
				.roleDescription("Full system administration")
				.build());
		roleRepository.save(Role.builder()
				.roleTitle("USER")
				.roleDescription("Standard application user")
				.build());
		roleRepository.save(Role.builder()
				.roleTitle("DOCTOR")
				.roleDescription("Hospital doctor role")
				.build());
	}
}
