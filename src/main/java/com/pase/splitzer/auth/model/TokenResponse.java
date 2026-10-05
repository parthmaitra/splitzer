package com.pase.splitzer.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

	private final String accessToken;
	private final String tokenType;
	private final long expiresIn;
}
