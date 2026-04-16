package com.hospital.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hospital.auth.model.Role;
import com.hospital.auth.model.User;
import com.hospital.auth.repository.RoleRepository;
import com.hospital.auth.repository.UserRepository;

@Service
public class MongoUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public MongoUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByUserEmailIgnoreCase(email.trim().toLowerCase())
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
		String roleTitle = roleRepository.findById(user.getUserRoleId())
				.map(Role::getRoleTitle)
				.orElse("USER");
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUserEmail())
				.password(user.getUserPassword())
				.authorities("ROLE_" + roleTitle)
				.build();
	}
}
