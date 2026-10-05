package com.pase.splitzer.auth.service;

import java.util.Locale;
import java.nio.charset.StandardCharsets;

import com.pase.splitzer.auth.model.AppUser;
import com.pase.splitzer.auth.model.AccountStatus;
import com.pase.splitzer.auth.repository.AppUserRepository;
import com.pase.splitzer.repository.UserProfileRepository;
import com.pase.splitzer.model.UserSummary;
import com.pase.splitzer.model.UserProfile;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountService {

	private final AppUserRepository users;
	private final UserProfileRepository profiles;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public AppUser register(String username, String password, String name, String email, String pictureUrl) {
		if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Password must not exceed 72 UTF-8 bytes");
		}
		String normalizedUsername = normalizeUsername(username);
		if (users.existsByUsername(normalizedUsername)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already registered");
		}

		String normalizedEmail = email == null || email.isBlank()
				? null
				: email.trim().toLowerCase(Locale.ROOT);
		if (normalizedEmail != null && profiles.existsByEmail(normalizedEmail)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
		}

		AppUser user = users.save(new AppUser(normalizedUsername, passwordEncoder.encode(password)));
		profiles.save(new UserProfile(user, normalizeOptional(name), normalizedEmail, normalizeOptional(pictureUrl)));
		return user;
	}

	@Transactional(readOnly = true)
	public List<UserSummary> getUsers() {
		return users.findAllByOrderByIdAsc().stream()
				.map(user -> {
					UserProfile profile = user.getProfile();
					return new UserSummary(
							user.getId(),
							user.getUsername(),
							user.getRole(),
							profile == null ? null : profile.getName(),
							profile == null ? null : profile.getEmail(),
							profile == null ? null : profile.getPictureUrl(),
							user.getStatus());
				})
				.toList();
	}

	@Transactional
	public UserSummary approveUser(Long userId) {
		AppUser user = users.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		user.approve();
		UserProfile profile = user.getProfile();
		return new UserSummary(
				user.getId(),
				user.getUsername(),
				user.getRole(),
				profile == null ? null : profile.getName(),
				profile == null ? null : profile.getEmail(),
				profile == null ? null : profile.getPictureUrl(),
				AccountStatus.APPROVED);
	}

	private String normalizeOptional(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}

	public static String normalizeUsername(String username) {
		return username.trim().toLowerCase(Locale.ROOT);
	}
}
