package com.pase.splitzer.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(min = 8, max = 72)
	private String password;

	@Size(max = 100)
	private String name;

	@Email
	@Size(max = 254)
	private String email;

	@Size(max = 2048)
	private String pictureUrl;

}
