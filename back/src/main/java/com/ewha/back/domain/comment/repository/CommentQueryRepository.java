package com.ewha.back.domain.comment.repository;

import static com.ewha.back.domain.comment.entity.QComment.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Comment> findCommentListByUser(User user, Pageable pageable) {
		List<Comment> commentList = jpaQueryFactory
			.select(comment)
			.from(comment)
			.where(comment.user.eq(user))
			.orderBy(comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(comment.count())
			.from(comment)
			.fetchOne();

		return new PageImpl<>(commentList, pageable, total);
	}

	public Page<Comment> findFeedComment(Long feedId, Pageable pageable) {
		List<Comment> commentList = jpaQueryFactory
			.selectFrom(comment)
			.where(comment.feed.id.eq(feedId))
			.orderBy(comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(comment.count())
			.from(comment)
			.fetchOne();

		return new PageImpl<>(commentList, pageable, total);
	}
}
