package com.pase.splitzer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping("/api/secure")
public class SecurePingController {

	@GetMapping("/ping")
	public PingResponse ping(Authentication authentication) {
		return new PingResponse("Authenticated", authentication.getName());
	}

	@Getter
	@AllArgsConstructor
	public static class PingResponse {

		private final String message;
		private final String username;
	}
}
