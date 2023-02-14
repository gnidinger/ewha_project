package com.ewha.back.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
