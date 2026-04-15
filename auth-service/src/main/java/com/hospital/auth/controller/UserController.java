package com.hospital.auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.auth.dto.UserRequest;
import com.hospital.auth.dto.UserResponse;
import com.hospital.auth.dto.UserUpdateRequest;
import com.hospital.auth.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/** UML: addUser */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse addUser(@Valid @RequestBody UserRequest request) {
		return userService.addUser(request);
	}

	/** UML: editUser */
	@PutMapping("/{userId}")
	public UserResponse editUser(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest request) {
		return userService.editUser(userId, request);
	}

	/** UML: deleteUser */
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
	}

	/** UML: searchUser — optional {@code name} and {@code email}; lists all when both omitted or blank. */
	@GetMapping("/search")
	public List<UserResponse> searchUsers(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email) {
		return userService.searchUsers(name, email);
	}

	@GetMapping("/{userId}")
	public UserResponse getUser(@PathVariable String userId) {
		return userService.getUser(userId);
	}

	@GetMapping
	public List<UserResponse> listUsers() {
		return userService.listUsers();
	}
}
