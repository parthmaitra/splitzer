package com.pase.splitzer.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import com.pase.splitzer.model.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "app_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 60)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "app_role", length = 20)
	private AppRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", length = 20)
	private AccountStatus status;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private UserProfile profile;

	public AppUser(String username, String passwordHash) {
		this(username, passwordHash, AppRole.USER, AccountStatus.PENDING);
	}

	public AppUser(String username, String passwordHash, AppRole role) {
		this(username, passwordHash, role, AccountStatus.PENDING);
	}

	public AppUser(String username, String passwordHash, AppRole role, AccountStatus status) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.role = role;
		this.status = status;
	}

	public AppRole getRole() {
		return role == null ? AppRole.USER : role;
	}

	public AccountStatus getStatus() {
		return status == null ? AccountStatus.APPROVED : status;
	}

	public void approve() {
		status = AccountStatus.APPROVED;
	}
}
