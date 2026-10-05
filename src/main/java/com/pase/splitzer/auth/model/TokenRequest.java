package com.pase.splitzer.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequest {

	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(max = 72)
	private String password;

}
