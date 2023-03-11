package com.ewha.back.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.like.entity.FeedLike;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
}
