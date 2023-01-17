package com.ewha.back.domain.question.repository;

import com.ewha.back.domain.question.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByUserId(Long userId);
    Boolean existsByUserId(String userId);
}
