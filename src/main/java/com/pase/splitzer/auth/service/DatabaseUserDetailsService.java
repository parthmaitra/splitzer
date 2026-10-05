package com.pase.splitzer.auth.service;

import com.pase.splitzer.auth.model.AppUser;
import com.pase.splitzer.auth.model.AccountStatus;
import com.pase.splitzer.auth.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

	private final AppUserRepository users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String normalizedUsername = UserAccountService.normalizeUsername(username);
		AppUser appUser = users.findByUsername(normalizedUsername)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return User.withUsername(appUser.getUsername())
				.password(appUser.getPasswordHash())
				.roles(appUser.getRole().name())
				.disabled(appUser.getStatus() != AccountStatus.APPROVED)
				.build();
	}
}
