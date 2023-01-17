package com.ewha.back.domain.question.repository;

import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ewha.back.domain.question.entity.QAnswer.answer;
import static com.ewha.back.domain.question.entity.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class QuestionQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Question> findQuestionListByUser(User user, Pageable pageable) {
        List<Question> questionList = jpaQueryFactory
                .selectFrom(question)
                .join(question.answers, answer)
                .where(answer.user.eq(user))
                .fetch();

        Long total = jpaQueryFactory
                .select(question.count())
                .from(question)
                .fetchOne();

        return new PageImpl<>(questionList, pageable, total);
    }
}
