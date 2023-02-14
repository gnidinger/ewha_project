package com.ewha.back.domain.question.repository;

import static com.ewha.back.domain.question.entity.QAnswer.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.question.entity.Answer;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Answer> findByUserId(Long userId) {
		return jpaQueryFactory
			.select(answer)
			.from(answer)
			.where(answer.user.id.eq(userId))
			.fetch();
	}

	public Answer findByQuestionIdAndUserId(Long questionId, Long userId) {
		return jpaQueryFactory
			.select(answer)
			.from(answer)
			.where(answer.question.id.eq(questionId).and(answer.user.id.eq(userId)))
			.fetchFirst();
	}
}
