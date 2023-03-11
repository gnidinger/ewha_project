package com.ewha.back.domain.like.repository;

import static com.ewha.back.domain.like.entity.LikeType.*;
import static com.ewha.back.domain.like.entity.QCommentLike.*;
import static com.ewha.back.domain.like.entity.QFeedLike.*;
import static com.ewha.back.domain.like.entity.QLike.*;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.like.entity.CommentLike;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentLikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public CommentLike findCommentLikeByFeedAndUser(Comment findComment, User findUser) {

		return jpaQueryFactory.selectFrom(commentLike)
			.where(commentLike.comment.eq(findComment).and(commentLike.user.eq(findUser)))
			.fetchFirst();
	}
}
