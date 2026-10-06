package com.pase.splitzer.controller;

import java.util.List;

import com.pase.splitzer.auth.model.AppUser;
import com.pase.splitzer.auth.model.RegisterRequest;
import com.pase.splitzer.auth.model.RegisterResponse;
import com.pase.splitzer.auth.service.UserAccountService;
import com.pase.splitzer.model.UserSummary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserAccountService userAccountService;

	@GetMapping
	public List<UserSummary> getUsers() {
		log.debug("Listing all users");
		return userAccountService.getUsers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse createUser(@Valid @RequestBody RegisterRequest request) {
		log.info("Admin create-user request for username '{}'", request.getUsername());
		AppUser user = userAccountService.register(
				request.getUsername(),
				request.getPassword(),
				request.getName(),
				request.getEmail(),
				request.getPictureUrl());
		return new RegisterResponse(user.getId(), user.getUsername(), user.getStatus());
	}

	@PutMapping("/{userId}/approve")
	public UserSummary approveUser(@PathVariable Long userId) {
		log.info("Approve request for user id {}", userId);
		return userAccountService.approveUser(userId);
	}
}
