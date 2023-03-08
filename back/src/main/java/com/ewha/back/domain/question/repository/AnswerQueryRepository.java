package com.ewha.back.domain.question.repository;

import static com.ewha.back.domain.question.entity.QAnswer.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.user.entity.User;
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

	public Page<Answer> findMyAnswers(User findUser, Pageable pageable) {
		List<Answer> answerList = jpaQueryFactory
			.selectFrom(answer)
			.where(answer.user.eq(findUser))
			.orderBy(answer.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(answerList, pageable, answerList.size());

	}
}
