package com.pase.splitzer.model;

import com.pase.splitzer.auth.model.AppRole;
import com.pase.splitzer.auth.model.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummary {

	private final Long id;
	private final String username;
	private final AppRole role;
	private final String name;
	private final String email;
	private final String pictureUrl;
	private final AccountStatus status;
}
