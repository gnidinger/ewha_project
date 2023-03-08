package com.ewha.back.domain.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.question.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

	List<Answer> findByUserId(Long userId);

	Boolean existsByUserId(String userId);
}
