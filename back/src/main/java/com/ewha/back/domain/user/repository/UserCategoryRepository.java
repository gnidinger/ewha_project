package com.ewha.back.domain.user.repository;

import com.ewha.back.domain.user.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
