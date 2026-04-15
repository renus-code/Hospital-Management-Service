package com.hospital.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.auth.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUserEmailIgnoreCase(String userEmail);

	List<User> findByUserNameContainingIgnoreCase(String userName);

	List<User> findByUserEmailContainingIgnoreCase(String userEmail);

	List<User> findByUserNameContainingIgnoreCaseAndUserEmailContainingIgnoreCase(String userName, String userEmail);

	boolean existsByUserRoleId(String userRoleId);
}
