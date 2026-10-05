package com.pase.splitzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pase.splitzer.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	boolean existsByEmail(String email);
}
