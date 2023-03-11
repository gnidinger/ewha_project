package com.ewha.back.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.like.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
