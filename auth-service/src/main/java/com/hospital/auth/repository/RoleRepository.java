package com.hospital.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.auth.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	Optional<Role> findByRoleTitleIgnoreCase(String roleTitle);

	List<Role> findByRoleTitleContainingIgnoreCase(String roleTitle);
}
