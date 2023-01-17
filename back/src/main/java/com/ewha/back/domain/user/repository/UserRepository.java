package com.ewha.back.domain.user.repository;


import com.ewha.back.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);
    User findByNickname(String nickname);
    Boolean existsByUserId(String userId);
    Boolean existsByNickname(String nickname);
}
