package com.pase.splitzer.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {

	private String secret;
	private long expirationSeconds;

	public JwtProperties() {
	}

	public void validate() {
		if (secret == null || secret.isBlank()) {
			throw new IllegalArgumentException("A JWT signing secret must be configured");
		}
		if (expirationSeconds <= 0) {
			throw new IllegalArgumentException("JWT expiration must be greater than zero seconds");
		}
	}
}
