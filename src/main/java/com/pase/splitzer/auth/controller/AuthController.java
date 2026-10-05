package com.pase.splitzer.auth.controller;

import java.nio.charset.StandardCharsets;

import com.pase.splitzer.auth.model.AppUser;
import com.pase.splitzer.auth.model.RegisterRequest;
import com.pase.splitzer.auth.model.RegisterResponse;
import com.pase.splitzer.auth.model.TokenRequest;
import com.pase.splitzer.auth.model.TokenResponse;
import com.pase.splitzer.auth.service.JwtTokenService;
import com.pase.splitzer.auth.service.UserAccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserAccountService userAccountService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
		AppUser user = userAccountService.register(
				request.getUsername(),
				request.getPassword(),
				request.getName(),
				request.getEmail(),
				request.getPictureUrl());
		return new RegisterResponse(user.getId(), user.getUsername(), user.getStatus());
	}

	@PostMapping("/token")
	public TokenResponse createToken(@Valid @RequestBody TokenRequest request) {
		if (request.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}
		try {
			var authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			return jwtTokenService.createToken(authentication);
		}
		catch (AuthenticationException exception) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}
	}
}
