package com.pase.splitzer.auth.repository;

import java.util.Optional;

import com.pase.splitzer.auth.model.AppUser;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String username);

	boolean existsByUsername(String username);

	@EntityGraph(attributePaths = "profile")
	java.util.List<AppUser> findAllByOrderByIdAsc();
}
