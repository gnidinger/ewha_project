package com.ewha.back.domain.like.repository;

import static com.ewha.back.domain.like.entity.LikeType.*;
import static com.ewha.back.domain.like.entity.QFeedLike.*;
import static com.ewha.back.domain.like.entity.QLike.*;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.like.entity.FeedLike;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedLikeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public FeedLike findFeedLikeByFeedAndUser(Feed findFeed, User findUser) {

		return jpaQueryFactory.selectFrom(feedLike)
			.where(feedLike.feed.eq(findFeed).and(feedLike.user.eq(findUser)))
			.fetchFirst();
	}
}
