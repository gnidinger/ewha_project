package com.ewha.back.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ewha.back.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(String userId);
	User findByNickname(String nickname);
	User findByProviderId(String providerId);
	Boolean existsByUserId(String userId);
	Boolean existsByNickname(String nickname);
}
