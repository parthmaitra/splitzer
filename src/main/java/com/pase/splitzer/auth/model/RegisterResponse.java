package com.pase.splitzer.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

	private final Long id;
	private final String username;
	private final AccountStatus status;
}
