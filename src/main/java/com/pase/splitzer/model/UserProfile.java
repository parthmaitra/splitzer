package com.pase.splitzer.model;

import com.pase.splitzer.auth.model.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "user_id")
	private AppUser user;

	@Column(length = 100)
	private String name;

	@Column(unique = true, length = 254)
	private String email;

	@Column(length = 2048)
	private String pictureUrl;

	public UserProfile(AppUser user, String name, String email, String pictureUrl) {
		this.user = user;
		this.name = name;
		this.email = email;
		this.pictureUrl = pictureUrl;
	}
}
