package com.ewha.back.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.user.entity.UserCategory;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
