package com.pase.splitzer.auth.service;

import java.time.Instant;

import com.pase.splitzer.auth.model.TokenResponse;
import com.pase.splitzer.security.JwtProperties;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final JwtEncoder jwtEncoder;
	private final JwtProperties properties;

	public TokenResponse createToken(Authentication authentication) {
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(properties.getExpirationSeconds());
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("splitzer")
				.subject(authentication.getName())
				.claim("roles", authentication.getAuthorities().stream()
						.map(authority -> authority.getAuthority())
						.filter(authority -> authority.startsWith("ROLE_"))
						.toList())
				.issuedAt(issuedAt)
				.expiresAt(expiresAt)
				.build();

		String token = jwtEncoder.encode(JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
		return new TokenResponse(token, "Bearer", properties.getExpirationSeconds());
	}
}
